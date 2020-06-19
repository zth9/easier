package cn.javak.mapper;

import cn.javak.pojo.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer UserId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer UserId);

    User selectByUsername(String username);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectOnlyInfo();

    User selectByEmail(String email);

    User selectByNN(String nickName);
}