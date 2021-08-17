package com.cgm.starter.account.service;

/**
 * @author cgm
 */
public interface ISysUserRoleService {

    /**
     * 为用户授予一个角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void allocateUserRole(Integer userId, Integer roleId);
}
