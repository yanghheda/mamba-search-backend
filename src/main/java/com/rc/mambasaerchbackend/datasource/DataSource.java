package com.rc.mambasaerchbackend.datasource;

import java.util.List;

public interface DataSource<T> {

    /**
     * 搜索方法
     * @param searchText 搜索关键字
     * @return List<T>
     */
    List<T> doSearch(String searchText);
}
