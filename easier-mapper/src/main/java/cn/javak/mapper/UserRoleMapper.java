package cn.javak.mapper;

import cn.javak.pojo.UserRoleKey;

import java.util.Set;

public interface UserRoleMapper {
    int deleteByPrimaryKey(UserRoleKey key);

    int insert(UserRoleKey record);

    int insertSelective(UserRoleKey record);

    Set<String> selectByUsername(String username);
}