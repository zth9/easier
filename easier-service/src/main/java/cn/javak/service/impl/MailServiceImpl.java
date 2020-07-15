package cn.javak.service.impl;

import cn.javak.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author: theTian
 * @date: 2020/6/12 15:59
 */
@Service(timeout = 100000)
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleMail(String receiver, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 邮件发送者
        message.setTo(receiver); // 邮件接受者
        message.setSubject(subject); // 主题
        message.setText(content); // 内容

        mailSender.send(message);
    }
}