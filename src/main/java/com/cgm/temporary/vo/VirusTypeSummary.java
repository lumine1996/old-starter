package com.cgm.temporary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author cgm
 */
@ApiModel("病毒种类汇总")
public class VirusTypeSummary {
    /**
     * 主要类型
     */
    @ApiModelProperty("主要类型")
    private String mainType;

    /**
     * 名称第二段
     */
    @ApiModelProperty("名称第二段")
    private String secondName;

    /**
     * 名称末段
     */
    @ApiModelProperty("名称末段")
    private String lastName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量", example = "1000")
    private Integer amount;

    public VirusTypeSummary(String mainType, String secondName, String lastName, Integer amount) {
        this.mainType = mainType;
        this.secondName = secondName;
        this.lastName = lastName;
        this.amount = amount;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}