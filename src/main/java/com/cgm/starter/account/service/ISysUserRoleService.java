package com.cgm.starter.account.service;

import com.cgm.starter.account.entity.SysRole;

import java.util.List;

/**
 * @author cgm
 */
public interface ISysUserRoleService {
    /**
     * 查询当前用户以下的角色
     *
     * @return 当前用户以下的角色
     */
    List<SysRole> listRolesBelow();

    /**
     * 为用户授予一个角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void assignUserRole(Integer userId, Integer roleId, boolean add);

    /**
     * 为用户授予一个角色
     *
     * @param userId   用户ID
     * @param roleCode 角色编码
     */
    void assignUserRole(Integer userId, String roleCode, boolean add);

    /**
     * 为用户授予若干角色
     *
     * @param userId       用户ID
     * @param roleCodeList 角色编码列表
     */
    void batchAssignUserRole(Integer userId, String[] roleCodeList, boolean add);
}
