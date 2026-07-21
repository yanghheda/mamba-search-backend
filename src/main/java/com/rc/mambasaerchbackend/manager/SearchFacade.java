package com.rc.mambasaerchbackend.manager;

import com.rc.mambasaerchbackend.common.BaseResponse;
import com.rc.mambasaerchbackend.common.ErrorCode;
import com.rc.mambasaerchbackend.datasource.*;
import com.rc.mambasaerchbackend.enums.SearchTypeEnum;
import com.rc.mambasaerchbackend.model.dto.SearchDTO;
import com.rc.mambasaerchbackend.model.vo.SearchVO;
import com.rc.mambasaerchbackend.utils.ThrowUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchFacade {

    @Resource
    private UserDataSource userDataSource;

    @Resource
    private ArticleDataSource articleDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    @Resource
    private DataSourceRegistry dataSourceRegistry;


    public SearchVO searchAll(SearchDTO dto) {

        String type = dto.getType();
        ThrowUtils.throwIf(!StringUtils.hasText(type), ErrorCode.PARAMS_ERROR);
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        String searchText = dto.getSearchText();

        SearchVO searchVO = new SearchVO();
        if (searchTypeEnum == SearchTypeEnum.ALL) {
            searchVO.setUserList(userDataSource.doSearch(searchText));
            searchVO.setArticleList(articleDataSource.doSearch(searchText));
            searchVO.setPictureList(pictureDataSource.doSearch(searchText));
        } else {
            DataSource<?> dataSource = dataSourceRegistry.getDataSourceByType(type);
            List<?> objects = dataSource.doSearch(searchText);
            searchVO.setDataList(objects);
        }
        return searchVO;
    }
}
