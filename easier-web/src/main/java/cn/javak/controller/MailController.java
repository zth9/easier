package cn.javak.controller;

import cn.javak.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: theTian
 * @date: 2020/6/12 15:55
 */
@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @GetMapping("/register")
    public void sendRegisterValid(){
        mailService.sendSimpleMail("javatt@qq.com", "恭喜你，注册成功","注册链接+123");
    }
}
