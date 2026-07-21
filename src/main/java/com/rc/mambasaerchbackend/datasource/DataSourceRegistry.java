package com.rc.mambasaerchbackend.datasource;

import com.rc.mambasaerchbackend.enums.SearchTypeEnum;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceRegistry {
    @Resource
    private UserDataSource userDataSource;

    @Resource
    private ArticleDataSource articleDataSource;

    @Resource
    private PictureDataSource pictureDataSource;

    private Map<String, DataSource<?>> typeDataSourceMap;

    @PostConstruct
    public void doInit() {
        typeDataSourceMap = new HashMap() {{
            put(SearchTypeEnum.USER.getValue(),userDataSource);
            put(SearchTypeEnum.ARTICLE.getValue(),articleDataSource);
            put(SearchTypeEnum.PICTURE.getValue(),pictureDataSource);
        }};
    }


    public DataSource<?> getDataSourceByType (String type) {
        return typeDataSourceMap.get(type);
    }
}
