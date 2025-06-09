package com.tams.webserver.services;

import com.tams.webserver.api.webmodels.TimeLogResponse;
import com.tams.webserver.api.webmodels.AdminLogResponse;
import com.tams.webserver.datasource.repository.AdminLogRepository;
import com.tams.webserver.datasource.repository.TimeLogRepository;
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
public class AccessLogService {

    private TimeLogRepository timeLogRepository;

    private AdminLogRepository adminLogRepository;

    public List<TimeLogResponse> getTimeLogList(String deviceSerialNo) {
        deviceSerialNo = deviceSerialNo.equals("-") ? "" : deviceSerialNo;
        List<Object[]> rawResults = timeLogRepository.findTimeLogs(deviceSerialNo);
        List<TimeLogResponse> responseData = new ArrayList<>();

        for (Object[] result : rawResults) {

            TimeLogResponse timeLogResponse = TimeLogResponse.builder()
                    .id((String) result[0])
                    .deviceSerialNo(result[1].equals("") ? "-" : (String) result[1])
                    .userId(result[2].equals("") ? "-" : (String) result[2])
                    .userName((null == result[3] || ((String) result[3]).isEmpty()) ? "-" : ((String) result[3]).trim())
                    .attendStat((String) result[4])
                    .action((String) result[5])
                    .logTime(Utilities.formatLocalDateTimeToUTCString(((Timestamp) result[6]).toLocalDateTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                    .build();
            responseData.add(timeLogResponse);
        }
        return responseData;
    }

    public List<AdminLogResponse> getAdminLogList(String deviceSerialNo) {
        deviceSerialNo = deviceSerialNo.equals("-") ? "" : deviceSerialNo;
        List<Object[]> rawResults = adminLogRepository.findAdminLogs(deviceSerialNo);
        List<AdminLogResponse> responseData = new ArrayList<>();

        for (Object[] result : rawResults) {

            AdminLogResponse timeLogResponse = AdminLogResponse.builder()
                    .id((String) result[0])
                    .deviceSerialNo(result[1].equals("") ? "-" : (String) result[1])
                    .userId(result[2].equals("") ? "-" : (String) result[2])
                    .userName((null == result[3] || ((String) result[3]).isEmpty()) ? "-" : ((String) result[3]).trim())
                    .action((String) result[4])
                    .logTime(Utilities.formatLocalDateTimeToUTCString(((Timestamp) result[5]).toLocalDateTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                    .build();
            responseData.add(timeLogResponse);
        }
        return responseData;
    }


}
