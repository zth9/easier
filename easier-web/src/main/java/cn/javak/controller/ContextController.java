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
    public RespBean getClickNum(HttpServletRequest request){
        try {
            ServletContext context = request.getServletContext();
            Integer clickNum = (Integer)context.getAttribute("clickNum");
            Map<String, Integer> map = new HashMap<>();
            map.put("clickNum", clickNum);
            return RespBean.ok("获取网站点击量成功", map);
        }catch (Exception e){
            logger.error("获取点击量失败");
            logger.error(e.getMessage(), e);
            return RespBean.error("获取点击量失败");
        }
    }

    @PreDestroy
    public void destroy(){
        try {
            Integer clickNum = (Integer) webApplicationConnect.getServletContext().getAttribute("clickNum");
            contextMapper.updateClickNum(clickNum);
            logger.info("更新点击量成功"+clickNum);
        }catch (Exception e){
            logger.error("更新点击量失败", e);
        }
    }
}
