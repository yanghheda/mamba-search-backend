package com.rc.mambasaerchbackend.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * ES Repository 示例（仅在配置了 spring.elasticsearch.uris 时加载）
 */
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "uris")
public interface EsDocRepository extends ElasticsearchRepository<EsDocEntity, String> {
}
