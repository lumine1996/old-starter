package com.cgm.starter.account.mapper;

import com.cgm.starter.account.entity.SysRole;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author cgm
 */
public interface SysRoleMapper extends Mapper<SysRole> {
    /**
     * 根据用户ID查询用户具有的角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> listRolesByUserId(Integer userId);

    /**
     * 根据角色编码查询角色ID
     *
     * @param code 角色编码
     * @return 角色ID
     */
    Integer getRoleIdByCode(String code);
}
