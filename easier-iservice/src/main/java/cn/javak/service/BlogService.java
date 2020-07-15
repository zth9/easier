package cn.javak.service;

import cn.javak.pojo.Blog;
import cn.javak.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @author: theTian
 * @date: 2020/7/13 14:20
 */
public interface BlogService {
    void save(Blog blog);

    List<Blog> select(Integer rule);

    Map<String, Object> clickBlog(int id);

    boolean topping(Blog blog, boolean isTopping);

    List<Blog> selectTopping();

    List<Blog> selectByUser(User user);

    void delete(Integer tokenUserId, Integer id) throws Exception;

    Blog selectByBlogId(Integer id);

    void update(Blog blog);

    List<Blog> selectByKeyWord(String keyWord);
}
