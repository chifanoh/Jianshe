package com.miku.controller;

import com.miku.dto.SearchDTO;
import com.miku.result.PageResult;
import com.miku.result.Result;
import com.miku.service.SearchService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/article")
@Slf4j
public class SearchContrller {

    @Autowired
    private SearchService searchService;

    /**
     * 文章搜索
     * @param searchDTO
     * @return
     */
    @GetMapping("/search")
    public Result<PageResult> search(@Valid SearchDTO searchDTO) {
        PageResult result = searchService.search(searchDTO);
        return Result.success(result);
    }
}
