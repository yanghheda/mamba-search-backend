package com.rc.mambasaerchbackend.search;

import lombok.AllArgsConstructor;
import lombok.Data;

// 类型统计
@Data
@AllArgsConstructor
public class TypeCountVO {
    private String type;
    private Long count;
}
