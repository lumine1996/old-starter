package com.cgm.starter.account.mapper;

import com.cgm.starter.account.dto.UserRoleDTO;
import com.cgm.starter.account.entity.SysRole;
import org.apache.ibatis.annotations.Param;
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
    List<SysRole> listRoleByUserId(Integer userId);

    /**
     * 根据用户ID列表查询具有的角色
     *
     * @param userIdList 用户ID列表
     * @return 具有的角色
     */
    List<UserRoleDTO> listRoleByUserIdList(@Param("userIdList") List<Integer> userIdList);

    /**
     * 根据角色编码数组查询角色
     *
     * @param codes 角色编码数组
     * @return 角色ID
     */
    List<SysRole> listRoleByCodeArray(@Param("codes") String[] codes);

    /**
     * 根据角色编码查询角色ID
     *
     * @param code 角色编码
     * @return 角色ID
     */
    Integer getRoleIdByCode(String code);

    /**
     * 查询指定级别以下的角色
     *
     * @param level 角色
     * @return 角色列表
     */
    List<SysRole> listRoleBelow(Integer level);
}
