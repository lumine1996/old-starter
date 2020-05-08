package com.cgm.temporary.controller;

import com.cgm.temporary.service.IListService;
import com.cgm.temporary.vo.ClientSufferedTimes;
import com.cgm.temporary.vo.VirusTypeSummary;
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
@RequestMapping("/list")
public class ListController {
    @Resource
    private IListService listService;

    @GetMapping("/virus/type/summary")
    @ApiOperation("病毒种类数量列表")
    @ApiImplicitParam(name = "pageSize", value = "条数", dataType = "int", required = true, example = "5")
    public ResponseEntity<List<VirusTypeSummary>> listVirusTypeSummary (@RequestParam Integer pageSize) {
        List<VirusTypeSummary> summaryList = listService.listVirusTypeSummary(pageSize);
        return new ResponseEntity<>(summaryList, HttpStatus.OK);
    }

    @GetMapping("/client/suffered/times")
    @ApiOperation("客户端受攻击次数列表")
    @ApiImplicitParam(name = "pageSize", value = "条数", dataType = "int", required = true, example = "5")
    public ResponseEntity<List<ClientSufferedTimes>> listClientSufferedTimes (@RequestParam Integer pageSize) {
        List<ClientSufferedTimes> typeSummaries = listService.listClientSufferedTimes(pageSize);
        return new ResponseEntity<>(typeSummaries, HttpStatus.OK);
    }
}