package cn.javak.advice.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: theTian
 * @date: 2020/6/29 16:49
 */
@Aspect
@Component
public class LoggerAdvice {
    /**
     * 但文章页加载 日志记录
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* cn.javak.controller.BlogController.load(int))")
    public Object method(ProceedingJoinPoint point) throws Throwable {
        Logger logger = LoggerFactory.getLogger(point.getTarget().getClass());
        logger.info("访问博客页,id为:" + point.getArgs()[0]);
        Object object = point.proceed();
        return object;
    }
}
