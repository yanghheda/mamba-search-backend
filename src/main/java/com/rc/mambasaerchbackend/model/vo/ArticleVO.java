package com.rc.mambasaerchbackend.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章响应 VO
 */
@Data
public class ArticleVO {

    private Long id;
    private String title;
    private String content;
    private String tags;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
