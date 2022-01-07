package com.cgm.starter.account.dto;

import com.cgm.starter.base.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cgm
 */
@Data
@ApiModel("组织查询参数")
@EqualsAndHashCode(of = {"name"}, callSuper = false)
public class OrganizationParamDTO extends PageParam {
    @ApiModelProperty(value = "组织名称(全称/简称/英文名称)")
    private String name;
}
