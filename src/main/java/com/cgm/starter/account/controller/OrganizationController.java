package com.cgm.starter.account.controller;

import com.cgm.starter.account.dto.OrganizationParamDTO;
import com.cgm.starter.account.entity.Organization;
import com.cgm.starter.account.service.IOrganizationService;
import com.cgm.starter.annotation.Isolation;
import com.cgm.starter.annotation.IsolationPolicy;
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
@Api(tags = "组织")
@RequestMapping("/api/organization")
public class OrganizationController {
    @Resource
    private IOrganizationService organizationService;

    @ApiOperation("查询组织列表")
    @Isolation(IsolationPolicy.SYS_ADMIN_ONLY)
    @GetMapping
    public ResponseData listOrganization(@ApiParam(value = "查询参数") OrganizationParamDTO paramDTO) {
        return new ResponseData(organizationService.listOrganization(paramDTO));
    }

    @ApiOperation("添加组织")
    @Isolation(IsolationPolicy.SYS_ADMIN_ONLY)
    @PostMapping
    public ResponseData addOrganization(
            @ApiParam(value = "组织", required = true) @RequestBody Organization organization) {
        organizationService.addOrganization(organization);
        return new ResponseData(organization);
    }

    @ApiOperation("更新组织")
    @Isolation(IsolationPolicy.SYS_ADMIN_ONLY)
    @PutMapping
    public ResponseData updateOrganization(
            @ApiParam(value = "组织", required = true) @RequestBody Organization organization) {
        organizationService.updateOrganization(organization);
        return new ResponseData(organization);
    }
}
