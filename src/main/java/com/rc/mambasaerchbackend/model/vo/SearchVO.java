package com.rc.mambasaerchbackend.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchVO {

    private List<?> dataList;
    private List<UserVO> userList;
    private List<ArticleVO> articleList;
    private List<PictureVO> pictureList;
}
