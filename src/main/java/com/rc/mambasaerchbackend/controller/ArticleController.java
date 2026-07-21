package com.rc.mambasaerchbackend.controller;

import com.rc.mambasaerchbackend.annotation.AuthCheck;
import com.rc.mambasaerchbackend.common.BaseResponse;
import com.rc.mambasaerchbackend.model.dto.ArticleCreateDTO;
import com.rc.mambasaerchbackend.model.dto.ArticleQueryDTO;
import com.rc.mambasaerchbackend.model.dto.ArticleUpdateDTO;
import com.rc.mambasaerchbackend.model.vo.ArticleVO;
import com.rc.mambasaerchbackend.search.ElasticsearchService;
import com.rc.mambasaerchbackend.search.SearchDoc;
import com.rc.mambasaerchbackend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 文章控制器
 */
@Tag(name = "文章管理", description = "文章增删改查")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    private final ElasticsearchService elasticsearchService;

    @Operation(summary = "创建文章")
    @PostMapping
    @AuthCheck
    public BaseResponse<Long> createArticle(@Valid @RequestBody ArticleCreateDTO dto) {
        Long id = articleService.createArticle(dto.getTitle(), dto.getContent(), dto.getTags());
//        SearchDoc searchDoc = new SearchDoc();
//        searchDoc.setTitle(dto.getTitle());
//        searchDoc.setContent(dto.getContent());
//        searchDoc.setType("article");
//        searchDoc.setUserId("5");
//        searchDoc.setCreateTime(LocalDateTime.now());
//        searchDoc.setUpdateTime(LocalDateTime.now());
//        searchDoc.setIsDeleted("0");
//        elasticsearchService.saveOrUpdate(searchDoc);
        return BaseResponse.success(id);
    }

    @Operation(summary = "根据 ID 获取文章")
    @GetMapping("/{id}")
    public BaseResponse<ArticleVO> getArticle(@PathVariable Long id) {
        return BaseResponse.success(articleService.getArticleById(id));
    }

    @Operation(summary = "更新文章")
    @PutMapping("/{id}")
    @AuthCheck
    public BaseResponse<Void> updateArticle(@PathVariable Long id,
                                            @Valid @RequestBody ArticleUpdateDTO dto) {
        articleService.updateArticle(id, dto.getTitle(), dto.getContent(), dto.getTags());
        return BaseResponse.success();
    }

    @Operation(summary = "删除文章")
    @DeleteMapping("/{id}")
    @AuthCheck
    public BaseResponse<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return BaseResponse.success();
    }

    @Operation(summary = "条件检索文章列表")
    @GetMapping("/search")
    public BaseResponse<List<ArticleVO>> searchArticles(ArticleQueryDTO query) {
        String title = query != null ? query.getTitle() : null;
        String tags = query != null ? query.getTags() : null;
        Long userId = query != null ? query.getUserId() : null;
        return BaseResponse.success(articleService.searchArticles(title, tags, userId));
    }
}
