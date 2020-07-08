package cn.javak.controller;

import cn.javak.annotation.UserLoginToken;
import cn.javak.pojo.RespBean;
import cn.javak.pojo.Todo;
import cn.javak.service.TodoService;
import cn.javak.token.TokenUtil;
import cn.javak.utils.Constants;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * TodoController包含了待办相关操作的接口
 * @author: theTian
 * @date: 2020/7/7 15:33
 */
@RestController
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService todoService;
    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    /**
     * 插入一条待办提醒
     * @param todo
     * @return
     */
    @PostMapping("")
    @UserLoginToken
    public RespBean save(Todo todo) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Integer userId = TokenUtil.getTokenUserId();
        if (todo.getTodoId()!=null){
            Todo selTodo = todoService.selectByTodoId(todo.getTodoId());
            if (!selTodo.getUserId().equals(userId)){
                //非本人操作
                logger.info("用户["+userId+"]进行非本人修改待办操作，原待办信息为"+JSON.toJSONString(selTodo)+"欲更新待办为"+JSON.toJSONString(todo));
                return RespBean.error("reject");
            }
            todo.setTodoStatus(Constants.TODO.NOT_REMIND);
            todo.setTodoTime(sdf.parse(todo.getTodoTimeStr()));
            todo.setUserId(userId);
            //触发修改待办
            todoService.update(todo);
            logger.info("用户["+userId+"]修改待办提醒["+todo.getTodoId()+"]为"+JSON.toJSONString(todo));
            return RespBean.ok("修改成功");
        }
        todo.setUserId(userId);
        String todoTimeStr = todo.getTodoTimeStr();
        Date date = sdf.parse(todoTimeStr);
        todo.setTodoTime(date);
        //设值状态为未提醒
        todo.setTodoStatus(Constants.TODO.NOT_REMIND);
        todoService.save(todo);
        logger.info("用户["+userId+"]新增待办提醒"+JSON.toJSONString(todo));
        return RespBean.ok("添加成功");
    }

    /**
     * 根据userId获取该用户所有待办列表
     * @return
     */
    @GetMapping("")
    @UserLoginToken
    public RespBean getTodoList(){
        Integer tokenUserId = TokenUtil.getTokenUserId();
        List<Todo> todoList = todoService.selectByUserId(tokenUserId);
        return RespBean.ok("查询待办列表成功", todoList);
    }

    /**
     * 根据todoId&userId删除单条待办信息
     * @param todoId
     * @return
     */
    @DeleteMapping("/{todoId}")
    @UserLoginToken
    public RespBean deleteTodo(@PathVariable("todoId") Integer todoId) throws Exception {
        if (null == todoId){
            //参数为空
            throw new MissingServletRequestParameterException("缺少todoId","Integer");
        }
        Integer curUserId = TokenUtil.getTokenUserId();
        Todo todo = todoService.selectByTodoId(todoId);
        if (null == todo){
            //不存在该todoId对应的记录
            throw new RuntimeException("未查询到该待办记录");
        }
        if (!curUserId.equals(todo.getUserId())){
            //非本人操作
            logger.info("用户["+curUserId+"]进行非本人删除待办操作，原待办信息为"+JSON.toJSONString(todo));
            return RespBean.error("reject");
        }
        try {
            todoService.delete(todoId);
        }catch (Exception e){
            throw new RuntimeException("删除失败");
        }
        logger.info("用户["+curUserId+"]成功删除一条待办"+JSON.toJSONString(todo));
        return RespBean.ok("删除成功");
    }
}
