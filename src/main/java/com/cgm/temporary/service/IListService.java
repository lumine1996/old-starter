package com.cgm.temporary.service;

import com.cgm.temporary.vo.ClientSufferedTimes;
import com.cgm.temporary.vo.VirusTypeSummary;

import java.util.List;

/**
 * @author angel
 */
public interface IListService {

    /**
     * 病毒种类汇总
     *
     * @param pageSize 条数
     * @return 病毒种类数量列表
     */
    List<VirusTypeSummary> listVirusTypeSummary(Integer pageSize);

    /**
     * 客户端受攻击统计
     *
     * @param pageSize 条数
     * @return 客户端受攻击次数列表
     */
    List<ClientSufferedTimes> listClientSufferedTimes(Integer pageSize);
}
