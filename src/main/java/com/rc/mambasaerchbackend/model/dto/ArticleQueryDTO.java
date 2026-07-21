package com.rc.mambasaerchbackend.model.dto;

import lombok.Data;

/**
 * 文章查询请求
 */
@Data
public class ArticleQueryDTO {

    private String title;
    private String tags;
    private Long userId;
}
