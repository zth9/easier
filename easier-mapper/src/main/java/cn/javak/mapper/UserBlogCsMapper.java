package cn.javak.mapper;

import cn.javak.pojo.UserBlogCs;
import cn.javak.pojo.UserBlogCsKey;

public interface UserBlogCsMapper {
    int deleteByPrimaryKey(UserBlogCsKey key);

    int insert(UserBlogCs record);

    int insertSelective(UserBlogCs record);

    UserBlogCs selectByPrimaryKey(UserBlogCsKey key);

    int updateByPrimaryKeySelective(UserBlogCs record);

    int updateByPrimaryKey(UserBlogCs record);
}