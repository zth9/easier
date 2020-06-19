package cn.javak.mapper;

import cn.javak.pojo.CommentUserStarKey;

public interface CommentUserStarMapper {
    int deleteByPrimaryKey(CommentUserStarKey key);

    int insert(CommentUserStarKey record);

    int insertSelective(CommentUserStarKey record);
}