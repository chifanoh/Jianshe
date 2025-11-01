package com.miku.controller;

import com.miku.context.BaseContext;
import com.miku.dto.ArticlesPageQueryDTO;
import com.miku.dto.CreateArticlesDTO;
import com.miku.result.PageResult;
import com.miku.result.Result;
import com.miku.service.ArticlesService;
import com.miku.vo.ArticleDetailVO;
import jakarta.servlet.http.HttpServletRequest;
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
     * 文章查询
     * @param articlesPageQueryDTO
     * @return
     */
    @GetMapping("/list")
    public Result<PageResult> getArticlesList(ArticlesPageQueryDTO articlesPageQueryDTO){
        log.info("文章分页查询结果:{}",articlesPageQueryDTO);
        PageResult pageResult = articlesService.pageQuery(articlesPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 获取文章详细页
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ArticleDetailVO> getArticleDetail(@PathVariable Integer id){
        log.info("文章分页查询结果:{}",id);
        ArticleDetailVO articleDetailVO = articlesService.getArticleDetail(id);
        return Result.success(articleDetailVO);
    }

    /**
     * 发布文章
     * @param createArticlesDTO
     */
    @PostMapping("/create")
    public Result createArtics(@RequestBody CreateArticlesDTO createArticlesDTO, HttpServletRequest request){
        log.info("用户发布的文章:{}", createArticlesDTO);

        //判断标题是否为空
        if (createArticlesDTO.getTitle() == null){
            return Result.error("内容为空");
        }

        Long uid = BaseContext.getCurrentId();
        //3.存储文章数据
        articlesService.createArtics(uid, createArticlesDTO);
        return Result.success();
    }
}
