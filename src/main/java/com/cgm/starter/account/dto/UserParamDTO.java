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
@ApiModel("用户查询参数")
@EqualsAndHashCode(of = {"name", "organizationId"}, callSuper = false)
public class UserParamDTO extends PageParam {
    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "组织ID", example = "100009")
    private Integer organizationId;

    @ApiModelProperty(value = "组织")
    private String organizationName;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

}
