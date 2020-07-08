package cn.javak.service;

import cn.javak.mapper.TodoMapper;
import cn.javak.mapper.UserMapper;
import cn.javak.pojo.Todo;
import cn.javak.pojo.User;
import cn.javak.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author: theTian
 * @date: 2020/7/7 16:20
 */
@Service
public class TodoService {
    private static final Logger logger = LoggerFactory.getLogger(TodoService.class);
    @Autowired
    private TodoMapper todoMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private MailService mailService;

    /**
     * 根据用户id查询所有待办列表
     *
     * @param userId
     * @return
     */
    public List<Todo> selectByUserId(Integer userId) {
        List<Todo> todoList = todoMapper.selectByUserId(userId);
        return todoList;
    }

    /**
     * 根据todoId查询待办记录
     *
     * @param todoId
     * @return
     */
    public Todo selectByTodoId(Integer todoId) {
        return todoMapper.selectByPrimaryKey(todoId);
    }

    /**
     * 新增一条待办提醒
     *
     * @param todo
     * @return
     */
    public int save(Todo todo) {
        int res = todoMapper.insert(todo);
        return res;
    }

    /**
     * 更新待办信息
     *
     * @param todo
     * @return
     */
    public int update(Todo todo) {
        return todoMapper.updateByPrimaryKeySelective(todo);
    }

    /**
     * 删除一条待办信息
     *
     * @param todoId
     * @return
     */
    public int delete(Integer todoId) {
        return todoMapper.deleteByPrimaryKey(todoId);
    }

    /**
     * 执行提醒任务
     */
    public void scheduleTodo() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.SECOND, 0);
        Date date = instance.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        List<Todo> todoList = todoMapper.selectAllNotRemind(format);
        if (CollectionUtils.isEmpty(todoList)) {
            return;
        }
        List<User> users = userMapper.selectOnlyEmail();
        HashMap<Integer, String> map = new HashMap<>(users.size());
        for (User user : users) {
            map.put(user.getUserId(), user.getEmail());
        }
        for (Todo todo : todoList) {
            Integer userId = todo.getUserId();
            String email = map.get(userId);
            if (email != null) {
                taskExecutor.execute(() -> {
                    mailService.sendSimpleMail(email, "TODO提醒", todo.getTodoContent());
                    logger.info("待办信息[" + todo.getTodoId() + "]的提醒邮件发送成功");
                    if (Constants.TODO.ONCE.equals(todo.getTodoType())) {
                        //仅一次 标记为阵亡
                        todo.setTodoStatus(Constants.TODO.REMINDED);
                        update(todo);
                    } else {
                        Date todoTime = todo.getTodoTime();
                        todoTime.setTime(todoTime.getTime() + Constants.DATE.ONE_DAY_MILLISECONDS);
                        todo.setTodoTime(todoTime);
                        update(todo);
                    }
                });
            }
        }
    }
}
