package cn.javak.schedule;

import cn.javak.pojo.Weather;
import cn.javak.service.MailService;
import cn.javak.service.UserService;
import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: theTian
 * @date: 2020/7/15 11:54
 */
@Component
public class WeatherSchedule {
    @Reference
    private MailService mailService;
    @Reference
    private UserService userService;
    @Autowired
    private TaskExecutor taskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(WeatherSchedule.class);

    /**
     * 7:00 18:00 提醒天气情况
     */
//    @Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled(cron = "0 0 7,18 * * ?")
    public void send(){
        Weather weather = getWeather("郑州");
        String content = "今日天气："+weather.getWea()+"\r\n" +
                "城市："+weather.getCity()+"\r\n"+
                "当前温度："+weather.getTem()+"\r\n"+
                "最高温度："+weather.getTem1()+"\r\n"+
                "最低温度："+weather.getTem2()+"\r\n"+
                "风向："+weather.getWin()+"\r\n"+
                "风级："+weather.getWin_meter()+"\r\n"+
                "能见度："+weather.getVisibility()+"\r\n"+
                "空气等级："+weather.getAir_level()+"\r\n"+
                "提示："+weather.getAir_tips();
//        List<User> emails = userService.selectAllEmail();
//        emails.stream().forEach(user -> {
//            if (user.getEmail().endsWith("qq.com")){
//                taskExecutor.execute(()->{
//                    logger.info("向用户["+user.getUserId()+"]发送天气提醒");
//                    mailService.sendSimpleMail(user.getEmail(), "今日天气【"+weather.getWea()+"】", content);
//                });
//            }
//        });
        mailService.sendSimpleMail("javatt@qq.com", "今日天气【"+weather.getWea()+"】", content);
    }
    public Weather getWeather(String cityName){
        CloseableHttpClient httpClient  = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://yiketianqi.com/api?appid=83386693&appsecret=7m2LqT5B&version=v6&city="+cityName);
        CloseableHttpResponse response = null;
        Weather weather = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String json = unicodeDecode(EntityUtils.toString(responseEntity));

                weather = JSON.parseObject(json, Weather.class);
                logger.info("获取天气信息成功");
            }
        } catch (Exception e) {
            logger.error("获取天气信息失败");
            logger.error(e.getMessage(), e);
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return weather;
        }
    }
    public static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
}
