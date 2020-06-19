package cn.javak.controller;

import cn.javak.annotation.UserLoginToken;
import cn.javak.pojo.Comment;
import cn.javak.pojo.RespBean;
import cn.javak.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: theTian
 * @date: 2020/6/13 23:57
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 新增评论
     * @param comment
     * @return
     */
    @PostMapping
    @UserLoginToken
    public RespBean save(Comment comment){
        try {
            //新增评论
            comment.setCreateTime(new Date());
            comment.setStarNum(0);
            commentService.insert(comment);
            return RespBean.ok("评论成功");
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }
    }
}
