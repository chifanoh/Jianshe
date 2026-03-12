package com.miku.controller;

import com.miku.context.BaseContext;
import com.miku.dto.PageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.result.PageResult;
import com.miku.result.Result;
import com.miku.service.ArticlesService;
import com.miku.vo.ArticleDetailVO;
import com.miku.vo.LikeArticleVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
@Slf4j
@CrossOrigin(origins = {"http://localhost:5173"},allowCredentials = "true")
public class ArticlesContrller {

    @Autowired
    private ArticlesService articlesService;

    /**
     * 文章列表查询
     * @param pageQueryDTO
     * @return
     */
    @GetMapping("/list")
    public Result<PageResult> getArticlesList(PageQueryDTO pageQueryDTO){
        log.info("文章分页查询结果:{}", pageQueryDTO);
        PageResult pageResult = articlesService.pageQuery(pageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取文章详细页
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ArticleDetailVO> getArticleDetail(@PathVariable String id){
        log.info("文章详细页查询结果:{}",id);
        ArticleDetailVO articleDetailVO = articlesService.getArticleDetail(id);
        //没有该文章
        if (articleDetailVO == null){
            return Result.error("啊哦,没有找到该文章!!!");
        }
        return Result.success(articleDetailVO);
    }

    public Result getLikeStatus(@PathVariable Integer id){
        return Result.success();
    }

    /**
     * 发布文章
     * @param createArticlesDTO
     */
    @PostMapping("/create")
    public Result createArtics(@Valid @RequestBody CreateArticlesDTO createArticlesDTO){
        log.info("用户发布的文章:{}", createArticlesDTO);
        String uid = BaseContext.getCurrentId();
        //3.存储文章数据
        articlesService.createArtics(uid, createArticlesDTO);
        return Result.success();
    }

    /**
     * 点赞
     * @param id
     */
    @PostMapping("/like/{id}")
    public Result<LikeArticleVO> likeArticle(@PathVariable Integer id){
        String userId = BaseContext.getCurrentId();
        log.info("用户:{}点赞:{}",userId, id);
        return Result.success();
    }

    /**
     * 取消点赞
     * @param id
     */
    @PostMapping("/unlike/{id}")
    public Result<LikeArticleVO> unlikeArticle(@PathVariable Integer id){
        String userId = BaseContext.getCurrentId();
        log.info("用户:{}取消点赞:{}",userId, id);
        return Result.success();
    }
}
