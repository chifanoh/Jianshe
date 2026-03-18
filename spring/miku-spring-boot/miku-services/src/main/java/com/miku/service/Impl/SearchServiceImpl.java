package com.miku.service.Impl;

import com.miku.dto.SearchDTO;
import com.miku.mapper.manticore.SearchMapper;
import com.miku.result.PageResult;
import com.miku.service.SearchService;
import com.miku.vo.ArticlesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // 1、查询数据
        List<ArticlesVO> articlesVO = searchMapper.searchArticles(
                searchDTO.getKeyword(),
                searchDTO.getPage(),
                searchDTO.getPageSize()
        );

        // 2、统计总数
        int total = searchMapper.countSearchArticles(searchDTO.getKeyword());
        return new PageResult(total, articlesVO);
    }
}
