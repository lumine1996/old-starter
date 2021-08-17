package com.cgm.starter.account.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户实体类
 *
 * @author cgm
 */
@Data
@NoArgsConstructor
@ApiModel("用户")
public class SysUser implements UserDetails, Serializable {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @ApiModelProperty(value = "组织ID", hidden = true)
    private Integer organizationId;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码", hidden = true)
    private String password;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    /**
     * 字段定为enable是为了防止和lombok注解产生冲突
     */
    @Column(name = "is_enabled")
    @JsonProperty("enabled")
    private Boolean enable;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "最后登录时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date lastLoginTime;

    @ApiModelProperty(value = "更新者", hidden = true)
    private Integer updatedBy;

    @ApiModelProperty(value = "显存上限(G)", example = "2")
    private String avatar;

    @Transient
    @ApiModelProperty(value = "角色列表", hidden = true)
    private transient List<SysRole> roles;

    @Override
    public boolean isEnabled() {
        return enable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysUser user = (SysUser) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 判断用户是否具有指定的角色
     *
     * @param roleCode 角色编码
     * @return 是否具有
     */
    public boolean hasRole(String roleCode) {
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        for (SysRole role : roles) {
            if (roleCode.equals(role.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回角色字符串数组
     *
     * @return 角色
     */
    public String[] rolesToArray() {
        String[] roleArray = {};
        if (roles.isEmpty()) {
            return roleArray;
        }
        return roles.stream().map(SysRole::getCode).collect(Collectors.toList()).toArray(roleArray);
    }

    /**
     * 数据脱敏
     */
    public void mask() {
        this.username = null;
        this.lastLoginTime = null;
        this.updateTime = null;
    }
}
