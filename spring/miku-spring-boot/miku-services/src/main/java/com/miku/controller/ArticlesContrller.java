package com.miku.controller;

import com.miku.context.BaseContext;
import com.miku.dto.PageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.result.PageResult;
import com.miku.result.Result;
import com.miku.service.ArticlesService;
import com.miku.vo.ArticleDetailVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
@Slf4j
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
    public Result<ArticleDetailVO> getArticleDetail(@PathVariable Long id){
        log.info("文章详细页查询结果:{}",id);
        Long userId = BaseContext.getCurrentId();
        ArticleDetailVO articleDetailVO = articlesService.getArticleDetail(id, userId);
        //没有该文章
        if (articleDetailVO == null){
            return Result.error("啊哦,没有找到该文章!!!");
        }
        return Result.success(articleDetailVO);
    }

    /**
     * 发布文章
     * @param createArticlesDTO
     */
    @PostMapping("/create")
    public Result createArtics(@Valid @RequestBody CreateArticlesDTO createArticlesDTO){
        log.info("用户发布的文章:{}", createArticlesDTO);
        Long uid = BaseContext.getCurrentId();
        //3.存储文章数据
        articlesService.createArtics(uid, createArticlesDTO);
        return Result.success();
    }

    /**
     * 获取点赞信息
     * @param articleId
     * @return
     */
    @PostMapping("/getLikeStatus")
    public Result<Boolean> getLikeStatus(@RequestParam Long articleId){
        Long userId = BaseContext.getCurrentId();
        log.info("用户:{}获取文章:{}点赞信息",userId, articleId);
        boolean liked = articlesService.getLikeStatus(userId, articleId);
        return Result.success(liked);
    }

    /**
     * 点赞
     * @param articleId
     */
    @PostMapping("/like/{articleId}")
    public Result toggleLike(@PathVariable Long articleId){
        Long userId = BaseContext.getCurrentId();
        log.info("用户:{}触发点赞文章按钮:{}",userId, articleId);
        articlesService.toggleLike(userId, articleId);
        return Result.success();
    }
}
