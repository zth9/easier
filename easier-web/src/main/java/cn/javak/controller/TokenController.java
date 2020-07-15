package cn.javak.controller;

import cn.javak.pojo.User;
import cn.javak.token.JwtUtil;
import cn.javak.token.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @author: theTian
 * @date: 2020/6/16 17:57
 */
@Controller
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    public String getToken(User user) {
        String userId = String.valueOf(user.getUserId());
        logger.info("用户["+user.getUsername()+"]下发token");
        String token = JwtUtil.sign(userId);
        return token;
    }

    public boolean verity(){
        String token = TokenUtil.getToken();
        return JwtUtil.verity(token);
    }
}
