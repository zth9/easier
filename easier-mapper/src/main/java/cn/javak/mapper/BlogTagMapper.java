package cn.javak.mapper;

import cn.javak.pojo.BlogTagKey;

import java.util.List;

public interface BlogTagMapper {
    int deleteByPrimaryKey(BlogTagKey key);

    int deleteByBlogId(Integer blogId);

    int insert(BlogTagKey record);

    int insertSelective(BlogTagKey record);

    List<BlogTagKey> selectByBlogId(Integer blogId);
}