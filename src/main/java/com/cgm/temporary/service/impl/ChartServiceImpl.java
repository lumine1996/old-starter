package com.cgm.temporary.service.impl;

import com.cgm.temporary.constant.GlobalConstant;
import com.cgm.temporary.service.IChartService;
import com.cgm.temporary.util.RandomContentUtils;
import com.cgm.temporary.vo.MatrixChart;
import com.cgm.temporary.vo.PieChart;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author cgm
 */
@Service
public class ChartServiceImpl implements IChartService {
    private final Random rd = new Random();

    @Override
    public MatrixChart countVirusByDay(Integer nodeAmount) {
        if (nodeAmount <= 1) {
            return randomChartNode(GlobalConstant.WORKSHOPS_1);
        }
        return randomChart(GlobalConstant.WORKSHOPS_1, nodeAmount, "DATE");
    }

    @Override
    public MatrixChart countVirusByMonth(Integer nodeAmount) {
        if (nodeAmount <= 1) {
            return randomChartNode(GlobalConstant.VIRUS_MAIN_TYPES);
        }
        return randomChart(GlobalConstant.VIRUS_MAIN_TYPES, nodeAmount, "MONTH");
    }

    @Override
    public MatrixChart getRecentHandleRank(Integer nodeAmount) {
        return randomChart(GlobalConstant.WORKSHOPS_2, nodeAmount, "SERIES");
    }

    @Override
    public List<PieChart> getHealthCondition() {
        List<PieChart> pieChartList = new ArrayList<>();
        for (String subtext : GlobalConstant.HEALTH_CONDITION_CATEGORY) {
            // 左右两个饼图的亮部/暗部数据位置相反
            List<Map<String, Object>> data1 = new ArrayList<>();
            List<Map<String, Object>> data2 = new ArrayList<>();

            Map<String, Object> unit1 = new HashMap<>(2);
            unit1.put("value", 100 + rd.nextInt(300));
            Map<String, Object> unit2 = new HashMap<>(2);
            unit2.put("value", 100 + rd.nextInt(300));

            data1.add(unit1);
            data1.add(unit2);
            data2.add(unit2);
            data2.add(unit1);
            pieChartList.add(new PieChart(subtext, data1));
            pieChartList.add(new PieChart(subtext, data2));
        }
        return pieChartList;
    }

    /**
     * 生成随机图表，限制了波动范围
     *
     * @param series     系列
     * @param nodeAmount x轴上的节点数量
     * @return 图表
     */
    private MatrixChart randomChart(String[] series, Integer nodeAmount, String labelType) {
        if (nodeAmount <= 1) {
            return randomChartNode(series);
        }

        Integer[][] dataArray = new Integer[series.length][nodeAmount];
        String[] labels;
        switch (labelType) {
            case "MONTH" :
                labels = RandomContentUtils.fakeDates(nodeAmount, 1, Calendar.MONTH);
                break;
            case "SERIES" :
                labels = series;
                break;
            default:
                labels = RandomContentUtils.fakeDates(nodeAmount, 5, Calendar.DATE);
        }

        for (int i = 0; i < series.length; i++) {
            dataArray[i][0] = rd.nextInt(1000);
            for (int j = 1; j < nodeAmount; j++) {
                dataArray[i][j] = dataArray[i][j - 1] + 200 - rd.nextInt(400);
                if (dataArray[i][j] < 0) {
                    dataArray[i][j] *= -1;
                }
            }
        }
        return new MatrixChart(series, labels, dataArray);
    }

    /**
     * 生成随机图表节点，用于追加到图表右侧
     *
     * @param series 系列
     * @return 仅有一个最新节点的图表
     */
    private MatrixChart randomChartNode(String[] series) {
        String[] labels = {RandomContentUtils.fakeDate()};
        Integer[][] dataArray = new Integer[series.length][1];
        for (int i = 0; i < series.length; i++) {
            dataArray[i][0] = 300 + rd.nextInt(700);
        }
        return new MatrixChart(series, labels, dataArray);
    }

}
