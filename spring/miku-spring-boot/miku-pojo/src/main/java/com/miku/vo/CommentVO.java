package com.miku.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {

    //评论ID
    private Long id;

    //用户ID
    private Long userId;

    //用户名
    private String username;

    //用户头像
    private String userAvatar;

    //所属文章ID
    private Long articleId;

    //父评论ID.用于前端判断评论层级关系
    private Long parentId;

    //如果是回复评论,显示父评论用户的昵称
    private String parentUsername;

    //评论内容
    private String content;

    //评论时间
    private LocalDateTime createTime;

    //点赞数
    private Integer likes;

    //子评论列表,用于前端展示多级评论结构
    private List<CommentVO> replies;
}
