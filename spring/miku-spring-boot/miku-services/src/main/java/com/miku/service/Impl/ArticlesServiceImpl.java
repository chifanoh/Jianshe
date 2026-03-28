package com.miku.service.Impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miku.dto.PageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.entity.ArticleLike;
import com.miku.entity.Articles;
import com.miku.entity.User;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticlesServiceImpl implements ArticlesService {
    @Autowired
    private ArticlesMapper articlesMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 文章列表查询
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(PageQueryDTO pageQueryDTO) {
        // 设置分页参数
        pageQueryDTO.setPage((pageQueryDTO.getPage() - 1) * pageQueryDTO.getPageSize()); // 计算偏移值
        Page<Articles> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<Articles> queryWrapper = new LambdaQueryWrapper<>();

        //执行分页查询，查询Articles表
        Page<Articles> pageResult = articlesMapper.selectPage(page,queryWrapper);

        // 收集userid，分开查询后组装
        ArrayList<Long> userId = new ArrayList<>();
        for (Articles id : pageResult.getRecords()) {
            userId.add(id.getUserId());
        }
        // 查询user表
        List<User> userList = userMapper.selectBatchIds(userId);
        // 把用户转成 Map<Integer, User> 方便后面取
        Map<Long,User> userMap = new HashMap<>();
        for (User u : userList) {
            userMap.put(u.getId(), u);
        }

        // 遍历文章，组装VO
        ArrayList<ArticlesVO> articlesVO = new ArrayList<>();
        for (Articles po : pageResult.getRecords()) {
            ArticlesVO vo = new ArticlesVO();
            vo.setId(po.getId());
            vo.setTitle(po.getTitle());
            vo.setContent(po.getContent());
            vo.setComments(po.getComments());
            vo.setLikes(po.getLikesCount());
            vo.setCreatedTime(po.getCreatedTime());

            //取出用户
            User u = userMap.get(po.getUserId());
            //把用户转成UserArticlesVO
            UserArticlesVO userVO = UserArticlesVO.builder()
                    .id(u.getId())
                    .name(u.getName())
                    .avatar(u.getAvatar())
                    .build();
            vo.setUser(userVO);
            articlesVO.add(vo);
        }
        return new PageResult(pageResult.getTotal(),articlesVO);
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
        //对象拷贝
        BeanUtils.copyProperties(createArticlesDTO,articles);

        //设置发布和修改时间
        articles.setUserId(uid);
        articles.setCreatedTime(LocalDateTime.now());
        articles.setUpdatedTime(LocalDateTime.now());
        articlesMapper.insert(articles);
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
    @Transactional(rollbackFor = Exception.class)
    public void toggleLike(Long userId, Long articleId) {
        UpdateWrapper<Articles> updateWrapper = new UpdateWrapper<>();
        // 使用文章id作为key
        String key = "liked:articles:" + articleId;
        // 判断缓存是否命中
        if (stringRedisTemplate.hasKey(key)){
            // 缓存命中
            // 判断当前登录用户是否点赞
            Boolean isMember = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
            if (BooleanUtil.isFalse(isMember)) {
                // 未点赞数据库文章表点赞数 + 1
                updateWrapper.setSql("likes_count = likes_count + 1").eq("id", articleId);

                // 双写: 更新数据库点赞表用户点赞状态
                ArticleLike articleLike = new ArticleLike().builder()
                        .userId(userId)
                        .articleId(articleId)
                        .createdTime(LocalDateTime.now())
                        .build();
                articleLikeMapper.insert(articleLike);

                // 双写: 保存用户数据到Redis
                boolean isSuccess = articlesMapper.update(null, updateWrapper) > 0;
                if (isSuccess) {
                    stringRedisTemplate.opsForSet().add(key ,userId.toString());
                }

            }else {
                // 已点赞数据库文章表点赞数 - 1
                updateWrapper.setSql("likes_count = likes_count - 1").eq("id", articleId);

                // 双写: 更新数据库点赞表用户点赞状态
                articleLikeMapper.delete(new LambdaQueryWrapper<ArticleLike>()
                    .eq(ArticleLike::getArticleId, articleId)
                    .eq(ArticleLike::getUserId, userId));

                // 双写: 保存用户数据到Redis
                boolean isSuccess = articlesMapper.update(null, updateWrapper) > 0;
                if (isSuccess) {
                    stringRedisTemplate.opsForSet().remove(key ,userId.toString());
                }
            }

        }else {
            // 缓存未命中
            // 查数据库
            boolean exists = articleLikeMapper.exists(new LambdaQueryWrapper<ArticleLike>()
                            .eq(ArticleLike::getUserId, userId)
                            .eq(ArticleLike::getArticleId, articleId)
            );

            // 判断: 未点赞: 写Redis; 已点赞: 不写Redis
            if (exists) {
                // 更新数据库点赞状态
                updateWrapper.setSql("likes_count = likes_count - 1").eq("id", articleId);
                articleLikeMapper.delete(new LambdaQueryWrapper<ArticleLike>()
                        .eq(ArticleLike::getArticleId, articleId)
                        .eq(ArticleLike::getUserId, userId));
            }else {

                // 更新数据库点赞状态
                updateWrapper.setSql("likes_count = likes_count + 1").eq("id", articleId);
                ArticleLike articleLike = new ArticleLike().builder()
                        .userId(userId)
                        .articleId(articleId)
                        .createdTime(LocalDateTime.now())
                        .build();
                articleLikeMapper.insert(articleLike);
                // 重建缓存写Redis
                stringRedisTemplate.opsForSet().add(key ,userId.toString());
            }
        }
    }

}
