package com.rc.mambasaerchbackend.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchResultVO {
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private List<SearchItemVO> list;
    private List<TypeCountVO> typeCount;
}

