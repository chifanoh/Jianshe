package com.miku.service.Impl;

import com.miku.dto.SearchDTO;
import com.miku.mapper.search.SearchMapper;
import com.miku.result.PageResult;
import com.miku.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchMapper searchMapper;

    /**
     * 文章搜索
     * @param searchDTO
     * @return
     */
    @Override
    public PageResult search(SearchDTO searchDTO) {
        return null;
    }
}
