package com.cgm.starter.base;

/**
 * 错误编码
 * 排序：USER->SYS->
 * 字段名规则：USER/SYS/第三方系统开头，全部大写下划线
 * 编码规则：user/sys/第三方系统开头，层级用小数点分隔，短语用下划线连接，全部小写
 *
 * @author cgm
 */
public class ErrorCode {

    private ErrorCode() {
    }

    public static final String USER_ACCOUNT_EXPIRED = "user.account_expired";
    public static final String USER_CREDENTIALS_ERROR = "user.credentials_error";
    public static final String USER_CREDENTIALS_EXPIRED = "user.credentials_expired";
    public static final String USER_DISABLE = "user.disable";
    public static final String USER_NOT_EXIST = "user.not_exist";
    public static final String USER_LOCKED = "user.locked";
    public static final String USER_NOT_LOGIN = "user.not_login";
    public static final String USER_SESSION_EXPIRED = "user.session_expired";

    public static final String USER_JWT_EXPIRED = "user.jwt_expired";
    public static final String USER_JWT_UNSUPPORTED = "user.jwt_unsupported";
    public static final String USER_JWT_SIGNATURE_ERROR = "user.jwt_signature_error";

    public static final String USER_PERMISSION_DENIED = "user.permission_denied";
    public static final String USER_ORGANIZATION_INVALID = "user.organization_invalid";

    public static final String USER_INCORRECT_FORMAT = "user.incorrect_format";
    public static final String USER_INVALID_INPUT = "user.invalid_input";

    public static final String SYS_INTERNAL_ERROR = "sys.internal_error";
    public static final String SYS_AUTHENTICATION_ERROR = "sys.authentication_error";

    public static final String SYS_CONNECTION_TIMED_OUT = "sys.connection_timed_out";
    public static final String SYS_CONNECTION_REFUSED = "sys.connection_refused";

    public static final String SYS_FILE_NOT_FOUND = "sys.file_not_found";
    public static final String SYS_FILE_DELETE_FAILED = "sys.file_delete_failed";

    public static final String SYS_QUERY_FAILED = "sys.query_failed";
    public static final String SYS_NO_FIELD = "sys.no_field";
    public static final String SYS_PAGE_FAILED = "sys.page_failed";
    public static final String SYS_DELETE_FAILED = "sys.delete_failed";

    public static final String SYS_ORG_ADD_FAILED = "sys.org_add_failed";

    public static final String SYS_ILLEGAL_ARGUMENT = "sys.illegal_argument";

    public static final String SYS_IO_EXCEPTION = "sys.io_exception";
    public static final String SYS_JWT_EXCEPTION = "sys.jwt_exception";
    public static final String SYS_SQL_EXCEPTION = "sys.sql_exception";
}
