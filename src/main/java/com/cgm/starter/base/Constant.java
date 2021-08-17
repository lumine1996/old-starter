package com.cgm.starter.base;

/**
 * 常量
 * 主要按字母排序
 *
 * @author cgm
 */
public class Constant {
    private Constant() {
    }

    /**
     * API前缀, /api/public/开头的不会被拦截
     */
    public static final String API_NORMAL = "/api/";
    public static final String API_PUBLIC = "/api/public/";

    /**
     * 打印异常时的标题
     */
    public static final String EXCEPTION_LOG_TITLE = "Exception occurred!";

    /**
     * 操作系统前缀-Windows
     */
    public static final String OS_NAME_PREFIX_WINDOWS = "Windows";

    /**
     * SYS_ADMIN: 系统超级管理员角色，为避免误用，其他角色不得包含"SYS_ADMIN"
     * ORG_ADMIN: 组织管理员角色
     * USER: 普通用户角色，所有登录用户都具有此角色，是除了匿名用户的最小权限集
     * ROLE_ANONYMOUS: 匿名角色（未登录），源自Spring Security的定义
     */
    public static final String ROLE_SYSTEM_ADMIN = "SYS_ADMIN";
    public static final String ROLE_ORG_ADMIN = "ORG_ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * 系统属性-操作系统名称
     */
    public static final String SYS_PROPERTY_OS_NAME = "os.name";

}
