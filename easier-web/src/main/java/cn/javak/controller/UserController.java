package cn.javak.controller;

import cn.javak.annotation.UserLoginToken;
import cn.javak.pojo.RespBean;
import cn.javak.pojo.User;
import cn.javak.service.MailService;
import cn.javak.service.UserService;
import cn.javak.token.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author: theTian
 * @date: 2020/5/27 23:24
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    @Reference
    private MailService mailService;

    @Autowired
    private TaskExecutor taskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 注册用户
     *
     * @param user
     * @return
     * @throws InterruptedException
     */
    @PostMapping("")
    public RespBean signup(User user) throws InterruptedException {
        //todo 表单验证

        User resUser = userService.signup(user);
        if (resUser != null) {
            //todo 线程池
            taskExecutor.execute(() -> {
                logger.info("用户[" + user.getUsername() + "]注册成功");
                mailService.sendSimpleMail("javatt@qq.com", "用户注册成功[" + user.getUsername() + "]", "账户名: [" + user.getUsername() + "] 密码: [" + user.getPassword() + "]");
            });
            return RespBean.ok("注册成功，邮箱激活后登录", resUser);
        }
        return RespBean.error("该用户名已被占用");
    }

    /**
     * 修改个人信息
     * //todo 使用shiro确定本人修改
     *
     * @return
     * @throws InterruptedException
     */
    @PutMapping("/{userId}")
    @UserLoginToken
    public RespBean changeInfo(User user) throws InterruptedException {
        Integer tokenUserId = TokenUtil.getTokenUserId();
        if (!tokenUserId.equals(user.getUserId())) {
            RespBean.error("reject");
        }
        //修改邮箱
        if (!StringUtils.isEmpty(user.getEmail())) {
            if (null != userService.selEmail(user.getEmail())) {
                return RespBean.error("该邮箱已被绑定");
            }
            logger.info("用户[" + user.getUsername() + "]将邮箱绑定为[" + user.getEmail() + "]");
            taskExecutor.execute(() -> {
                mailService.sendSimpleMail(user.getEmail(), "【易论坛】邮箱绑定成功", "Good lucky for you!!!");
            });
        }
        //修改昵称
        if (!StringUtils.isEmpty(user.getNickName())) {
            User selUser = userService.selectByNN(user.getNickName());
            if (selUser != null) {
                //昵称被占用
                return RespBean.error("该昵称已被占用");
            }
            logger.info("用户[" + user.getUsername() + "]将昵称改为[" + user.getNickName() + "]");
        }
        //修改密码
        if (!StringUtils.isEmpty(user.getPassword())) {
            logger.info("用户[" + user.getUsername() + "]将密码改为[" + user.getPassword() + "]");
        }
        userService.update(user);
        User selUser = userService.login(user.getUserId());

        //发送邮件
        if (!StringUtils.isEmpty(selUser.getEmail())) {
            if (!StringUtils.isEmpty(user.getNickName())) {
                //修改昵称
                taskExecutor.execute(() -> {
                    logger.info("已向用户[" + user.getUsername() + "]发送确认邮件");
                    mailService.sendSimpleMail(selUser.getEmail(), "【易论坛】昵称修改成功", "您的昵称已经修改为[" + selUser.getNickName() + "]，如非本人操作请联系管理员");
                });
            }
        }
        return RespBean.ok("修改成功", selUser);
    }

    @PutMapping("/uploadAvatar")
    @UserLoginToken
    public RespBean uploadAvatar(@RequestParam(required = true) MultipartFile file) throws IOException {
        Integer tokenUserId = TokenUtil.getTokenUserId();
        return userService.updateAvatar(file, tokenUserId);
    }
}
