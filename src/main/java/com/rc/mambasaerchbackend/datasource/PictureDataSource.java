package com.rc.mambasaerchbackend.datasource;

import com.rc.mambasaerchbackend.model.vo.ArticleVO;
import com.rc.mambasaerchbackend.model.vo.PictureVO;
import com.rc.mambasaerchbackend.service.ArticleService;
import com.rc.mambasaerchbackend.service.PictureService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PictureDataSource implements DataSource<PictureVO> {

    @Resource
    private PictureService pictureService;

    @Override
    public List<PictureVO> doSearch(String searchText) {
        return pictureService.fetchPicture(searchText);
    }
}
