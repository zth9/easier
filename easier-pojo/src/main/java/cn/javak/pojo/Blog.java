package cn.javak.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * blog
 * @author 
 */
public class Blog implements Serializable {
    private Integer blogId;

    private Integer userId;

    private String topic;

    private String content;

    private Integer starNum;

    private Integer collectionNum;

    private Integer clickNum;

    private Integer commentNum;

    private Date createTime;

    private boolean haveStar; // 是否点赞

    private boolean haveCollection; // 是否收藏

    private String headPic; // 用户头像

    private String nickName; // 用户昵称

    private Integer tagId; // 标签id 1为热们 其他为普通类型

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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStarNum() {
        return starNum;
    }

    public void setStarNum(Integer starNum) {
        this.starNum = starNum;
    }

    public Integer getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(Integer collectionNum) {
        this.collectionNum = collectionNum;
    }

    public Integer getClickNum() {
        return clickNum;
    }

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isHaveStar() {
        return haveStar;
    }

    public void setHaveStar(boolean haveStar) {
        this.haveStar = haveStar;
    }

    public boolean isHaveCollection() {
        return haveCollection;
    }

    public void setHaveCollection(boolean haveCollection) {
        this.haveCollection = haveCollection;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}