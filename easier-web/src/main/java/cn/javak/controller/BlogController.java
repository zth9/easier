package cn.javak.controller;

import cn.javak.annotation.UserLoginToken;
import cn.javak.pojo.Blog;
import cn.javak.pojo.RespBean;
import cn.javak.pojo.User;
import cn.javak.service.BlogService;
import cn.javak.service.CommentService;
import cn.javak.service.TokenService;
import cn.javak.service.UserService;
import cn.javak.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: theTian
 * @date: 2020/6/2 0:40
 */
@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    /**
     * 发布博客
     * @param blog
     * @return
     * @throws InterruptedException
     */
    @PostMapping()
    @UserLoginToken
    public RespBean save(Blog blog) throws InterruptedException {
        if (blogService.selectByBlogId(blog.getBlogId())!=null){
            blogService.update(blog);
            return RespBean.ok("修改成功");
        }
        Date curDate = new Date();
        blog.setCreateTime(curDate);
        blog.setStarNum(0);
        blog.setCollectionNum(0);
        blog.setClickNum(0);
        blog.setCommentNum(0);

        //添加博客
        try {
            blogService.save(blog);
            return RespBean.ok("发布成功");
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }
    }

    /**
     * 主页加载blog
     * todo 增加分页 增加查询条件以按照热度 时间 综合进行排序
     * @param user
     * @param rule 1 按时间从大到小 2 按点赞从多到少
     * @return
     * @throws InterruptedException
     */
    @GetMapping
    public RespBean load(User user, Integer rule) throws InterruptedException {
        //查询blog
        try {
            List<Blog> select = blogService.select(rule);
            return RespBean.ok("查询成功", select);
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }
    }

    /**
     * 单文章页加载
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public RespBean load(@PathVariable("id") int id){
        try {
            Map<String, Object> resMap = blogService.clickBlog(id);
            if (resMap!=null){
                return RespBean.ok("查询到博客内容",resMap);
            }else {
                return RespBean.error("未查询到");
            }
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }
    }

    /**
     * 将某博客置顶
     * @param blog
     */
    @PutMapping(value = "/topping/{id}")
    @UserLoginToken
    public RespBean topping(Blog blog){
        try {
            boolean toppingSuccess = blogService.topping(blog, true);
            if (toppingSuccess){
                return RespBean.ok("置顶成功");
            }else {
                return RespBean.error("该文章已置顶");
            }
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }
    }

    /**
     * 将某博客取消置顶
     * @param blog
     */
    @PutMapping(value = "/unTopping/{id}")
    @UserLoginToken
    public RespBean unTopping(Blog blog){
        try {
            boolean toppingSuccess = blogService.topping(blog, false);
            if (toppingSuccess){
                return RespBean.ok("已取消置顶");
            }else {
                return RespBean.error("该文章未置顶");
            }
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }
    }

    /**
     * 查询出置顶文章
     * @return
     */
    @GetMapping(value = "/topping")
    public RespBean loadTopping(){
        try {
            List<Blog> toppingList = blogService.selectTopping();
            return RespBean.ok("",toppingList);
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }
    }

    /**
     * 根据用户id查询出他的所有 blog
     * @param user
     * @return
     */
    @GetMapping(value = "/mine")
    @UserLoginToken
    public RespBean loadUserBlog(User user){
        try {
            //参数验证
            if (user.getUserId() == null){
                return RespBean.error("未登录");
            }
            List<Blog> blogList = blogService.selectByUser(user);
            return RespBean.ok("", blogList);
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }
    }

    /**
     * 根据 用户id 和 博客id 删除博客
     * @param id 传过来博客id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    @UserLoginToken
    public RespBean deleteBlogByUserIdAndBlogId(@PathVariable("id") Integer id){
        //验证token的userId 和 博客id查询出的userId 是否一致
        Integer tokenUserId = TokenUtil.getTokenUserId();
        if (tokenUserId != null){
            try {
                blogService.delete(tokenUserId, id);
                return RespBean.ok("删除成功");
            }catch (Exception e){
                e.printStackTrace();
                return RespBean.error(e.getMessage());
            }
        }
        return RespBean.error("tokenError");
    }

    /**
     * 编辑博客时返回该博客信息
     * @param id
     * @return
     */
    @GetMapping("edit/{id}")
    @UserLoginToken
    public RespBean editBlog(@PathVariable("id")Integer id){
        //验证blogid是否属于该用户 用该用户id查询该blog null表示非该用户
        Integer userId = TokenUtil.getTokenUserId();
        Blog selBlog = blogService.selectByBlogId(id);
        if (!selBlog.getUserId().equals(userId)){
            return RespBean.error("reject");
        }

        return RespBean.ok("可编辑", selBlog);
    }
}