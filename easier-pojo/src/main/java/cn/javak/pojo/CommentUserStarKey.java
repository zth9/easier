package cn.javak.pojo;


import java.io.Serializable;

/**
 * comment_user_star
 * @author 
 */
public class CommentUserStarKey implements Serializable {
    private Integer userId;

    private Integer commentId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }
}