package cn.javak.service;

import cn.javak.pojo.RespBean;
import cn.javak.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

/**
 * @author: theTian
 * @date: 2020/7/13 14:25
 */
@Service
public interface UserService {
    //登录 by user 取出username
    User login(User user);

    //登录 by username
    User login(String username);

    //登录 by userId
    User login(Integer userId);

    //注册
    User signup(User user);

    void update(User user);

    User selEmail(String email);

    User selectByNN(String nickName);

    User selectByUserId(Integer userId);

    RespBean updateAvatar(MultipartFile file, Integer tokenUserId) throws IOException;

    Set<String> getRoles(String username);
}
