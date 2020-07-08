package cn.javak.mapper;

import cn.javak.pojo.Todo;
import cn.javak.utils.Constants;

import java.util.Date;
import java.util.List;

public interface TodoMapper {
    int deleteByPrimaryKey(Integer todoId);

    int insert(Todo record);

    int insertSelective(Todo record);

    Todo selectByPrimaryKey(Integer todoId);

    //通过用户id获取todo 按提醒时间升序排序
    List<Todo> selectByUserId(Integer userId);

    int updateByPrimaryKeySelective(Todo record);

    int updateByPrimaryKey(Todo record);

    List<Todo> selectAllNotRemind(String datetime);
}