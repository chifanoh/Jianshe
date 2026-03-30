package com.miku.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.util.*;
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

        Long userId = BaseContext.getCurrentId();

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
        Set<Long> userIds = records.stream().map(Comment::getUserId).collect(Collectors.toSet());
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
        
        // 只加载前5条二级评论，剩下的通过getCommentReplies分页获取
        List<Comment> limitedChildren = children.stream()
                .sorted((c1, c2) -> c2.getCreateTime().compareTo(c1.getCreateTime())) // 按时间降序
                .limit(5)
                .collect(Collectors.toList());
                
        v.setReply(new CommentVO.Reply(children.size(), limitedChildren.stream()
                .map(child -> buildVO(child, userMap, Collections.emptyMap())) // 二级不再嵌套
                .collect(Collectors.toList())));
        return v;
    }

    /**
     * 获取评论分页
     * @param parentId
     * @param pageQueryDTO
     * @return
     */
    @Override
    public PageResult getCommentReplies(Long parentId, PageQueryDTO pageQueryDTO) {
        // 手动实现分页逻辑，避免MyBatis Plus分页插件的问题
        int page = pageQueryDTO.getPage();
        int pageSize = pageQueryDTO.getPageSize();
        // 计算偏移量：第1页偏移0，第2页偏移5，第3页偏移10
        int offset = (page - 1) * pageSize;
        // 1. 先查询总数
        long total = commentMapper.selectCount(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getParentId, parentId));
        // 2. 手动分页查询数据
        List<Comment> records = commentMapper.selectList(
                Wrappers.<Comment>lambdaQuery()
                        .eq(Comment::getParentId, parentId)
                        .orderByDesc(Comment::getCreateTime)
                        .last("LIMIT " + offset + ", " + pageSize)
        );
        // 3. 批量查询用户信息
        Set<Long> userIds = records.stream().map(Comment::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userIds.isEmpty() ? Map.of()
                : userMapper.selectList(Wrappers.<User>lambdaQuery().in(User::getId, userIds))
                .stream().collect(Collectors.toMap(User::getId, u -> u));
        // 4. 构建CommentVO列表
        List<CommentVO> commentVOList = records.stream()
                .map(comment -> {
                    CommentVO vo = new CommentVO();
                    BeanUtils.copyProperties(comment, vo);
                    User user = userMap.getOrDefault(comment.getUserId(), null);
                    vo.setUserName(user == null ? "" : user.getName());
                    vo.setUserAvatar(user == null ? "" : user.getAvatar());
                    // 回复评论不再嵌套子评论
                    vo.setReply(new CommentVO.Reply(0, Collections.emptyList()));
                    return vo;
                })
                .collect(Collectors.toList());
        return new PageResult<>(total, commentVOList);
    }

    /**
     * 根据评论表主键id删除评论
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public boolean delectComment(Long id, Long userId) {
        // 当前操作用户是否是评论用户
        boolean exists = commentMapper.exists(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getUserId, userId));
        if (exists){
            // 删除
            commentMapper.deleteById(id);
            return true;
        }
        // 返回false删除失败
        return false;
    }
}