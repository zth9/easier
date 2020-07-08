package cn.javak.interceptor;

import cn.javak.annotation.PassToken;
import cn.javak.annotation.UserLoginToken;
import cn.javak.pojo.RespBean;
import cn.javak.service.TokenService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * token认证拦截器
 * 拦截注解{@link UserLoginToken},校验token,如未持有token或者token失效则拦截该请求
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    public String getPath(ServletRequest request) {
        return ((HttpServletRequest) request).getRequestURL().toString();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

//        logger.info(getPath(request)+"进入拦截器");

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
//            logger.info(getPath(request)+"不是映射到方法直接通过");
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注解，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
//                logger.info(getPath(request)+"有passtoken注解 通过");
                return true;
            }
        }

        String token = request.getHeader("token");// 从 http 请求头中取出 token
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 验证token
                if (token == null || !tokenService.verity()) {
                    String res = JSON.toJSONString(RespBean.error("tokenError"));
                    response.getWriter().print(res);
//                    logger.info(getPath(request)+"tokenError 驳回");
                    return false;
                }
//                logger.info(getPath(request)+"token正确 通过");
                return true;
            }
        }
//        logger.info(getPath(request)+"无注解标记 通过");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}
