package com.cgm.temporary.service.impl;

import com.cgm.temporary.constant.GlobalConstant;
import com.cgm.temporary.service.IListService;
import com.cgm.temporary.util.RandomContentUtils;
import com.cgm.temporary.vo.ClientSufferedTimes;
import com.cgm.temporary.vo.VirusTypeSummary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author angel
 */
@Service
public class ListServiceImpl implements IListService {
    private final Random rd = new Random();

    @Override
    public List<VirusTypeSummary> listVirusTypeSummary(Integer pageSize) {
        List<VirusTypeSummary> summaryList = new ArrayList<>();
        if (pageSize > GlobalConstant.VIRUS_MAIN_TYPES.length) {
            pageSize = GlobalConstant.VIRUS_MAIN_TYPES.length;
        }
        for (int i = 0; i < pageSize; i++) {
            // 从数组中随机取出
            String mainType = GlobalConstant.VIRUS_MAIN_TYPES[i];
            String secondName = GlobalConstant.VIRUS_SECOND_NAMES[rd.nextInt(GlobalConstant.VIRUS_SECOND_NAMES.length)];

            String lastName = String.valueOf((1000 + rd.nextInt(9000)));
            Integer amount = rd.nextInt(400);
            VirusTypeSummary summary = new VirusTypeSummary(mainType, secondName, lastName, amount);
            summaryList.add(summary);
        }
        return summaryList;
    }

    @Override
    public List<ClientSufferedTimes> listClientSufferedTimes(Integer pageSize) {
        List<ClientSufferedTimes> timesList = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            String workShopName = GlobalConstant.WORKSHOPS_2[rd.nextInt(GlobalConstant.WORKSHOPS_2.length)];
            String clientName = GlobalConstant.CLIENT_PREFIX[rd.nextInt(GlobalConstant.CLIENT_PREFIX.length)] + "-"
                    + RandomContentUtils.randomString(3, true, true, false);
            String clientIp = RandomContentUtils.randomIp();
            int times = (pageSize - i) * 10 + rd.nextInt(10);
            ClientSufferedTimes sufferedTimes = new ClientSufferedTimes(workShopName, clientName, clientIp, times);
            timesList.add(sufferedTimes);
        }
        return timesList;
    }

}
