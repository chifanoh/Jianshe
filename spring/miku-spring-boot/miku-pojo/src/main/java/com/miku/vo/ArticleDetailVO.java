package com.miku.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVO implements Serializable {
    private Long id;
    private UserArticleDetailVO user;
    private String title;
    private String content;
    private Integer views;
    private Integer comments;
    private Integer likes;
    private Integer favorite;
    private List<String> images;
    private List<TopicVO> topics;
    private LocalDateTime createdTime;
}
