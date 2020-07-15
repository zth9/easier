package cn.javak.pojo;


import java.io.Serializable;

/**
 * tgroup
 * @author 
 */
public class Tgroup implements Serializable {
    private Integer tgroupId;

    private String tgroupName;

    public Integer getTgroupId() {
        return tgroupId;
    }

    public void setTgroupId(Integer tgroupId) {
        this.tgroupId = tgroupId;
    }

    public String getTgroupName() {
        return tgroupName;
    }

    public void setTgroupName(String tgroupName) {
        this.tgroupName = tgroupName;
    }
}