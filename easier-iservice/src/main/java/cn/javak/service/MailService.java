package cn.javak.service;

/**
 * @author: theTian
 * @date: 2020/7/13 14:25
 */
public interface MailService {
    void sendSimpleMail(String receiver, String subject, String content);
}
