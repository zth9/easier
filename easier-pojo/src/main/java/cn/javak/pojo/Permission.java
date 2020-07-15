package cn.javak.pojo;


import java.io.Serializable;

/**
 * permission
 * @author 
 */
public class Permission implements Serializable {
    private Integer permissionId;

    private String url;

    private String desc;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}