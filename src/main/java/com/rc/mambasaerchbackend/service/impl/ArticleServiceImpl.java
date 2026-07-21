package com.rc.mambasaerchbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rc.mambasaerchbackend.common.BusinessException;
import com.rc.mambasaerchbackend.common.ErrorCode;
import com.rc.mambasaerchbackend.mapper.ArticleMapper;
import com.rc.mambasaerchbackend.model.entity.Article;
import com.rc.mambasaerchbackend.model.vo.ArticleVO;
import com.rc.mambasaerchbackend.service.ArticleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;
    private final HttpServletRequest request;

    public ArticleServiceImpl(ArticleMapper articleMapper, HttpServletRequest request) {
        this.articleMapper = articleMapper;
        this.request = request;
    }

    @Override
    public Long createArticle(String title, String content, String tags) {
        Long userId = getLoginUserId();

        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setTags(tags);
        article.setUserId(userId);

        articleMapper.insert(article);
        return article.getId();
    }

    @Override
    public ArticleVO getArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }
        return toVO(article);
    }

    @Override
    public void updateArticle(Long id, String title, String content, String tags) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }

        Long userId = getLoginUserId();
        if (!userId.equals(article.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能编辑自己的文章");
        }

        Article update = new Article();
        update.setId(id);
        if (StringUtils.hasText(title)) {
            update.setTitle(title);
        }
        if (StringUtils.hasText(content)) {
            update.setContent(content);
        }
        if (StringUtils.hasText(tags)) {
            update.setTags(tags);
        }
        articleMapper.updateById(update);
    }

    @Override
    public void deleteArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "文章不存在");
        }

        Long userId = getLoginUserId();
        if (!userId.equals(article.getUserId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "只能删除自己的文章");
        }

        articleMapper.deleteById(id);
    }

    @Override
    public List<ArticleVO> searchArticles(String title, String tags, Long userId) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(title), Article::getTitle, title)
               .like(StringUtils.hasText(tags), Article::getTags, tags)
               .eq(userId != null, Article::getUserId, userId)
               .orderByDesc(Article::getCreatedTime);

        List<Article> articles = articleMapper.selectList(wrapper);
        return articles.stream().map(this::toVO).toList();
    }

    // ---------- 工具方法 ----------

    private Long getLoginUserId() {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        return userId;
    }

    private ArticleVO toVO(Article article) {
        if (article == null) return null;
        ArticleVO vo = new ArticleVO();
        BeanUtils.copyProperties(article, vo);
        return vo;
    }
}
