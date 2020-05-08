package com.cgm.temporary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * @author cgm
 */
@ApiModel("饼图")
public class PieChart {
    /**
     * 图表标题
     */
    @ApiModelProperty("图表标题")
    private String title;


    /**
     * 图表副标题
     */
    @ApiModelProperty("图表副标题")
    private String subtext;

    /**
     * 数据
     */
    @ApiModelProperty("数据")
    private List<Map<String, Object>> data;

    public PieChart(String subtext, List<Map<String, Object>> data) {
        this.subtext = subtext;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
