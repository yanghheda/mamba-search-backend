package com.rc.mambasaerchbackend.search;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;

@Data
@Document(indexName = "mamba_search") // 对应索引名
@Setting(shards = 1, replicas = 0, refreshInterval = "1s")
public class SearchDoc {

    @Id
    private String id; // 文档ID：article_1 / product_1 / user_1

    @Field(type = FieldType.Keyword)
    private String type; // 内容类型：article/user/product

    // 全文检索字段，指定IK分词器
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;

    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;

    @Field(type = FieldType.Keyword)
    private String userId;

    // 🔴 关键：指定日期格式，完美支持 yyyy-MM-dd HH:mm:ss
    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Field(type = FieldType.Date, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Field(type = FieldType.Keyword)
    private String isDeleted;

}
