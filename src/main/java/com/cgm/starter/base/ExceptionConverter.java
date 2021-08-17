package com.cgm.starter.base;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cgm
 */
@Slf4j
public class ExceptionConverter {
    private static final String TRACE_BREAK = "\r\n\t";
    private static final String BASE_PACKAGE = ExceptionConverter.class.getPackage().getName()
            .replace(".base", "");
    private static final String TIMEOUT_EXCEPTION_SIGN = "Timeout";

    private ExceptionConverter() {

    }

    /**
     * 处理通用异常
     *
     * @param exception 通用异常
     * @return 数据返回对象
     */
    public static ResponseData handleBaseException(BaseException exception) {
        List<String> traceList = getSimpleTrace(exception);
        return new ResponseData(exception.getCode(), null, null, traceList);
    }

    /**
     * 处理非法参数异常
     * Assert时的message参数使用错误编码, 会在此处被引用
     *
     * @param exception 通用异常
     * @return 数据返回对象
     */
    public static ResponseData handleIllegalArgumentException(IllegalArgumentException exception) {
        // 如果是Assert抛出的异常, 会使用其message作为错误编码
        String sysErrorPrefix = "sys.";
        String userErrorPrefix = "user.";
        String errorCode = ErrorCode.SYS_ILLEGAL_ARGUMENT;
        String exceptionMessage = exception.getMessage();
        if (exceptionMessage.startsWith(sysErrorPrefix) || exceptionMessage.startsWith(userErrorPrefix)) {
            errorCode = exceptionMessage;
        }

        List<String> traceList = getSimpleTrace(exception);
        return new ResponseData(errorCode, null, exceptionMessage, traceList);
    }

    /**
     * 处理连接异常
     *
     * @param exception 连接异常
     * @return 数据返回对象
     */
    public static ResponseData handleConnectException(IOException exception) {
        List<String> traceList = getSimpleTrace(exception);
        String errorCode = ErrorCode.SYS_CONNECTION_REFUSED;
        return new ResponseData(errorCode, null, exception.getMessage(), traceList);
    }

    /**
     * 处理IO异常
     *
     * @param exception IO异常
     * @return 数据返回对象
     */
    public static ResponseData handleIOException(IOException exception) {
        List<String> traceList = getSimpleTrace(exception);
        String errorCode = ErrorCode.SYS_IO_EXCEPTION;
        if (exception instanceof SocketTimeoutException) {
            errorCode = ErrorCode.SYS_CONNECTION_TIMED_OUT;
        }
        return new ResponseData(errorCode, null, exception.getMessage(), traceList);
    }

    /**
     * 处理Sql异常
     *
     * @param exception Sql异常
     * @return 数据返回对象
     */
    public static ResponseData handleSqlException(SQLException exception) {
        List<String> traceList = getSimpleTrace(exception);
        return new ResponseData(ErrorCode.SYS_SQL_EXCEPTION, null, exception.getMessage(), traceList);
    }

    /**
     * 处理其他异常
     *
     * @param exception 异常
     * @return 数据返回对象
     */
    public static ResponseData handleOtherException(Throwable exception) {
        String errorCode = ErrorCode.SYS_INTERNAL_ERROR;
        if (exception.getClass().getName().contains(TIMEOUT_EXCEPTION_SIGN)) {
            errorCode = ErrorCode.SYS_CONNECTION_TIMED_OUT;
        }

        List<String> traceList = getSimpleTrace(exception);
        return new ResponseData(errorCode, null, exception.getMessage(), traceList);
    }

    /**
     * 获取简单堆栈
     *
     * @param exception 异常
     * @return 删减后的异常堆栈
     */
    public static List<String> getSimpleTrace(Throwable exception) {
        // 堆栈转字符串数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        exception.printStackTrace(ps);
        ps.flush();
        String trace = outputStream.toString();
        String[] traceArray = trace.split(TRACE_BREAK);

        // 按包名过滤需要的堆栈信息, startsWith加速匹配，offset为开头的"at "长度
        return Arrays.stream(traceArray)
                .filter(e -> e.startsWith(BASE_PACKAGE, 3) || !e.startsWith("at "))
                .collect(Collectors.toList());
    }
}
