package cn.javak.pojo;

import java.io.Serializable;

/**
 * blog_tag
 * @author 
 */
public class BlogTagKey  implements Serializable {
    private Integer blogId;

    private Integer tagId;

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}