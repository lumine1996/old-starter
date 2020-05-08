package com.cgm.temporary.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author cgm
 */
@ApiModel("终端受攻击统计")
public class ClientSufferedTimes {

    /**
     * 车间名称
     */
    @ApiModelProperty("车间名称")
    private String workshopName;

    /**
     * 终端名称
     */
    @ApiModelProperty("终端名称")
    private String clientName;

    /**
     * 终端IP
     */
    @ApiModelProperty("终端IP")
    private String clientIp;

    /**
     * 受攻击次数
     */
    @ApiModelProperty(value = "受攻击次数", example = "1000")
    private Integer sufferedTimes;

    public String getClientName() {
        return clientName;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getSufferedTimes() {
        return sufferedTimes;
    }

    public void setSufferedTimes(Integer sufferedTimes) {
        this.sufferedTimes = sufferedTimes;
    }

    public ClientSufferedTimes(String workshopName, String clientName, String clientIp, Integer sufferedTimes) {
        this.workshopName = workshopName;
        this.clientName = clientName;
        this.clientIp = clientIp;
        this.sufferedTimes = sufferedTimes;
    }
}
