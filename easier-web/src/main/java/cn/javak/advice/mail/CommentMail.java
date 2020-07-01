package cn.javak.advice.mail;

import cn.javak.pojo.Blog;
import cn.javak.pojo.Comment;
import cn.javak.pojo.User;
import cn.javak.service.BlogService;
import cn.javak.service.MailService;
import cn.javak.service.UserService;
import cn.javak.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 评论邮件提醒
 * @author: theTian
 * @date: 2020/6/30 14:09
 */
@Aspect
@Component
public class CommentMail {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private MailService mailService;
    @Autowired
    private TaskExecutor taskExecutor;

    private static final Logger logger = LoggerFactory.getLogger(CommentMail.class);
    /**
     * 评论邮件提醒
     * 您的博文[标题]收到一条来自用户[评论者昵称]的评论，点击链接查看[博客链接地址]。
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("execution(* cn.javak.controller.CommentController.save(*))")
    public Object sendCommentMail(ProceedingJoinPoint point) throws Throwable {
        Object object = point.proceed();
        //新增评论成功 向博主发送邮件提醒
        Comment comment = (Comment)point.getArgs()[0];
        Integer blogId = comment.getBlogId();
        Blog blog = blogService.selectByBlogId(blogId);
        logger.info("博文["+blog.getTopic()+"]收到评论，准备发送邮件提醒");
        Integer bloggerId = blog.getUserId();
        User blogger = userService.selectByUserId(bloggerId);
        String email = blogger.getEmail();
        if (StringUtils.isEmpty(email)){
            logger.warn("用户"+bloggerId+"未绑定邮箱,无法发送邮件提醒");
        }else {
            Integer senderId = comment.getUserId();
            User sender = userService.selectByUserId(senderId);
            String senderNN = sender.getNickName();
            String blogTopic = blog.getTopic();
            String blogURL = Constants.ADDRESS.BLOG_PAGE_WITH_ID + blogId;
            taskExecutor.execute(()->{
                try {
                    mailService.sendSimpleMail(email, "评论提醒", "您的博文【"+blogTopic+"】收到一条来自用户【"+senderNN+"】的评论，点击链接查看\r\n"+blogURL+"。");
                    logger.info("发送评论提醒邮件成功");
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                    logger.error("发送评论提醒邮件失败");
                }
            });
        }
        return object;
    }
}
