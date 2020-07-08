package cn.javak.schedule;

import cn.javak.service.TodoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: theTian
 * @date: 2020/7/8 11:33
 */
@Component
public class TodoSchedule {
    @Autowired
    private TodoService todoService;

    private static final Logger logger = LoggerFactory.getLogger(TodoSchedule.class);
    /**
     * 每分钟执行一次提醒任务
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduleTodo(){
        logger.info("执行提醒任务");
        todoService.scheduleTodo();
    }
}
