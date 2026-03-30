package com.miku.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlesVO implements Serializable {
    private Long id;
    private Long userId;
    private UserArticlesVO user;
    private String title;
    private String summary;
    private Integer comments;
    private Integer likesCount;
    private List<String> images;
    private LocalDateTime createdTime;
}
