package com.miku.controller;

import com.miku.dto.SearchDTO;
import com.miku.result.PageResult;
import com.miku.result.Result;
import com.miku.service.SearchService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@Slf4j
public class SearchContrller {

    @Autowired
    private SearchService searchService;

    /**
     * 文章搜索
     * @param searchDTO
     * @return
     */
    @PostMapping()
    public Result<PageResult> search(@Valid SearchDTO searchDTO) {
        log.info("搜索内容{}",searchDTO);
        PageResult result = searchService.search(searchDTO);
        return Result.success(result);
    }
}
