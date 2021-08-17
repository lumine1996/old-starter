package com.cgm.starter.util;

import com.cgm.starter.account.entity.SysUser;
import com.cgm.starter.base.BaseException;
import com.cgm.starter.base.Constant;
import com.cgm.starter.base.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @author cgm
 */
public class UserUtils {
    private UserUtils() {

    }

    /**
     * 获取当前请求的用户
     *
     * @return 用户
     */
    public static SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return (SysUser) authentication.getPrincipal();
        }
        throw new BaseException(ErrorCode.USER_NOT_LOGIN);
    }

    /**
     * 获取当前请求用户的组织ID
     *
     * @return 组织ID
     */
    public static Integer getCurrentUserOrgId() {
        return getCurrentUser().getOrganizationId();
    }

    /**
     * 判断当前请求的用户是否超管
     * 如果已经获取了用户，可以使用SysUser->hasRole("SYS_ADMIN")代替此方法
     *
     * @return 是否超管
     */
    public static boolean isSystemAdmin() {
        // 获取用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.isEmpty()) {
            return false;
        }
        // 检查用户权限是否包含超管
        for (GrantedAuthority authority : authorities) {
            if (Constant.ROLE_SYSTEM_ADMIN.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
