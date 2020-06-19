package cn.javak.service;

import cn.javak.mapper.*;
import cn.javak.pojo.Blog;
import cn.javak.pojo.BlogTagKey;
import cn.javak.pojo.Comment;
import cn.javak.pojo.User;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: theTian
 * @date: 2020/6/2 0:41
 */
@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private CommentMapper commentMapper;

    public void save(Blog blog) {
        blogMapper.insert(blog);
    }

    /**
     * 默认按照时间从大到小顺序 不查询content 用于首页显示
     * @return
     */
    public List<Blog> select(Integer rule) {
        List<Blog> blogList = null;
        if (rule == 1){
            blogList = blogMapper.select();
        }else if (rule == 2){
            blogList = blogMapper.selectHot();
        }

        List<User> users = userMapper.selectOnlyInfo();

        //业务装配 效率低 后期使用关联查询
        for (Blog blog : blogList) {
            //装配用户信息
            for (User user : users) {
                if (blog.getUserId() == user.getUserId()){
                    blog.setHeadPic(user.getHeadPic());
                    blog.setNickName(user.getNickName());
                    break;
                }
            }
            //装配标签信息
            List<BlogTagKey> blogTagKeyList = blogTagMapper.selectByBlogId(blog.getBlogId());
            if (CollectionUtils.isEmpty(blogTagKeyList)){
                continue;
            }else {
                //todo 目前只有置顶功能 后期增设不同类型文章标签功能
                blog.setTagId(1);
            }
        }
        return blogList;
    }

    /**
     * 单击查看博客
     * @param id blogId
     * @return
     */
    public Map<String, Object> clickBlog(int id) {
        Blog blog = blogMapper.selectByPrimaryKey(id);
        if (blog == null){
            return null;
        }
        //blog点击量+1
        blog.setClickNum(blog.getClickNum() + 1);
        blogMapper.updateByPrimaryKeySelective(blog);

        //查询出作者信息
        User user = userMapper.selectByPrimaryKey(blog.getUserId());

        //查出评论列表 按时间降序
        List<Comment> commentList = commentMapper.selectByBlogId(blog.getBlogId());
        for (Comment comment : commentList) {
            //todo 后期优化sql(关联查询) --> 使用redis存储用户信息
            User curUser = userMapper.selectByPrimaryKey(comment.getUserId());
            comment.setHeadPic(curUser.getHeadPic());
            comment.setNickName(curUser.getNickName());
        }

        Map<String, Object> resMap = new HashMap<>();
        resMap.put("blog", blog);
        resMap.put("user", user);
        resMap.put("commentList", commentList);
        return resMap;
    }

    /**
     * 置顶/取消置顶 置顶tagId = 1
     * @param blog
     * @param isTopping true表示置顶操作 false表示取消置顶操作
     * @return
     */
    public boolean topping(Blog blog, boolean isTopping) {
        //先查询是否已经置顶
        List<BlogTagKey> blogTagKeyList = blogTagMapper.selectByBlogId(blog.getBlogId());
        if (isTopping){
            //欲置顶
            for (BlogTagKey blogTagKey : blogTagKeyList) {
                if (blogTagKey.getTagId() == 1){
                    //存在置顶标签 置顶失败
                    return false;
                }
            }
            BlogTagKey blogTagKey = new BlogTagKey();
            blogTagKey.setBlogId(blog.getBlogId());
            blogTagKey.setTagId(1);//置顶标签的id
            blogTagMapper.insert(blogTagKey);
            //置顶成功
            return true;
        }else {
            //欲取消置顶
            for (BlogTagKey blogTagKey : blogTagKeyList) {
                if (blogTagKey.getTagId() == 1){
                    //存在置顶标签 可取消置顶
                    BlogTagKey delBlogTagKey = new BlogTagKey();
                    delBlogTagKey.setBlogId(blog.getBlogId());
                    delBlogTagKey.setTagId(1);//置顶标签的id
                    blogTagMapper.deleteByPrimaryKey(delBlogTagKey);
                    //取消置顶成功
                    return true;
                }
            }
            //不存在置顶标签 取消置顶失败
            return false;
        }
    }

    /**
     * 获取置顶文章
     * @return
     */
    public List<Blog> selectTopping(){
        return blogMapper.selectTopping();
    }

    /**
     * 获取用户所有文章
     * //todo 分页
     * @param user
     * @return
     */
    public List<Blog> selectByUser(User user){
        return blogMapper.selectByUserId(user.getUserId());
    }

    public void delete(Integer tokenUserId, Integer id) throws Exception {
        Blog blog = blogMapper.selectByPrimaryKey(id);
        if (!tokenUserId.equals(blog.getUserId())){
            throw new RuntimeException("不是你写的,咋都没法删");
        }
        //进行删除
        //删除博客标签关联
        BlogTagKey blogTagKey = new BlogTagKey();
        blogTagKey.setBlogId(id);
        blogTagMapper.deleteByBlogId(id);
        //删除评论
        commentMapper.deleteByBlogId(id);
        //删除博客
        blogMapper.deleteByPrimaryKey(id);
    }

    public Blog selectByBlogId(Integer id) {
        return blogMapper.selectByPrimaryKey(id);
    }

    public void update(Blog blog) {
        blogMapper.updateByPrimaryKeySelective(blog);
    }
}
