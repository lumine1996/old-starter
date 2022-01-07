package com.cgm.starter.account.dto;

import com.cgm.starter.account.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author cgm
 */
@Data
@ApiModel("用户信息")
public class UserInfoDTO {
    @ApiModelProperty(value = "用户ID", hidden = true)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "组织ID")
    private Integer organizationId;

    @ApiModelProperty(value = "组织")
    private String organizationName;

    @ApiModelProperty(value = "介绍")
    private String introduction;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "角色")
    private String[] roles;

    @ApiModelProperty(value = "头像url")
    private String avatar;

    public UserInfoDTO() {
    }

    public UserInfoDTO(SysUser user) {
        this.username = user.getUsername();
        this.name = StringUtils.hasText(user.getNickname()) ? user.getNickname() : user.getName();
        this.organizationId = user.getOrganizationId();
        this.roles = user.rolesToArray();
        this.avatar = user.getAvatar();
    }
}
