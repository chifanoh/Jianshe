package com.miku.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miku.dto.PageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.entity.Articles;
import com.miku.entity.User;
import com.miku.mapper.ArticlesMapper;
import com.miku.mapper.UserMapper;
import com.miku.result.PageResult;
import com.miku.service.ArticlesService;
import com.miku.vo.ArticleDetailVO;
import com.miku.vo.ArticlesVO;
import com.miku.vo.UserArticleDetailVO;
import com.miku.vo.UserArticlesVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 文章查询
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(PageQueryDTO pageQueryDTO) {
        // 设置分页参数
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
            vo.setLikes(po.getLikes());
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
    public ArticleDetailVO getArticleDetail(Integer id) {
        // 查询数据库
        // 1.获取文章详细页
        Articles articles = articlesMapper.selectById(id);

        // 文章为空直接返回
        if (articles == null){
            return null;
        }

        // 2.获取用户信息
        Long uid = articles.getUserId();
        User user = userMapper.selectById(uid);

        // 数据拷贝
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        UserArticleDetailVO userArticleDetailVO = new UserArticleDetailVO();
        BeanUtils.copyProperties(articles,articleDetailVO);
        BeanUtils.copyProperties(user,userArticleDetailVO);
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

}
