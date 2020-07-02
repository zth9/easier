package cn.javak.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 拦截请求并记录时间
 * @author: theTian
 * @date: 2020/7/2 9:06
 */
public class TimeFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TimeFilter.class);
    @Override
    public void init(FilterConfig filterConfig){
    }
    public String getPath(ServletRequest request){
        return ((HttpServletRequest) request).getRequestURL().toString();
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
//        logger.info(getPath(request)+"进入过滤器");
        long start = System.currentTimeMillis();
        filterChain.doFilter(request, response);
//        logger.info(getPath(request)+"filter耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    public void destroy() {
    }

}
