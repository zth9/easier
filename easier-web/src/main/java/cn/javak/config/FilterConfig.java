package cn.javak.config;

import cn.javak.filter.TimeFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置过滤器
 * @author: theTian
 * @date: 2020/7/2 10:52
 */
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<TimeFilter> timeFilter(){
        TimeFilter filter = new TimeFilter();
        FilterRegistrationBean<TimeFilter> filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns("/*"); //拦截的路径（对所有请求拦截）
        filterRegistrationBean.setName("TestFilter"); //拦截器的名称
        filterRegistrationBean.setOrder(1); //拦截器的执行顺序。数字越小越先执行
        return filterRegistrationBean;
    }
}
