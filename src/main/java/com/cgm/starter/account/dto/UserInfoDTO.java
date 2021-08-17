package com.cgm.starter.account.dto;

import com.cgm.starter.account.entity.SysUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

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

    @ApiModelProperty(value = "组织")
    private String organizationName;

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

    public UserInfoDTO() {
    }

    public UserInfoDTO(SysUser user) {
        this.username = user.getUsername();
        this.name = StringUtils.isEmpty(user.getNickname()) ? user.getName() : user.getNickname();
        this.roles = user.rolesToArray();
        this.avatar = user.getAvatar();

        this.createTime = user.getCreateTime();
        this.lastLoginTime = user.getLastLoginTime();
    }
}
