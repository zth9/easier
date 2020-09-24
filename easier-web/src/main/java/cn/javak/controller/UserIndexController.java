package cn.javak.controller;

import cn.javak.pojo.Blog;
import cn.javak.pojo.User;
import cn.javak.service.BlogService;
import cn.javak.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: theTian
 * @date: 2020/6/24 10:46
 */
@Controller
@RequestMapping("/user/index")
public class UserIndexController {
    @Reference
    private UserService userService;
    @Reference
    private BlogService blogService;

    private static Logger logger = LoggerFactory.getLogger(UserIndexController.class);

    @GetMapping("{userId}")
    public String goIndex(User user, Model model){
        User selUser = userService.selectByUserId(user.getUserId());
        model.addAttribute("user", selUser);
        List<Blog> blogList = blogService.selectByUser(user);
        model.addAttribute("blogList", blogList);
        return "user";
    }
}
