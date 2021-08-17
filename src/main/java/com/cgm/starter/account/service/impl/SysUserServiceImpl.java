package com.cgm.starter.account.service.impl;

import com.cgm.starter.account.dto.UserInfoDTO;
import com.cgm.starter.account.dto.UserParamDTO;
import com.cgm.starter.account.entity.SysRole;
import com.cgm.starter.account.entity.SysUser;
import com.cgm.starter.account.mapper.SysRoleMapper;
import com.cgm.starter.account.mapper.SysUserMapper;
import com.cgm.starter.account.service.ISysUserRoleService;
import com.cgm.starter.account.service.ISysUserService;
import com.cgm.starter.base.BaseException;
import com.cgm.starter.base.Constant;
import com.cgm.starter.base.ErrorCode;
import com.cgm.starter.config.ExtraConfig;
import com.cgm.starter.util.UserUtils;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author cgm
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Resource
    private ISysUserRoleService sysUserRoleService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private ExtraConfig extraConfig;

    @Override
    public SysUser getById(Integer id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public UserInfoDTO getInfoByToken(String token) {
        // token已验证并存储了用户信息，此处可以直接获取
        SysUser user = UserUtils.getCurrentUser();
        UserInfoDTO userInfoDTO = new UserInfoDTO(user);
        if (StringUtils.isEmpty(user.getAvatar())) {
            userInfoDTO.setAvatar(extraConfig.getDefaultAvatar());
        }
        return userInfoDTO;
    }

    @Override
    public PageInfo<UserInfoDTO> listUsers(UserParamDTO userParamDTO) {
        SysUser user = UserUtils.getCurrentUser();
        if (!user.hasRole(Constant.ROLE_SYSTEM_ADMIN)) {
            userParamDTO.setOrganizationId(user.getOrganizationId());
        }

        PageMethod.startPage(userParamDTO.getPage(), userParamDTO.getLimit());
        List<UserInfoDTO> listUsers = sysUserMapper.listUsers(userParamDTO);
        return new PageInfo<>(listUsers);
    }

    @Override
    public List<UserInfoDTO> listUserByUsername(List<String> usernameList) {
        if (usernameList == null || usernameList.isEmpty()) {
            return new ArrayList<>();
        }
        return sysUserMapper.listUserByUsername(UserUtils.getCurrentUserOrgId(), usernameList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser createUser(SysUser newUser) {
        // 仅允许超管/组织管理员创建用户
        SysUser currentUser = UserUtils.getCurrentUser();
        Assert.isTrue(getPermissionLevel(currentUser) > 1, ErrorCode.USER_PERMISSION_DENIED);
        // 组织ID默认和当前用户一致, 超管可以指定其他组织, 不能和超管一致
        boolean isSystemAdmin = currentUser.hasRole(Constant.ROLE_SYSTEM_ADMIN);
        if (!isSystemAdmin || newUser.getOrganizationId() == null) {
            newUser.setOrganizationId(currentUser.getOrganizationId());
        }
        if (isSystemAdmin && currentUser.getOrganizationId().equals(newUser.getOrganizationId())) {
            throw new BaseException(ErrorCode.USER_ORGANIZATION_INVALID);
        }
        newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        sysUserMapper.insertSelective(newUser);
        Integer userRoleId = sysRoleMapper.getRoleIdByCode(Constant.ROLE_USER);
        sysUserRoleService.allocateUserRole(newUser.getId(), userRoleId);
        return newUser;
    }

    @Override
    public void sysUpdateUser(SysUser user) {
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public SysUser updateUser(SysUser user) {
        SysUser currentUser = UserUtils.getCurrentUser();
        // 暂时允许同级或更高级用户操作, 后续增加自更新接口
        Assert.isTrue(getPermissionLevel(currentUser) >= getPermissionLevel(user),
                ErrorCode.USER_PERMISSION_DENIED);
        user.mask();
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        user.setUpdatedBy(currentUser.getId());
        sysUserMapper.updateByPrimaryKeySelective(user);
        return user;
    }

    @Override
    public void deleteUserById(Integer id) {
        SysUser targetUser = this.getById(id);
        targetUser.setRoles(this.getRolesByUserId(id));
        SysUser currentUser = UserUtils.getCurrentUser();

        // 仅允许更高一级的用户操作
        Assert.isTrue(getPermissionLevel(currentUser) > getPermissionLevel(targetUser),
                ErrorCode.USER_PERMISSION_DENIED);

        sysUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void enableUser(Integer id) {
        SysUser targetUser = this.getById(id);
        targetUser.setRoles(this.getRolesByUserId(id));
        SysUser currentUser = UserUtils.getCurrentUser();

        // 仅允许更高一级的用户操作
        Assert.isTrue(getPermissionLevel(currentUser) > getPermissionLevel(targetUser),
                ErrorCode.USER_PERMISSION_DENIED);

        targetUser.setEnable(true);
        targetUser.setUpdatedBy(currentUser.getId());
        sysUserMapper.updateByPrimaryKeySelective(targetUser);
    }

    @Override
    public void disableUser(Integer id) {
        SysUser targetUser = this.getById(id);
        targetUser.setRoles(this.getRolesByUserId(id));
        SysUser currentUser = UserUtils.getCurrentUser();

        // 仅允许更高一级的用户操作
        if (getPermissionLevel(currentUser) <= getPermissionLevel(targetUser)) {
            throw new BaseException(ErrorCode.USER_PERMISSION_DENIED);
        }

        targetUser.setEnable(false);
        targetUser.setUpdatedBy(currentUser.getId());
        sysUserMapper.updateByPrimaryKeySelective(targetUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser user = this.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在!");
        }

        user.setRoles(getRolesByUserId(user.getId()));
        return user;
    }

    private List<SysRole> getRolesByUserId(Integer id) {
        // 查询用户角色，查不到的默认赋予"USER"角色
        List<SysRole> roleList = sysRoleMapper.listRolesByUserId(id);
        roleList = roleList.isEmpty() ? Collections.singletonList(new SysRole(Constant.ROLE_USER)) : roleList;
        return roleList;
    }

    private Integer getPermissionLevel(SysUser user) {
        // 超管 > 组织管理员 > 普通用户，0为未注册用户预留
        if (user.hasRole(Constant.ROLE_SYSTEM_ADMIN)) {
            return 3;
        }
        if (user.hasRole(Constant.ROLE_ORG_ADMIN)) {
            return 2;
        }
        return 1;
    }
}
