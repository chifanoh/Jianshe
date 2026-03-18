package com.miku.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManticoreArticleVO {
    private Long id;
    private UserArticlesVO user;
    private String title;
    private String content;
    private Integer comments;
    private Integer likes;
    private List<String> images;
    private LocalDateTime createdTime;
}
