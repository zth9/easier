package cn.javak.mapper;

import cn.javak.pojo.RolePermissionKey;

public interface RolePermissionMapper {
    int deleteByPrimaryKey(RolePermissionKey key);

    int insert(RolePermissionKey record);

    int insertSelective(RolePermissionKey record);
}