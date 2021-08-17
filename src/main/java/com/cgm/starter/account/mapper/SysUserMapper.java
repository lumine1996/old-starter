package com.cgm.starter.account.mapper;

import com.cgm.starter.account.dto.UserInfoDTO;
import com.cgm.starter.account.dto.UserParamDTO;
import com.cgm.starter.account.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author cgm
 */
public interface SysUserMapper extends Mapper<SysUser> {
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser selectByUsername(String username);

    /**
     * 查询用户列表
     *
     * @param userParamDTO 查询参数
     * @return 用户列表
     */
    List<UserInfoDTO> listUsers(@Param("paramDTO") UserParamDTO userParamDTO);

    /**
     * 根据用户名列表查询用户列表
     *
     * @param organizationId 组织ID
     * @param usernameList   用户名列表
     * @return 用户列表
     */
    List<UserInfoDTO> listUserByUsername(@Param("organizationId") Integer organizationId,
                                     @Param("usernameList") List<String> usernameList);
}
