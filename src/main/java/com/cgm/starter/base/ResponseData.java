package com.cgm.starter.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author cgm
 */
@ApiModel("数据返回对象")
@Slf4j
public class ResponseData {

    private static final String PAGE_CLASS_NAME = "com.github.pagehelper.PageInfo";

    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty(value = "状态编码")
    private String code;

    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty(value = "提示信息（面向用户）")
    private String message;

    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty(value = "详细描述（面向开发团队）")
    private String detail;

    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty(value = "数据")
    private Object result;

    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty(value = "异常堆栈")
    private List<String> trace;

    @ApiModelProperty(value = "成功标识")
    private boolean success = true;

    @JsonInclude(Include.NON_NULL)
    @ApiModelProperty(value = "总数")
    private Integer total;

    public ResponseData() {
    }

    public ResponseData(boolean success) {
        setSuccess(success);
    }

    public ResponseData(Object object) {
        this(true);
        setResult(object);
    }

    public ResponseData(boolean success, String message) {
        this(success);
        setMessage(message);
    }

    public ResponseData(String code, String message, Object result, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
        setResult(result);
    }

    public ResponseData(String code, String message, String detail, List<String> trace) {
        this.code = code;
        this.message = message;
        this.success = false;
        this.detail = detail;
        this.trace = trace;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDetail() {
        return detail;
    }

    public Object getResult() {
        return result;
    }

    public List<String> getTrace() {
        return trace;
    }

    public Integer getTotal() {
        return total;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setResult(Object result) {
        if (result == null) {
            this.setTotal(0);
            return;
        }
        try {
            if (result instanceof List) {
                // 返回列表
                this.result = result;
                setTotal(((List<?>) result).size());
            } else if (PAGE_CLASS_NAME.equals(result.getClass().getCanonicalName())) {
                // 返回分页结果
                this.result = result.getClass().getDeclaredMethod("getList").invoke(result);
                Long pageTotal = (Long) result.getClass().getDeclaredMethod("getTotal").invoke(result);
                setTotal(pageTotal.intValue());
            } else {
                // 其他, 直接返回
                this.result = result;
                setTotal(1);
            }
        } catch (Exception e) {
            log.error(Constant.EXCEPTION_LOG_TITLE, e);
            throw new BaseException(ErrorCode.SYS_PAGE_FAILED);
        }
    }

    public void setTrace(List<String> trace) {
        this.trace = trace;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
