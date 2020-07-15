package cn.javak.service;

import cn.javak.pojo.Todo;

import java.util.List;

/**
 * @author: theTian
 * @date: 2020/7/13 14:25
 */
public interface TodoService {
    List<Todo> selectByUserId(Integer userId);

    Todo selectByTodoId(Integer todoId);

    int save(Todo todo);

    int update(Todo todo);

    int delete(Integer todoId);

    void scheduleTodo();
}
