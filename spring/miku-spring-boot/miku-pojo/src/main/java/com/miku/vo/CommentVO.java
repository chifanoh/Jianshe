package com.miku.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private String userName;

    //用户头像
    private String userAvatar;

    //父评论ID.用于前端判断评论层级关系
    private Long parentId;

    //评论内容
    private String content;

    //评论时间
    private LocalDateTime createTime;

    //二级评论
    private Reply reply;

    @Data
    @AllArgsConstructor
    public static class Reply {
        private Integer total;
        private List<CommentVO> list;
    }
}
