package com.miku.service.Impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miku.context.BaseContext;
import com.miku.dto.CommentDTO;
import com.miku.dto.PageQueryDTO;
import com.miku.entity.Comment;
import com.miku.entity.User;
import com.miku.mapper.mysql.CommentMapper;
import com.miku.mapper.mysql.UserMapper;
import com.miku.result.PageResult;
import com.miku.service.CommentService;
import com.miku.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 提交评论
     * @param commentDTO
     * @return
     */
    @Override
    public CommentDTO putComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        //对象拷贝
        BeanUtils.copyProperties(commentDTO, comment);

        String userId = BaseContext.getCurrentId();

        //设置发布和修改时间
        comment.setUserId(userId);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        commentMapper.insert(comment);

        //插入评论主键id
        commentDTO.setId(comment.getId());
        return commentDTO;

    }

    /**
     * 获取评论列表
     * @param articleId
     * @return
     */
    @Override
    public PageResult getCommentList(Long articleId, PageQueryDTO pageQueryDTO) {
        // 设置分页参数
        Page<Comment> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());

        // 查一级评论
        Page<Comment> pageResult = commentMapper.selectPage(page,
                Wrappers.<Comment>lambdaQuery()
                        .eq(Comment::getArticleId, articleId)
                        .orderByDesc(Comment::getCreateTime));
        // 取评论
        List<Comment> records = pageResult.getRecords();

        // 2. 批量用户 Map
        Set<String> userIds = records.stream().map(Comment::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userIds.isEmpty() ? Map.of()
                : userMapper.selectList(Wrappers.<User>lambdaQuery().in(User::getId, userIds))
                .stream().collect(Collectors.toMap(User::getId, u -> u));

        // 3. 一级 vs 二级分组
        Map<Long, List<Comment>> childrenMap = records.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(Comment::getParentId));

        // 4. 只保留一级，挂二级
        List<CommentVO> commentVO = records.stream()
                .filter(c -> c.getParentId() == null)
                .map(r -> buildVO(r, userMap, childrenMap))
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), commentVO);
    }

    private CommentVO buildVO(Comment c, Map<Long, User> userMap, Map<Long, List<Comment>> childrenMap) {
        CommentVO v = new CommentVO();
        BeanUtils.copyProperties(c, v);
        User u = userMap.getOrDefault(c.getUserId(), null);
        v.setUserName(u == null ? "" : u.getName());
        v.setUserAvatar(u == null ? "" : u.getAvatar());

        List<Comment> children = childrenMap.getOrDefault(c.getId(), List.of());
        v.setReply(new CommentVO.Reply(children.size(), children.stream()
                .map(child -> buildVO(child, userMap, Collections.emptyMap())) // 二级不再嵌套
                .collect(Collectors.toList())));
        return v;
    }

}
