package com.rc.mambasaerchbackend.controller;

import com.rc.mambasaerchbackend.common.BaseResponse;
import com.rc.mambasaerchbackend.common.ErrorCode;
import com.rc.mambasaerchbackend.enums.SearchTypeEnum;
import com.rc.mambasaerchbackend.manager.SearchFacade;
import com.rc.mambasaerchbackend.model.dto.SearchDTO;
import com.rc.mambasaerchbackend.model.vo.SearchVO;
import com.rc.mambasaerchbackend.utils.ThrowUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聚合控制器
 */
@Tag(name = "搜索管理", description = "聚合搜索")
@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchDTO dto) {

        return BaseResponse.success(searchFacade.searchAll(dto));

    }
}
