package com.rc.mambasaerchbackend.datasource;

import com.rc.mambasaerchbackend.model.entity.Article;
import com.rc.mambasaerchbackend.model.entity.User;
import com.rc.mambasaerchbackend.model.vo.ArticleVO;
import com.rc.mambasaerchbackend.model.vo.UserVO;
import com.rc.mambasaerchbackend.search.ElasticsearchService;
import com.rc.mambasaerchbackend.search.SearchItemVO;
import com.rc.mambasaerchbackend.search.SearchResultVO;
import com.rc.mambasaerchbackend.service.ArticleService;
import com.rc.mambasaerchbackend.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleDataSource implements DataSource<ArticleVO> {

    @Resource
    private ArticleService articleService;

    @Resource
    private ElasticsearchService elasticsearchService;

    @Override
    public List<ArticleVO> doSearch(String searchText) {
        SearchResultVO searchResultVO = elasticsearchService.search(searchText, "article", 1, 2);
        List<SearchItemVO> list = searchResultVO.getList();
        //        return articleService.searchArticles(searchText,null,null);
        return list.stream().map(searchItemVO -> {
            ArticleVO articleVO = new ArticleVO();
            articleVO.setContent(searchItemVO.getContent());
            articleVO.setTitle(searchItemVO.getTitle());
            return articleVO;
        }).toList();
    }
}
