package com.cgm.starter.account.controller;

import com.cgm.starter.account.service.ISysUserRoleService;
import com.cgm.starter.base.BaseController;
import com.cgm.starter.base.ResponseData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@Api(tags = "角色")
@RequestMapping("/api/{organizationId}/role")
public class SysRoleController extends BaseController {
    @Resource
    private ISysUserRoleService sysUserRoleService;

    @ApiOperation("查询当前用户以下的角色")
    @GetMapping("/below")
    public ResponseData listRolesBelow() {
        return new ResponseData(sysUserRoleService.listRolesBelow());
    }

    @ApiOperation("为用户分配角色")
    @PostMapping("/allocate")
    public ResponseData allocateRole(
            @PathVariable @ApiParam(value = "组织ID", example = "100001") Integer organizationId,
            Integer userId, String[] roleCodeList, boolean add) {
        sysUserRoleService.batchAssignUserRole(userId, roleCodeList, add);
        return new ResponseData();
    }
}
