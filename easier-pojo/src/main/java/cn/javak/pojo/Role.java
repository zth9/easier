package cn.javak.pojo;

import java.io.Serializable;

/**
 * role
 * @author 
 */
public class Role implements Serializable {
    private Integer roleId;

    private String roleName;

    private String desc;

    private static final long serialVersionUID = 1L;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}