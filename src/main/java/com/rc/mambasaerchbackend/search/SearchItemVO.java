package com.rc.mambasaerchbackend.search;

import lombok.Data;

import java.time.LocalDateTime;

// 单条结果
@Data
public class SearchItemVO {
    private String id;
    private String type;
    private String title;
    private String content;
    private String userId;
}
