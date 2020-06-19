package cn.javak.controller;

import cn.javak.annotation.UserLoginToken;
import cn.javak.pojo.RespBean;
import cn.javak.pojo.User;
import cn.javak.service.MailService;
import cn.javak.service.UserService;
import cn.javak.token.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author: theTian
 * @date: 2020/5/27 23:24
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    /**
     * 注册用户
     * @param user
     * @return
     * @throws InterruptedException
     */
    @PostMapping()
    public RespBean signup(User user) throws InterruptedException {
        //todo 表单验证

        User resUser = userService.signup(user);
        if (resUser!=null){
            //todo 线程池
            new Thread(()->{
                mailService.sendSimpleMail("javatt@qq.com", "用户注册成功["+user.getUsername()+"]","账户名: "+user.getUsername()+" 密码: "+user.getPassword());
            }).start();
            return RespBean.ok("注册成功，邮箱激活后登录", resUser);
        }
        return RespBean.error("该用户名已被占用");
    }

    /**
     * 修改个人信息
     * //todo 使用shiro确定本人修改
     * @return
     * @throws InterruptedException
     */
    @PutMapping("/{userId}")
    @UserLoginToken
    public RespBean changeInfo(User user) throws InterruptedException {
        Integer tokenUserId = TokenUtil.getTokenUserId();
        if (!tokenUserId.equals(user.getUserId())){
            RespBean.error("reject");
        }
        try {
            if (!StringUtils.isEmpty(user.getEmail())){
                if (null!=userService.selEmail(user.getEmail())){
                    return RespBean.error("该邮箱已被绑定");
                }
                new Thread(()->{
                    mailService.sendSimpleMail(user.getEmail(), "【易论坛】邮箱绑定成功","Good lucky for you!!!");
                }).start();
            }
            if (!StringUtils.isEmpty(user.getNickName())){
                User selUser = userService.selectByNN(user.getNickName());
                if (selUser != null){
                    //昵称被占用
                    return RespBean.error("该昵称已被占用");
                }
            }
            userService.update(user);
            User selUser = userService.login(user.getUserId());

            //发送邮件
            if (!StringUtils.isEmpty(selUser.getEmail())){
                if (!StringUtils.isEmpty(user.getNickName())){
                    //修改昵称
                    new Thread(()->{
                        mailService.sendSimpleMail(user.getEmail(), "【易论坛】昵称修改成功","您的昵称已经修改为"+selUser.getNickName()+"，如非操作请联系管理员");
                    }).start();
                }
            }
            return RespBean.ok("修改成功");
        }catch (Exception e){
            return RespBean.error("服务器繁忙");
        }

    }
}
