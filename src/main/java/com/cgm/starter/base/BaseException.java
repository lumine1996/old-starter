package com.cgm.starter.base;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通用异常
 *
 * @author cgm
 */
public class BaseException extends RuntimeException {

    /**
     * 错误编码
     * 无编码时，使用错误提示信息
     */
    private final String code;

    /**
     * 主要的构造函数
     *
     * @param code 错误编码
     */
    public BaseException(String code) {
        super(code);
        this.code = code;
    }

    public BaseException(String code, Throwable cause) {
        super(code, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getTrace() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(outputStream);
        this.printStackTrace(ps);
        ps.flush();
        return new String(outputStream.toByteArray());
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("code", code);
        map.put("message", super.getMessage());
        return map;
    }
}
