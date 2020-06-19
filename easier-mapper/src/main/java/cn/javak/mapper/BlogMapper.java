package cn.javak.mapper;

import cn.javak.pojo.Blog;

import java.util.List;

public interface BlogMapper {
    int deleteByPrimaryKey(Integer blogId);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Integer blogId);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKey(Blog record);

    /**
     * 首页查询置顶
     * @return
     */
    List<Blog> selectTopping();

    /**
     * 首页查询 暂时查询出前15条记录
     * @return
     */
    List<Blog> select();

    List<Blog> selectHot();

    List<Blog> selectByUserId(Integer userId);
}