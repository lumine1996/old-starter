package com.cgm.starter.account.mapper;

import com.cgm.starter.account.entity.SysPermission;

import java.util.List;

/**
 * @author cgm
 */
public interface SysPermissionMapper {
    /**
     * 查询指定http方法的所有权限
     *
     * @param httpMethod http方法
     * @return 权限列表
     */
    List<SysPermission> listPermissionsByMethod(String httpMethod);

    /**
     * 根据权限id列出具有该权限的角色
     *
     * @param permissionId 权限id
     * @return 角色代码列表
     */
    List<String> listRolesByPermissionId(int permissionId);
}
