package cn.javak.service;

import cn.javak.mapper.BlogMapper;
import cn.javak.mapper.CommentMapper;
import cn.javak.pojo.Blog;
import cn.javak.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: theTian
 * @date: 2020/6/13 23:57
 */
@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogMapper blogMapper;

    /**
     * 新增评论
     * @param comment
     */
    public void insert(Comment comment){
        //拆入评论
        commentMapper.insertSelective(comment);

        //更新博客评论数
        Blog blog = blogMapper.selectByPrimaryKey(comment.getBlogId());
        blog.setCommentNum(blog.getCommentNum() + 1);
        blogMapper.updateByPrimaryKeySelective(blog);
    }

    public List<Comment> selByBlogId(int blogId){
        return commentMapper.selectByBlogId(blogId);
    }
}
