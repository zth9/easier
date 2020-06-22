package cn.javak.token;

import com.auth0.jwt.JWT;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: theTian
 * @date: 2020/6/16 17:56
 */
public class TokenUtil {
    public static String getToken(){
        return  getRequest().getHeader("token");
    }

    /**
     * 从header中获取token 再获取userId
     * @return
     */
    public static Integer getTokenUserId() {
        String token = getRequest().getHeader("token");// 从 http 请求头中取出 token
        String userId = JWT.decode(token).getAudience().get(0);
        if (userId == null){
            return null;
        }
        return Integer.parseInt(userId);
    }

    /**
     * 获取request
     *
     * @return
     */
    private static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }
}
