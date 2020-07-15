package cn.javak.service;

import cn.javak.pojo.Comment;

import java.util.List;

/**
 * @author: theTian
 * @date: 2020/7/13 14:25
 */
public interface CommentService {
    void insert(Comment comment);

    List<Comment> selByBlogId(int blogId);
}
