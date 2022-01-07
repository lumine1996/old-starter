package com.cgm.starter.account.service;

import com.cgm.starter.account.dto.UserDetailDTO;
import com.cgm.starter.account.dto.UserInfoDTO;
import com.cgm.starter.account.dto.UserParamDTO;
import com.cgm.starter.account.entity.SysUser;
import com.github.pagehelper.PageInfo;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * @author cgm
 */
public interface ISysUserService extends UserDetailsService {
    /**
     * 根据用户ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    SysUser getById(Integer id);

    /**
     * 查询当前用户基本信息
     *
     * @return 用户基本信息
     */
    UserInfoDTO getMyInfo();

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser getByUsername(String username);

    /**
     * 查询用户列表
     *
     * @param userParamDTO 查询参数
     * @return 用户列表
     */
    PageInfo<UserDetailDTO> listUsers(UserParamDTO userParamDTO);

    /**
     * 根据用户名列表查询用户列表
     *
     * @param usernameList 用户名列表
     * @return 用户列表
     */
    List<UserInfoDTO> listUserByUsername(List<String> usernameList);

    /**
     * 创建用户
     *
     * @param newUser 用户
     * @return 用户
     */
    SysUser createUser(SysUser newUser);

    /**
     * 更新用户，系统内部使用
     *
     * @param user 用户
     */
    void sysUpdateUser(SysUser user);

    /**
     * 更新用户，部分关键字段不会更新
     *
     * @param user 用户
     * @return 用户
     */
    UserDetailDTO updateUser(UserDetailDTO user);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void deleteUserById(Integer id);

    /**
     * 启用用户
     *
     * @param id 用户ID
     */
    void enableUser(Integer id);

    /**
     * 禁用用户
     *
     * @param id 用户ID
     */
    void disableUser(Integer id);
}
