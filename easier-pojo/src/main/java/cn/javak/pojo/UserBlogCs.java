package cn.javak.pojo;

import java.io.Serializable;

/**
 * user_blog_cs
 * @author 
 */
public class UserBlogCs extends UserBlogCsKey {
    private Integer haveStar;

    private Integer haveCollection;

    public Integer getHaveStar() {
        return haveStar;
    }

    public void setHaveStar(Integer haveStar) {
        this.haveStar = haveStar;
    }

    public Integer getHaveCollection() {
        return haveCollection;
    }

    public void setHaveCollection(Integer haveCollection) {
        this.haveCollection = haveCollection;
    }
}