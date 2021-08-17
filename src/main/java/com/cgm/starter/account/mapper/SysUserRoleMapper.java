package com.cgm.starter.account.mapper;

import com.cgm.starter.account.entity.SysUserRole;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author cgm
 */
public interface SysUserRoleMapper extends Mapper<SysUserRole> {

    /**
     * 为用户授予一个角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void allocateUserRole(Integer userId, Integer roleId);
}
