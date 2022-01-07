package com.cgm.starter.account.service.impl;

import com.cgm.starter.account.entity.SysRole;
import com.cgm.starter.account.entity.SysUser;
import com.cgm.starter.account.entity.SysUserRole;
import com.cgm.starter.account.mapper.SysRoleMapper;
import com.cgm.starter.account.mapper.SysUserMapper;
import com.cgm.starter.account.mapper.SysUserRoleMapper;
import com.cgm.starter.account.service.ISysUserRoleService;
import com.cgm.starter.base.ErrorCode;
import com.cgm.starter.util.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cgm
 */
@Service
public class SysUserRoleServiceImpl implements ISysUserRoleService {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysRole> listRolesBelow() {
        SysUser currentUser = UserUtils.getCurrentUser();
        return sysRoleMapper.listRoleBelow(UserUtils.getRoleLevel(currentUser));
    }

    @Override
    public void assignUserRole(Integer userId, Integer roleId, boolean add) {
        // 校验用户是否存在
        SysUser userInDatabase = sysUserMapper.selectByPrimaryKey(userId);
        Assert.isTrue(userInDatabase != null, ErrorCode.USER_NOT_EXIST);
        // 有角色且操作为增加, 没有角色且操作为移除, 已达预期结果, 直接返回
        SysUserRole sysUserRole = new SysUserRole(userId, roleId);
        List<SysUserRole> sysUserRoleInDatabase = sysUserRoleMapper.select(sysUserRole);
        boolean hasRole = !sysUserRoleInDatabase.isEmpty();
        if (hasRole == add) {
            return;
        }

        // 要分配的角色必须受当前用户控制
        SysUser currentUser = UserUtils.getCurrentUser();
        List<Integer> belowRoleIdList = sysRoleMapper.listRoleBelow(UserUtils.getRoleLevel(currentUser))
                .stream()
                .map(SysRole::getId)
                .collect(Collectors.toList());
        Assert.isTrue(!belowRoleIdList.isEmpty() && belowRoleIdList.contains(roleId),
                ErrorCode.USER_PERMISSION_DENIED);
        if (add) {
            sysUserRoleMapper.insertSelective(sysUserRole);
        } else {
            sysUserRoleMapper.delete(sysUserRole);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRole(Integer userId, String roleCode, boolean add) {
        Integer roleId = sysRoleMapper.getRoleIdByCode(roleCode);
        Assert.isTrue(roleId != null, ErrorCode.SYS_ROLE_NOT_EXIST);
        this.assignUserRole(userId, roleId, add);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchAssignUserRole(Integer userId, String[] roleCodeList, boolean add) {
        for (String roleCode : roleCodeList) {
            this.assignUserRole(userId, roleCode, add);
        }
    }
}
