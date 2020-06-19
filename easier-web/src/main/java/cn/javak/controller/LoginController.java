package cn.javak.controller;

import cn.javak.pojo.RespBean;
import cn.javak.pojo.User;
import cn.javak.service.TokenService;
import cn.javak.service.UserService;
import cn.javak.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * @author: theTian
 * @date: 2020/5/31 15:10
 */
@RestController
@RequestMapping("/")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    /**
     * 登录系统
     * todo 使用apache shiro做权限控制
     *
     * @param user
     * @return
     * @throws InterruptedException
     */
    @PostMapping("login")
    public RespBean login(User user, HttpServletResponse response) throws InterruptedException {
        //todo 表单验证
        System.out.println("用户["+user.getUsername()+"]开始登陆");

        User resUser = userService.login(user);
        if (resUser == null || !resUser.getPassword().equals(user.getPassword())) {
            return RespBean.error("用户名或密码错误");
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("user", resUser);
        String token = tokenService.getToken(resUser);
        resMap.put("token", token);

        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);

        return RespBean.ok("登陆成功", resMap);
    }
}
