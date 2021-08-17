package com.cgm.starter.account.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author cgm
 */
@Data
@NoArgsConstructor
@ApiModel("权限")
public class SysPermission {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "请求路径")
    private String path;

    @ApiModelProperty(value = "HTTP方法")
    private String method;

    @ApiModelProperty(value = "创建时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "描述")
    private String description;

    public SysPermission(String code) {
        this.code = code;
    }
}
