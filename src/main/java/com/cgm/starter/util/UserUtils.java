package com.cgm.starter.util;

import com.cgm.starter.account.entity.SysRole;
import com.cgm.starter.account.entity.SysUser;
import com.cgm.starter.base.BaseException;
import com.cgm.starter.base.Constant;
import com.cgm.starter.base.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

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
     * 如果已经获取了用户，建议使用有参方法
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

    /**
     * 判断用户是否超管
     *
     * @param user 用户
     * @return 是否超管
     */
    public static boolean isSystemAdmin(SysUser user) {
        return user.hasRole(Constant.ROLE_SYSTEM_ADMIN);
    }

    /**
     * 获取用户角色级别
     *
     * @param user 用户
     * @return 角色级别
     */
    public static Integer getRoleLevel(SysUser user) {
        return getRoleLevel(user.getRoles());
    }

    public static Integer getRoleLevel(List<SysRole> userRoleList) {
        int level = 0;
        if (userRoleList == null || userRoleList.isEmpty()) {
            return level;
        }

        for (SysRole role : userRoleList) {
            level = Math.max(level, role.getLevel() == null ? 0 : role.getLevel());
        }
        return level;
    }
}
