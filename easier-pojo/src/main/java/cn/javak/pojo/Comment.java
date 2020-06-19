package cn.javak.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * comment
 * @author 
 */
public class Comment {
    private Integer commentId; // 评论id

    private Integer blogId; // 博客id

    private Integer userId; // 发送者用户id

    private String content; // 评论内容

    private Date createTime; // 评论时间

    private Integer starNum; // 评论点赞数

    //外键
    private String nickName; // 用户名

    private String headPic; // 用户头像

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStarNum() {
        return starNum;
    }

    public void setStarNum(Integer starNum) {
        this.starNum = starNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }
}