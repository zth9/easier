package cn.javak.controller;

import cn.javak.mapper.ContextMapper;
import cn.javak.pojo.RespBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: theTian
 * @date: 2020/6/29 14:56
 */
@RestController
@RequestMapping("/context")
public class ContextController {
    @Autowired
    private ContextMapper contextMapper;
    @Autowired
    private WebApplicationContext webApplicationConnect;

    private static final Logger logger = LoggerFactory.getLogger(ContextController.class);

    @GetMapping("/clickNum")
    public RespBean getClickNum(HttpServletRequest request) {
        ServletContext context = request.getServletContext();
        AtomicInteger clickNum = (AtomicInteger) context.getAttribute("clickNum");
        Map<String, Integer> map = new HashMap<>();
        map.put("clickNum", clickNum.get());
        return RespBean.ok("获取网站点击量成功", map);
    }

    @PreDestroy
    public void destroy() {
        AtomicInteger clickNum = (AtomicInteger) webApplicationConnect.getServletContext().getAttribute("clickNum");
        contextMapper.updateClickNum(clickNum.get());
        logger.info("更新点击量成功" + clickNum.get());
    }
}
