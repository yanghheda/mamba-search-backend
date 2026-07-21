package com.rc.mambasaerchbackend.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新文章请求
 */
@Data
public class ArticleUpdateDTO {

    @Size(max = 200, message = "标题最多200个字符")
    private String title;

    private String content;

    @Size(max = 500, message = "标签最多500个字符")
    private String tags;
}
