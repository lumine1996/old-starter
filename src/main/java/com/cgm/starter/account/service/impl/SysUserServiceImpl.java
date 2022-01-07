package com.cgm.starter.account.service.impl;

import com.cgm.starter.account.dto.UserDetailDTO;
import com.cgm.starter.account.dto.UserInfoDTO;
import com.cgm.starter.account.dto.UserParamDTO;
import com.cgm.starter.account.dto.UserRoleDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cgm
 */
@Service
@Slf4j
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
    public UserInfoDTO getMyInfo() {
        // token已验证并存储了用户信息，此处可以直接获取
        SysUser user = UserUtils.getCurrentUser();
        UserInfoDTO userInfoDTO = new UserInfoDTO(user);
        if (!StringUtils.hasText(user.getAvatar())) {
            userInfoDTO.setAvatar(extraConfig.getDefaultAvatar());
        }
        return userInfoDTO;
    }

    @Override
    public PageInfo<UserDetailDTO> listUsers(UserParamDTO userParamDTO) {
        if (UserUtils.isSystemAdmin()) {
            userParamDTO.setOrganizationId(null);
        }

        PageMethod.startPage(userParamDTO.getPage(), userParamDTO.getLimit());
        List<UserDetailDTO> userList = sysUserMapper.listUsers(userParamDTO);

        // 查询用户角色并匹配
        List<Integer> userIdList = userList.stream().map(UserDetailDTO::getId).collect(Collectors.toList());
        List<UserRoleDTO> userRoleList = sysRoleMapper.listRoleByUserIdList(userIdList);
        userList.forEach(user -> user.addRole(userRoleList.stream()
                .filter(userRole -> user.getId().equals(userRole.getUserId()))
                .collect(Collectors.toList()))
        );
        return new PageInfo<>(userList);
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
        Assert.isTrue(UserUtils.getRoleLevel(currentUser) > 1, ErrorCode.USER_PERMISSION_DENIED);
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
        sysUserRoleService.assignUserRole(newUser.getId(), userRoleId, true);

        return newUser;
    }

    @Override
    public void sysUpdateUser(SysUser user) {
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetailDTO updateUser(UserDetailDTO dto) {
        SysUser targetUser = new SysUser(dto);
        List<SysRole> originRoleList = sysRoleMapper.listRoleByUserId(targetUser.getId());
        SysUser currentUser = UserUtils.getCurrentUser();
        // 暂时允许同级或更高级用户操作, 后续增加自更新接口
        Assert.isTrue(UserUtils.getRoleLevel(currentUser) >= UserUtils.getRoleLevel(originRoleList),
                ErrorCode.USER_PERMISSION_DENIED);

        // 调整角色
        String[] originRoles = originRoleList.stream().map(SysRole::getCode).toArray(String[]::new);
        String[] addRoles = Arrays.stream(dto.getRoles()).filter(target ->
                !Arrays.asList(originRoles).contains(target)).toArray(String[]::new);
        String[] removeRoles = Arrays.stream(originRoles).filter(target ->
                !Arrays.asList(dto.getRoles()).contains(target)).toArray(String[]::new);
        sysUserRoleService.batchAssignUserRole(dto.getId(), addRoles, true);
        sysUserRoleService.batchAssignUserRole(dto.getId(), removeRoles, false);

        targetUser.cleanNotUpdateFields();
        if (!StringUtils.hasText(targetUser.getPassword())) {
            targetUser.setPassword(null);
        } else {
            targetUser.setPassword(new BCryptPasswordEncoder().encode(targetUser.getPassword()));
        }
        targetUser.setUpdatedBy(currentUser.getId());
        sysUserMapper.updateByPrimaryKeySelective(targetUser);
        return dto;
    }

    @Override
    public void deleteUserById(Integer id) {
        SysUser targetUser = this.getById(id);
        targetUser.setRoles(this.getRolesByUserId(id));
        SysUser currentUser = UserUtils.getCurrentUser();

        // 仅允许更高一级的用户操作
        Assert.isTrue(UserUtils.getRoleLevel(currentUser) > UserUtils.getRoleLevel(targetUser),
                ErrorCode.USER_PERMISSION_DENIED);

        sysUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void enableUser(Integer id) {
        SysUser targetUser = this.getById(id);
        targetUser.setRoles(this.getRolesByUserId(id));
        SysUser currentUser = UserUtils.getCurrentUser();

        // 仅允许更高一级的用户操作
        Assert.isTrue(UserUtils.getRoleLevel(currentUser) > UserUtils.getRoleLevel(targetUser),
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
        if (UserUtils.getRoleLevel(currentUser) <= UserUtils.getRoleLevel(targetUser)) {
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
            throw new UsernameNotFoundException("Username Not Found!");
        }

        user.setRoles(this.getRolesByUserId(user.getId()));
        return user;
    }

    private List<SysRole> getRolesByUserId(Integer id) {
        List<SysRole> roleList;

        // 从数据库查询
        roleList = sysRoleMapper.listRoleByUserId(id);
        this.addDefaultRole(roleList);
        return roleList;
    }

    private void addDefaultRole(List<SysRole> roleList) {
        // 默认赋予"USER"角色, 对正常创建的用户无影响
        boolean hasRoleUser = false;
        for (SysRole role : roleList) {
            if (Constant.ROLE_USER.equals(role.getCode())) {
                hasRoleUser = true;
                break;
            }
        }
        if (!hasRoleUser) {
            roleList.add(new SysRole(Constant.ROLE_USER));
        }
    }
}
