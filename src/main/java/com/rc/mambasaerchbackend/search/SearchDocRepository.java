package com.rc.mambasaerchbackend.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchDocRepository extends ElasticsearchRepository<SearchDoc, String> {

    // 简单查询可以直接用方法名派生，复杂查询用ElasticsearchOperations
    // 比如：根据类型查询
    List<SearchDoc> findByType(String type);

}
