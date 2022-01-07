package com.cgm.starter.account.dto;

import com.cgm.starter.account.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author cgm
 */
@Data
@ApiModel("用户信息")
public class UserDetailDTO {
    @ApiModelProperty(value = "用户ID", example = "10000001")
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "组织")
    private String organizationName;

    @ApiModelProperty(value = "组织ID", hidden = true)
    private Integer organizationId;

    @ApiModelProperty(value = "介绍")
    private String introduction;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "角色")
    private String[] roles;

    @ApiModelProperty(value = "头像url")
    private String avatar;

    @ApiModelProperty(value = "是否有效", example = "true")
    private Boolean enabled;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "最后登录时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date lastLoginTime;

    public UserDetailDTO() {
    }

    public UserDetailDTO(SysUser user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = !StringUtils.hasText(user.getNickname()) ? user.getName() : user.getNickname();
        this.roles = user.rolesToArray();
        this.avatar = user.getAvatar();
        this.organizationId = user.getOrganizationId();

        this.createTime = user.getCreateTime();
        this.lastLoginTime = user.getLastLoginTime();
    }

    public void addRole(List<UserRoleDTO> userRoleList) {
        this.roles = userRoleList.stream().map(UserRoleDTO::getRoleCode).toArray(String[]::new);
    }
}