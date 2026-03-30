package com.miku.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miku.dto.PageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.entity.ArticleLike;
import com.miku.entity.Articles;
import com.miku.entity.ManticoreArticle;
import com.miku.entity.User;
import com.miku.mapper.manticore.SearchMapper;
import com.miku.mapper.mysql.ArticleLikeMapper;
import com.miku.mapper.mysql.ArticlesMapper;
import com.miku.mapper.mysql.UserMapper;
import com.miku.result.PageResult;
import com.miku.service.ArticlesService;
import com.miku.vo.ArticleDetailVO;
import com.miku.vo.ArticlesVO;
import com.miku.vo.UserArticleDetailVO;
import com.miku.vo.UserArticlesVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticlesServiceImpl implements ArticlesService {
    @Autowired
    private ArticlesMapper articlesMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private SearchMapper searchMapper;

    /**
     * 文章列表查询
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(PageQueryDTO pageQueryDTO) {
        // 计算偏移量
        int page = (pageQueryDTO.getPage() - 1) * pageQueryDTO.getPageSize();
        // 1. 查文章（带 LIMIT，只返回 pageSize 条）
        List<ArticlesVO> list = articlesMapper.selectArticlesList(page, pageQueryDTO.getPageSize());
        if (list.isEmpty()) {
            return new PageResult<>(0, Collections.emptyList());
        }
        // 2. 批量查用户
        Set<Long> userIds = list.stream()
                .map(ArticlesVO::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserArticlesVO> userMap = userMapper.selectBatchIds(userIds)
                .stream()
                .map(u -> UserArticlesVO.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .avatar(u.getAvatar())
                        .build())
                .collect(Collectors.toMap(UserArticlesVO::getId, u -> u));
        // 3. 组装用户到文章
        list.forEach(vo -> vo.setUser(userMap.get(vo.getUserId())));
        return new PageResult<>(-1, list);
    }

    /**
     *获取文章详细页
     * @param id
     * @return
     */
    @Override
    public ArticleDetailVO getArticleDetail(Long id) {
        // 1.获取文章详细页
        Articles articles = articlesMapper.selectOne(new LambdaQueryWrapper<Articles>()
                .select(Articles::getId, Articles::getUserId,
                        Articles::getTitle, Articles::getContent,
                        Articles::getViews, Articles::getComments,
                        Articles::getLikesCount,
                        Articles::getCreatedTime)
                .eq(Articles::getId, id));

        // 文章为空直接返回
        if (articles == null){
            return null;
        }

        // 2.获取用户信息
        Long uid = articles.getUserId();
        User user = userMapper.selectById(uid);

        // 数据拷贝,拼装
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        UserArticleDetailVO userArticleDetailVO = new UserArticleDetailVO();
        BeanUtils.copyProperties(articles,articleDetailVO);//文章信息
        BeanUtils.copyProperties(user,userArticleDetailVO);//用户信息
        articleDetailVO.setUser(userArticleDetailVO);
        return articleDetailVO;
    }

    /**
     * 发布文章
     * @param uid
     */
    @Override
    public void createArtics(Long uid, CreateArticlesDTO createArticlesDTO) {
        Articles articles = new Articles();
        LocalDateTime now = LocalDateTime.now();
        //对象拷贝
        BeanUtils.copyProperties(createArticlesDTO,articles);
        //设置发布和修改时间
        articles.setUserId(uid);
        articles.setCreatedTime(now);
        articles.setUpdatedTime(now);
        articlesMapper.insert(articles);

        // 为 Manticore 创建专用对象，转换时间为时间戳
        ManticoreArticle manticoreArticle = new ManticoreArticle();
        BeanUtils.copyProperties(articles, manticoreArticle);
        manticoreArticle.setImages("");
        manticoreArticle.setViews(0);
        manticoreArticle.setComments(0);
        manticoreArticle.setLikesCount(0);
        manticoreArticle.setStatus(1);
        manticoreArticle.setCreatedTime(now.toEpochSecond(ZoneOffset.ofHours(8)));
        manticoreArticle.setUpdatedTime(now.toEpochSecond(ZoneOffset.ofHours(8)));
        searchMapper.insert(manticoreArticle);
    }

    /**
     * 获取点赞信息
     * @param userId
     * @param articleId
     * @return
     */
    @Override
    public boolean getLikeStatus(Long userId, Long articleId) {
        LambdaQueryWrapper<ArticleLike> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleLike::getArticleId, articleId)
                .eq(ArticleLike::getUserId, userId);
        return this.articleLikeMapper.exists(wrapper);
    }

    /**
     * 点赞
     * @param userId
     * @param articleId
     */
    @Override
    public void toggleLike(Long userId, Long articleId) {
        try {
            // 点赞
            ArticleLike articleLike = new ArticleLike().builder()
                    .userId(userId)
                    .articleId(articleId)
                    .createdTime(LocalDateTime.now())
                    .build();
            articleLikeMapper.insert(articleLike);
            articlesMapper.update(null, new UpdateWrapper<Articles>()
                    .setSql("likes_count = likes_count + 1")
                    .eq("id", articleId));
        } catch (DuplicateKeyException e) {
            // 取消点赞
            articleLikeMapper.delete(new LambdaQueryWrapper<ArticleLike>()
                    .eq(ArticleLike::getArticleId, articleId)
                    .eq(ArticleLike::getUserId, userId));
            articlesMapper.update(null, new UpdateWrapper<Articles>()
                    .setSql("likes_count = likes_count - 1")
                    .eq("id", articleId));
        }
    }
}
