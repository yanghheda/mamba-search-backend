package com.rc.mambasaerchbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rc.mambasaerchbackend.model.entity.Article;
import com.rc.mambasaerchbackend.model.entity.User;
import com.rc.mambasaerchbackend.model.vo.ArticleVO;

import java.util.List;

/**
 * 文章服务
 */
public interface ArticleService extends IService<Article> {

    /**
     * 创建文章
     */
    Long createArticle(String title, String content, String tags);

    /**
     * 根据 ID 获取文章
     */
    ArticleVO getArticleById(Long id);

    /**
     * 更新文章
     */
    void updateArticle(Long id, String title, String content, String tags);

    /**
     * 删除文章（逻辑删除）
     */
    void deleteArticle(Long id);

    /**
     * 条件检索文章列表
     */
    List<ArticleVO> searchArticles(String title, String tags, Long userId);
}
