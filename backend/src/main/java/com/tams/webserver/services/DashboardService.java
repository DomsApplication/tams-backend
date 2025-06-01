package com.tams.webserver.services;

import com.tams.webserver.api.webmodels.DashboardDeviceLogTableResponse;
import com.tams.webserver.api.webmodels.DashboardTimeLogTableResponse;
import com.tams.webserver.api.webmodels.DashboardWidgetsResponse;
import com.tams.webserver.datasource.repository.DeviceLogRepository;
import com.tams.webserver.datasource.repository.TimeLogRepository;
import com.tams.webserver.datasource.repository.UserInfoRepository;
import com.tams.webserver.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DashboardService {

    private UserInfoRepository userInfoRepository;

    private DeviceLogRepository deviceLogRepository;

    private TimeLogRepository timeLogRepository;

    public List<DashboardWidgetsResponse> getWidgetsData() {
        Object[] result = userInfoRepository.findDashboardWidgetsData();
        Object[] dataRow = (Object[]) result[0];
        for (Object obj : dataRow) {
            System.out.println("Type: " + (obj != null ? obj.getClass().getName() : "null") + ", Value: " + obj);
        }
        List<DashboardWidgetsResponse> widgetLis = new ArrayList<>();

        DashboardWidgetsResponse dashboardWidgetsResponse = DashboardWidgetsResponse.builder()
                .title("Devices Count")
                .description("Lising all registred device count.")
                .count(String.valueOf(dataRow[1]))
                .build();
        widgetLis.add(dashboardWidgetsResponse);

        DashboardWidgetsResponse dashboardWidgetsResponse1 = DashboardWidgetsResponse.builder()
                .title("Users Count")
                .description("Lising all registred user count.")
                .count(String.valueOf(dataRow[0]))
                .build();
        widgetLis.add(dashboardWidgetsResponse1);

        DashboardWidgetsResponse dashboardWidgetsResponse2 = DashboardWidgetsResponse.builder()
                .title("Active Devices Count")
                .description("It will provide the currnet active devices count.")
                .count(String.valueOf(dataRow[2]))
                .build();
        widgetLis.add(dashboardWidgetsResponse2);

        DashboardWidgetsResponse dashboardWidgetsResponse3 = DashboardWidgetsResponse.builder()
                .title("Latest Time log Count")
                .description("It will list number of department added.")
                .count(String.valueOf(dataRow[3]))
                .build();
        widgetLis.add(dashboardWidgetsResponse3);

        return widgetLis;
    }

    public List<DashboardTimeLogTableResponse> getDashboardTimeLogData() {
        List<Object[]> rawResults = timeLogRepository.getDashboardTimeLogTableData();
        List<DashboardTimeLogTableResponse> resultList = new ArrayList<>();
        for (Object[] result : rawResults) {
            DashboardTimeLogTableResponse dashboardTimeLogTableResponse = DashboardTimeLogTableResponse.builder().deviceSerialNo((String) result[0]).userId((String) result[1]).time(Utilities.formatLocalDateTimeToUTCString(((Timestamp) result[2]).toLocalDateTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z)).build();
            resultList.add(dashboardTimeLogTableResponse);
        }
        return resultList;
    }

    public List<DashboardDeviceLogTableResponse> getDashboardDeviceLogData() {
        List<Object[]> rawResults = deviceLogRepository.getDashboardDeviceLogTableData();
        List<DashboardDeviceLogTableResponse> resultList = new ArrayList<>();
        for (Object[] result : rawResults) {
            DashboardDeviceLogTableResponse dashboardDeviceLogTableResponse = DashboardDeviceLogTableResponse.builder().deviceSerialNo((String) result[0]).deviceTime(Utilities.formatLocalDateTimeToUTCString(((Timestamp) result[1]).toLocalDateTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z)).productName((String) result[2]).build();
            resultList.add(dashboardDeviceLogTableResponse);
        }
        return resultList;
    }


}
