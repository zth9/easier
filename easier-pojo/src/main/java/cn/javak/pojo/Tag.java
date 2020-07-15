package cn.javak.pojo;

import java.io.Serializable;

/**
 * tag
 * @author 
 */
public class Tag implements Serializable {
    private Integer tagId;

    private Integer tgroupId;

    private String tagName;


    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getTgroupId() {
        return tgroupId;
    }

    public void setTgroupId(Integer tgroupId) {
        this.tgroupId = tgroupId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}