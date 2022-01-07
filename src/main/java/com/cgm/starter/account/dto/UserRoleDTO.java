package com.cgm.starter.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cgm
 */
@Data
@ApiModel("用户角色")
public class UserRoleDTO {
    @ApiModelProperty(value = "用户ID", example = "100000001")
    private Integer userId;

    @ApiModelProperty(value = "角色ID", example = "100001")
    private Integer roleId;

    @ApiModelProperty(value = "角色编码")
    private String roleCode;

    @ApiModelProperty(value = "角色名称")
    private String roleName;
}
