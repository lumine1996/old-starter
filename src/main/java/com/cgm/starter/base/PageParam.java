package com.cgm.starter.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页参数
 *
 * @author cgm
 */
@Data
public class PageParam {
    @ApiModelProperty(value = "页数", example = "1")
    private int page;

    @ApiModelProperty(value = "每页条数", example = "10")
    private int limit;

    @ApiModelProperty(value = "排序")
    private String sort;
}
