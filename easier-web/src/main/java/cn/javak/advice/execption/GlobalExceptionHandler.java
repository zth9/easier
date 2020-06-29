package cn.javak.advice.execption;

import cn.javak.pojo.RespBean;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: theTian
 * @date: 2020/6/29 17:01
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 接口有参数未传
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public RespBean missActionParam(HttpServletRequest req, MissingServletRequestParameterException e) throws Exception {
        return makeErrorObj("参数有误", req, e);
    }

    /**
     * 数字格式错误
     */
    @ExceptionHandler(value = NumberFormatException.class)
    @ResponseBody
    public RespBean numberFormatError(HttpServletRequest req, NumberFormatException e) throws Exception {
        return makeErrorObj("数字格式错误", req, e);
    }

    /**
     * JSON格式解析错误
     */
    @ExceptionHandler(value = JSONException.class)
    @ResponseBody
    public RespBean jsonError(HttpServletRequest req, JSONException e) throws Exception {
        return makeErrorObj("JSON格式解析错误", req, e);
    }

    /**
     * 服务器内部错误
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public RespBean nullError(HttpServletRequest req, NullPointerException e) throws Exception {
        return makeErrorObj("服务器内部错误", req, e);
    }

    /**
     * 未知错误
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RespBean scheduleError(HttpServletRequest req, Exception e) throws Exception {
        return makeErrorObj("服务器繁忙", req, e);
    }

    /**
     * 构造错误信息
     *
     * @param msg 错误描述
     * @param e   异常信息
     * @return
     */
    private RespBean makeErrorObj(String msg, HttpServletRequest req, Exception e) {
        JSONObject obj = new JSONObject();
        obj.put("detailMsg", e.getMessage());
        JSONObject logObj = new JSONObject();
        logObj.put("status", "failed");
        logObj.put("msg", msg);
        logObj.put("error", e.getMessage());
        logObj.put("url", req.getRequestURL());
        logObj.put("field", req.getParameterMap());
        //记录异常信息
        logger.error(logObj.toJSONString(), e);
        return RespBean.error(msg, obj);
    }
}