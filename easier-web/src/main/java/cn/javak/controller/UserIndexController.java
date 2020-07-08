package cn.javak.controller;

import cn.javak.pojo.Blog;
import cn.javak.pojo.User;
import cn.javak.service.BlogService;
import cn.javak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author: theTian
 * @date: 2020/6/24 10:46
 */
@Controller
@RequestMapping("/user/index")
public class UserIndexController {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;

    @GetMapping("{userId}")
    public String goIndex(User user, Model model){
        User selUser = userService.selectByUserId(user.getUserId());
        model.addAttribute("user", selUser);
        List<Blog> blogList = blogService.selectByUser(user);
        model.addAttribute("blogList", blogList);
        return "user";
    }
}
