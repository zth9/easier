package cn.javak.controller;

import cn.javak.utils.Constants;
import cn.javak.utils.FTPUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.*;
import java.util.HashMap;

/**
 * @author: theTian
 * @date: 2020/7/3 13:58
 */
@SpringBootTest
@Import(UserController.class)
public class UserControllerTest {
    private static final Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    public void upload() {
        File file = new File("E:\\work\\入职\\简历信息.html");
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            String fileName = Constants.ADDRESS.AVATAR_ABSOLUTE_DIR + "简历信息.html";
            boolean uploadOk = FTPUtils.uploadFile(FTPUtils.loginFTP(), Constants.ADDRESS.AVATAR_ABSOLUTE_DIR, fileName, inputStream);
            if (uploadOk) {
                logger.info("上传成功");
            }else {
                logger.error("上传失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
    public void test(){
    }
}
