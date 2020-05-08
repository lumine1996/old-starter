package com.cgm.temporary.controller;

import com.cgm.temporary.service.IChartService;
import com.cgm.temporary.vo.MatrixChart;
import com.cgm.temporary.vo.PieChart;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author cgm
 */
@CrossOrigin
@RestController
@RequestMapping("/chart")
public class ChartController {
    @Resource
    private IChartService chartService;

    @GetMapping("/virus/quantity/area/day")
    @ApiOperation("病毒数量统计，按日和区域")
    @ApiImplicitParam(name = "nodeAmount", value = "节点数量", dataType = "int", required = true, example = "1")
    public ResponseEntity<MatrixChart> countVirusByDay(@RequestParam Integer nodeAmount) {
        MatrixChart matrixChart = chartService.countVirusByDay(nodeAmount);
        return new ResponseEntity<>(matrixChart, HttpStatus.OK);
    }

    @GetMapping("/virus/quantity/type/month")
    @ApiOperation("病毒数量统计，按月和类型")
    @ApiImplicitParam(name = "nodeAmount", value = "节点数量", dataType = "int", required = true, example = "1")
    public ResponseEntity<MatrixChart> countVirusByMonth(@RequestParam Integer nodeAmount) {
        MatrixChart matrixChart = chartService.countVirusByMonth(nodeAmount);
        return new ResponseEntity<>(matrixChart, HttpStatus.OK);
    }

    @GetMapping("/virus/handle/rank/recent")
    @ApiOperation("近期病毒处理排名")
    @ApiImplicitParam(name = "nodeAmount", value = "节点数量", dataType = "int", required = true, example = "5")
    public ResponseEntity<MatrixChart> getRecentHandleRank(@RequestParam Integer nodeAmount) {
        MatrixChart matrixChart = chartService.getRecentHandleRank(nodeAmount);
        return new ResponseEntity<>(matrixChart, HttpStatus.OK);
    }

    @GetMapping("/anti-virus/health/condition")
    @ApiOperation("杀毒软件健康状况")
    public ResponseEntity<List<PieChart>> getHealthCondition() {
        List<PieChart> pieChartList = chartService.getHealthCondition();
        return new ResponseEntity<>(pieChartList, HttpStatus.OK);
    }
}