package com.miku.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticlesDTO implements Serializable {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 文章标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(message = "标题最长不能超过{max}", max = 30)
    private String title;

    /**
     * 文章正文
     */
    private String content;

    /**
     * 文章图片
     */
    private String images;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

}
