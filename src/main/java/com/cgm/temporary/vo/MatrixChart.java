package com.cgm.temporary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author cgm
 */
@ApiModel("矩阵类图表")
public class MatrixChart {
    /**
     * 图表标题
     */
    @ApiModelProperty("图表标题")
    private String title;

    /**
     * x轴刻度标签
     */
    @ApiModelProperty("x轴刻度标签")
    private String[] axisLabelsX;

    /**
     * y轴刻度标签
     */
    @ApiModelProperty("y轴刻度标签")
    private String[] axisLabelsY;

    /**
     * 系列
     */
    @ApiModelProperty("系列")
    private String[] series;

    /**
     * 数据，长度与系列一致
     */
    @ApiModelProperty("数据")
    private Integer[][] dataArray;

    public MatrixChart(String[] series, String[] axisLabelsX, Integer[][] dataArray) {
        this.series = series;
        this.axisLabelsX = axisLabelsX;
        this.dataArray = dataArray;
    }

    public MatrixChart(String title, String[] axisLabelsX, String[] axisLabelsY, String[] series, Integer[][] dataArray) {
        this.title = title;
        this.axisLabelsX = axisLabelsX;
        this.axisLabelsY = axisLabelsY;
        this.series = series;
        this.dataArray = dataArray;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAxisLabelsX() {
        return axisLabelsX;
    }

    public void setAxisLabelsX(String[] axisLabelsX) {
        this.axisLabelsX = axisLabelsX;
    }

    public String[] getAxisLabelsY() {
        return axisLabelsY;
    }

    public void setAxisLabelsY(String[] axisLabelsY) {
        this.axisLabelsY = axisLabelsY;
    }

    public String[] getSeries() {
        return series;
    }

    public void setSeries(String[] series) {
        this.series = series;
    }

    public Integer[][] getDataArray() {
        return dataArray;
    }

    public void setDataArray(Integer[][] dataArray) {
        this.dataArray = dataArray;
    }
}
