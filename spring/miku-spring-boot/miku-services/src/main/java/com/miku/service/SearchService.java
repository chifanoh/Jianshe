package com.miku.service;

import com.miku.dto.SearchDTO;
import com.miku.result.PageResult;

public interface SearchService {
    /**
     * 文章搜索
     * @param searchDTO
     * @return
     */
    PageResult search(SearchDTO searchDTO);
}
