package com.rc.mambasaerchbackend.controller;

import com.rc.mambasaerchbackend.common.BaseResponse;
import com.rc.mambasaerchbackend.model.entity.Picture;
import com.rc.mambasaerchbackend.model.vo.PictureVO;
import com.rc.mambasaerchbackend.service.PictureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 图片控制器
 */
@Tag(name = "图片管理", description = "图片增删改查")
@RestController
@RequestMapping("/api/picture")
public class PictureController {

    @Resource
    private PictureService pictureService;

    @GetMapping("/list")
    @Operation(summary = "图片获取列表")
    public BaseResponse<List<PictureVO>> getPictureList(String searchText) {
        List<PictureVO> pictures = pictureService.fetchPicture(searchText);
        return BaseResponse.success(pictures);
    }
}
