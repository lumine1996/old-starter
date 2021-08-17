package com.cgm.starter.base;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.Locale;

/**
 * 基础Controller
 *
 * @author cgm
 */
@RestController
@Slf4j
public class BaseController {

    @Autowired
    private MessageSource messageSource;

    /**
     * 处理控制层所有异常
     *
     * @param exception 未捕获的异常
     * @param request   HttpServletRequest
     * @return ResponseData或ModelAndView
     * @see ResponseData
     */
    @ExceptionHandler(value = {Exception.class})
    public Object exceptionHandler(Exception exception, HttpServletRequest request) {
        Throwable thr = Throwables.getRootCause(exception);
        Locale locale = RequestContextUtils.getLocale(request);

        if (isRestRequest(request)) {
            ResponseData res;
            if (thr instanceof BaseException) {
                // 通用异常处理
                res = ExceptionConverter.handleBaseException((BaseException) thr);
            } else if (thr instanceof IllegalArgumentException) {
                // 非法参数异常处理
                res = ExceptionConverter.handleIllegalArgumentException((IllegalArgumentException) thr);
            } else if (thr instanceof ConnectException) {
                // 网络连接异常处理
                res = ExceptionConverter.handleConnectException((ConnectException) thr);
            } else if (thr instanceof IOException) {
                // IO异常处理
                res = ExceptionConverter.handleIOException((IOException) thr);
            } else if (thr instanceof SQLException) {
                // SQL异常处理
                res = ExceptionConverter.handleSqlException((SQLException) thr);
            } else {
                // 其他异常
                res = ExceptionConverter.handleOtherException(thr);
            }

            // 输出简化后的错误堆栈
            log.error(exception.getMessage());
            res.getTrace().forEach(log::error);

            // 多语言提示
            res.setMessage(messageSource.getMessage(res.getCode(), null, locale));
            return res;
        } else {
            // 返回mv，本项目暂不存在mv
            ModelAndView view = new ModelAndView("500");
            if (thr instanceof BaseException) {
                BaseException be = (BaseException) thr;
                String message = messageSource.getMessage(be.getCode(), null, locale);
                view.addObject("message", message);
            }
            return view;
        }
    }

    private boolean isRestRequest(HttpServletRequest request) {
        String xr = request.getHeader("X-Requested-With");
        String xmlHttpRequest = "XMLHttpRequest";
        if (xmlHttpRequest.equals(xr)) {
            return true;
        }
        if (request.getRequestURI().contains(Constant.API_NORMAL)) {
            return true;
        }
        String contentType = request.getContentType();
        return contentType != null && contentType.toLowerCase().startsWith("multipart/");
    }
}
