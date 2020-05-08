package com.cgm.temporary.service;

import com.cgm.temporary.vo.MatrixChart;
import com.cgm.temporary.vo.PieChart;

import java.util.List;

/**
 * @author cgm
 */
public interface IChartService {

    /**
     * 病毒数量统计，按日和区域
     *
     * @param nodeAmount 节点数量
     * @return 病毒数量统计
     */
    MatrixChart countVirusByDay(Integer nodeAmount);

    /**
     * 病毒数量统计，按月和类型
     *
     * @param nodeAmount 节点数量
     * @return 病毒数量统计
     */
    MatrixChart countVirusByMonth(Integer nodeAmount);

    /**
     * 近期病毒处理排名
     *
     * @param nodeAmount 节点数量
     * @return 近期病毒处理排名
     */
    MatrixChart getRecentHandleRank(Integer nodeAmount);

    /**
     * 杀毒软件健康状况
     *
     * @return 健康状况
     */
    List<PieChart> getHealthCondition();
}
