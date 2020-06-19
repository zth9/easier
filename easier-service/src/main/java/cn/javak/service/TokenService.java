package cn.javak.service;

import cn.javak.pojo.User;
import cn.javak.token.JwtUtil;
import cn.javak.token.TokenUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: theTian
 * @date: 2020/6/16 17:57
 */
@Service
public class TokenService {
    public String getToken(User user) {
        String userId = String.valueOf(user.getUserId());
        String token = JwtUtil.sign(userId);
        return token;
    }

    public boolean verity(){
        String token = TokenUtil.getToken();
        return JwtUtil.verity(token);
    }
}
