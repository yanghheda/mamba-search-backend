package com.rc.mambasaerchbackend.search;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightFieldParameters;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import org.springframework.stereotype.Service;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final SearchDocRepository docRepository;
    private final ElasticsearchOperations esOperations;

    /**
     * 新增/更新单条文档（增量同步用）
     */
    public boolean saveOrUpdate(SearchDoc doc) {
        try {
            docRepository.save(doc);
            return true;
        } catch (Exception e) {
            log.error("ES保存文档失败, id:{}", doc.getId(), e);
            return false;
        }
    }

    /**
     * 根据ID删除文档
     */
    public boolean deleteById(String id) {
        try {
            docRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("ES删除文档失败, id:{}", id, e);
            return false;
        }
    }

    /**
     * 批量导入（全量同步用，底层自动bulk）
     */
    public boolean bulkSave(List<SearchDoc> docList) {
        try {
            docRepository.saveAll(docList);
            return true;
        } catch (Exception e) {
            log.error("ES批量导入失败", e);
            return false;
        }
    }

    /**
     * 根据ID查询
     */
    public SearchDoc getById(String id) {
        return docRepository.findById(id).orElse(null);
    }


    /**
     * 聚合搜索
     */
    public SearchResultVO search(String keyword, String type, Integer pageNum, Integer pageSize) {
        // 1. 构建原生查询
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> {
                            // 多字段匹配：标题权重3倍
                            b.must(m -> m
                                    .multiMatch(mm -> mm
                                            .query(keyword)
                                            .fields(List.of("title^3", "content"))
                                            .analyzer("ik_smart")
                                            .minimumShouldMatch("70%")
                                    )
                            );
                            // 按类型过滤
                            if (org.springframework.util.StringUtils.hasText(type)) {
                                b.filter(f -> f.term(t -> t.field("type").value(type)));
                            }
                            return b;
                        })
                )
                // 分页
                .withPageable(PageRequest.of(pageNum - 1, pageSize))
                // 排序：相关度优先，其次创建时间倒序
                .withSort(s -> s.score(sc -> sc.order(SortOrder.Desc)))
                .withSort(s -> s.field(f -> f.field("createTime").order(SortOrder.Desc)))
                // 高亮配置

                .withHighlightQuery(new HighlightQuery(
                        new Highlight(
                                HighlightParameters.builder()
                                        .withPreTags("<em class='highlight'>")
                                        .withPostTags("</em>")
                                        .build(),
                                List.of(
                                        new HighlightField("title"),
                                        new HighlightField("content", HighlightFieldParameters.builder()
                                                .withFragmentSize(100)
                                                .build())
                                )
                        ),
                        null
                ))
                // 聚合：类型统计
                .withAggregation("type_count", Aggregation.of(a -> a
                        .terms(t -> t.field("type").size(10))
                ))
                .build();

        // 2. 执行搜索
        SearchHits<SearchDoc> searchHits = esOperations.search(query, SearchDoc.class);

        // 3. 解析结果
        SearchResultVO result = new SearchResultVO();
        result.setTotal(searchHits.getTotalHits());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);

        // 解析列表 + 高亮替换
        List<SearchItemVO> list = new ArrayList<>();
        for (SearchHit<SearchDoc> hit : searchHits) {
            SearchDoc source = hit.getContent();
            SearchItemVO item = new SearchItemVO();
            // 拷贝基础属性
            org.springframework.beans.BeanUtils.copyProperties(source, item);
            // 替换高亮内容
            hit.getHighlightFields();
            if (hit.getHighlightFields().containsKey("title")) {
                item.setTitle(String.join("", hit.getHighlightFields().get("title")));
            }
            if (hit.getHighlightFields().containsKey("content")) {
                item.setContent(String.join("", hit.getHighlightFields().get("content")));
            }
            list.add(item);
        }
        result.setList(list);

        // 解析类型聚合
        List<TypeCountVO> typeCount = new ArrayList<>();
        var container = searchHits.getAggregations();
        if (container != null) {
            var esAggs = (ElasticsearchAggregations) container;
            var typeAgg = esAggs.aggregationsAsMap().get("type_count");
            if (typeAgg != null) {
                var stringTerms = typeAgg.aggregation().getAggregate().sterms();
                for (var bucket : stringTerms.buckets().array()) {
                    typeCount.add(new TypeCountVO(bucket.key().stringValue(), bucket.docCount()));
                }
            }
        }
        result.setTypeCount(typeCount);

        return result;
    }
}
