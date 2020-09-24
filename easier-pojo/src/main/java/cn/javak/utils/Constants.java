package cn.javak.utils;

import java.io.Serializable;

/**
 * @author: theTian
 * @date: 2020/6/30 15:51
 */
public class Constants implements Serializable {
    /**
     * 网站链接地址
     */
    public interface ADDRESS{
        //博客页链接 后跟blogId
        String BLOG_PAGE_WITH_ID = "http://123.57.236.58:8080/easier/blog.html?";

        //头像绝对地址
        String AVATAR_ABSOLUTE_DIR = "/home/ftpuser/easier/user/avatar/";

        //头像外链
        String AVATAR_NET_ADDRESS = "http://123.57.236.58/easier/user/avatar/";
    }

    /**
     * 待办状态码
     */
    public interface TODO{
        //正在路上
        String NOT_REMIND = "0";
        //已阵亡
        String REMINDED = "1";
        //仅提醒一次
        String ONCE = "0";
        //每天都提醒
        String EVERYDAY = "1";
    }

    /**
     * 时间常量
     */
    public interface DATE{
        long ONE_DAY_MILLISECONDS = 1000 * 60 * 60 * 24;
    }
}
