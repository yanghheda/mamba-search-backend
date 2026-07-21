package com.rc.mambasaerchbackend.service;

import com.rc.mambasaerchbackend.model.entity.Picture;
import com.rc.mambasaerchbackend.model.vo.PictureVO;

import java.util.List;

public interface PictureService {
    List<PictureVO> fetchPicture(String searchText);
}
