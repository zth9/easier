package cn.javak.controller;

import cn.javak.pojo.RespBean;
import cn.javak.pojo.User;
import cn.javak.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @author: theTian
 * @date: 2020/5/31 15:10
 */
@RequestMapping("/login")
@RestController
public class LoginController {
    @Reference
    private UserService userService;
    @Autowired
    private TokenController tokenController;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    /**
     * 登录系统
     * todo 使用apache shiro做权限控制
     *
     * @param user
     * @return
     * @throws InterruptedException
     */
    @PostMapping("")
    public RespBean login(User user, HttpServletResponse response) throws InterruptedException {
        //todo 表单验证

        User resUser = userService.login(user);
        if (resUser == null || !resUser.getPassword().equals(user.getPassword())) {
            return RespBean.error("用户名或密码错误");
        }
        logger.info("用户["+user.getUsername()+"]开始登陆");
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("user", resUser);
        String token = tokenController.getToken(resUser);
        resMap.put("token", token);
        //记录最后登入时间
        resUser.setLastLoginTime(new Date());
        userService.update(resUser);
        return RespBean.ok("登陆成功", resMap);
    }
}
