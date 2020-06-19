package cn.javak.mapper;

import cn.javak.pojo.Tgroup;

public interface TgroupMapper {
    int deleteByPrimaryKey(Integer tgroupId);

    int insert(Tgroup record);

    int insertSelective(Tgroup record);

    Tgroup selectByPrimaryKey(Integer tgroupId);

    int updateByPrimaryKeySelective(Tgroup record);

    int updateByPrimaryKey(Tgroup record);
}