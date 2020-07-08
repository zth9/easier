package cn.javak.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * todo
 * @author 
 */
public class Todo implements Serializable {
    private Integer todoId;

    private Integer userId;

    private Date todoTime;

    @JsonIgnore
    private String todoTimeStr;

    private String todoContent;

    /**
     * 0 正在路上 1 已阵亡
     */
    private String todoStatus;

    /**
     * 0 仅一次 1 每天同一时间
     */
    private String todoType;

    private static final long serialVersionUID = 1L;

    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTodoTime() {
        return todoTime;
    }

    public void setTodoTime(Date todoTime) {
        this.todoTime = todoTime;
    }

    public String getTodoTimeStr() {
        return todoTimeStr;
    }

    public void setTodoTimeStr(String todoTimeStr) {
        this.todoTimeStr = todoTimeStr;
    }

    public String getTodoContent() {
        return todoContent;
    }

    public void setTodoContent(String todoContent) {
        this.todoContent = todoContent;
    }

    public String getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(String todoStatus) {
        this.todoStatus = todoStatus;
    }

    public String getTodoType() {
        return todoType;
    }

    public void setTodoType(String todoType) {
        this.todoType = todoType;
    }
}