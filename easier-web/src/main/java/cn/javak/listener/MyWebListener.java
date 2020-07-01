package cn.javak.listener;

import cn.javak.mapper.ContextMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 用于在网站加载前加载数据到{@link ServletContext}
 * @author: theTian
 * @date: 2020/6/29 14:20
 */
@Component
public class MyWebListener implements ServletContextListener {
    @Autowired
    private ContextMapper contextMapper;
    private static final Logger logger = LoggerFactory.getLogger(MyWebListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("项目初始化变量");
        ServletContext context = sce.getServletContext();
        Integer clickNum = null;
        try {
            clickNum = contextMapper.selectClickNum();
            context.setAttribute("clickNum",clickNum);
            logger.info("初始化网站点击量"+clickNum);
        }catch (Exception e){
            logger.error("获取点击量失败", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("项目终止");
    }
}
