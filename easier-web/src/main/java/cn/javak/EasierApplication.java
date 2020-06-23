package cn.javak;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author: theTian
 * @date: 2020/5/27 23:25
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.javak.mapper")
public class EasierApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(EasierApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
