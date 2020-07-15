package cn.javak.pojo;

import java.io.Serializable;

/**
 * user_blog_cs
 * @author 
 */
public class UserBlogCsKey  implements Serializable {
    private Integer userId;

    private Integer blogId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }
}