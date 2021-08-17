package com.cgm.starter.account.controller;

import com.cgm.starter.account.dto.UserParamDTO;
import com.cgm.starter.account.entity.SysUser;
import com.cgm.starter.account.service.ISysUserService;
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
@Api(tags = "用户")
@RequestMapping("/api/user")
public class SysUserController extends BaseController {
    @Resource
    private ISysUserService sysUserService;

    @ApiOperation("根据ID查询用户")
    @GetMapping("/{id}")
    public ResponseData findById(
            @ApiParam(value = "用户ID", required = true, example = "10000001") @PathVariable Integer id) {
        return new ResponseData(sysUserService.getById(id));
    }

    @ApiOperation("根据token查询用户")
    @GetMapping("/info")
    public ResponseData getInfoByToken(@ApiParam(value = "token") String token) {
        return new ResponseData(sysUserService.getInfoByToken(token));
    }

    @ApiOperation("查询用户列表")
    @GetMapping
    public ResponseData listUsers(@ApiParam(value = "查询参数") UserParamDTO userParamDTO) {
        return new ResponseData(sysUserService.listUsers(userParamDTO));
    }

    @ApiOperation("创建用户")
    @PostMapping
    public ResponseData createUser(@ApiParam(value = "用户") @RequestBody SysUser user) {
        return new ResponseData(sysUserService.createUser(user));
    }

    @ApiOperation("更新用户")
    @PutMapping
    public ResponseData updateUser(@ApiParam(value = "用户") @RequestBody SysUser user) {
        return new ResponseData(sysUserService.updateUser(user));
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public ResponseData deleteUser(
            @ApiParam(value = "用户ID", required = true, example = "10000001") @PathVariable Integer id) {
        sysUserService.deleteUserById(id);
        return new ResponseData();
    }

    @ApiOperation("启用用户")
    @PutMapping("/enable/{id}")
    public ResponseData enableUser(
            @ApiParam(value = "用户ID", required = true, example = "10000001") @PathVariable Integer id) {
        sysUserService.enableUser(id);
        return new ResponseData();
    }

    @ApiOperation("禁用用户")
    @PutMapping("/disable/{id}")
    public ResponseData disableUser(
            @ApiParam(value = "用户ID", required = true, example = "10000001") @PathVariable Integer id) {
        sysUserService.disableUser(id);
        return new ResponseData();
    }
}
