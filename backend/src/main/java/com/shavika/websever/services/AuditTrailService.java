package com.shavika.websever.services;

import com.shavika.websever.api.webmodels.AuditTrailResponse;
import com.shavika.websever.api.webmodels.AuditTrailSpanResponse;
import com.shavika.websocket.datasource.entity.AuditTrail;
import com.shavika.websocket.datasource.repository.AuditTrailRepository;
import com.shavika.websocket.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.sql.Timestamp;

@Slf4j
@Service
@AllArgsConstructor
public class AuditTrailService {

    private AuditTrailRepository auditTrailRepository;

    public List<AuditTrailResponse> getAuditTrails() {
        List<Object[]> rawResults =  auditTrailRepository.findAuditTrailsWithAggregation();
        // Create a list to hold the response data
        List<AuditTrailResponse> responseData = new ArrayList<>();

        // Convert each AuditTrail entity to AuditTrailResponse
        for (Object[] result : rawResults) {
            BigDecimal durationBigDecimal = (BigDecimal) result[4]; // assuming duration is at index 4
            Long duration = durationBigDecimal != null ? durationBigDecimal.longValue() : null;

            AuditTrailResponse auditTrailResponse = AuditTrailResponse.builder()
                    .traceId((String) result[0])
                    .createdOn(Utilities.formatLocalDateTimeToUTCString( ((Timestamp) result[1]).toLocalDateTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                    .deviceSerialNo((String) result[2])
                    .command((String) result[3])
                    .duration(Long.toString(duration))
                    .hostAddress((String) result[5])
                    .hostPort((String) result[6])
                    .startTime((Utilities.formatLocalDateTimeToUTCString(Utilities.getDateTimeFromMills((long) result[7]), Utilities.YYY_MM_DD_T_HH_MM_SS_Z)))
                    .build();
            responseData.add(auditTrailResponse);
        }
        return responseData;
    }

    public List<AuditTrailSpanResponse> getAuditTrailsSpanDetails(String traceId)  throws Exception {
        List<AuditTrail> rawResults =  auditTrailRepository.findrecordsByTraceId(traceId);
        // Create a list to hold the response data
        List<AuditTrailSpanResponse> responseData = new ArrayList<>();

        for (AuditTrail auditTrail : rawResults) {
            AuditTrailSpanResponse response = AuditTrailSpanResponse.builder()
                    .traceId(auditTrail.getTraceId())
                    .spanId(auditTrail.getSpanId())
                    .createdOn(Utilities.formatLocalDateTimeToUTCString(auditTrail.getCreatedOn(), Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                    .deviceSerialNo(auditTrail.getDeviceSerialNo())
                    .command(auditTrail.getCommand())
                    .duration(Long.toString(auditTrail.getDuration()))
                    .input(auditTrail.getInput())
                    .output(auditTrail.getOutput())
                    .startTime(Utilities.formatLocalDateTimeToUTCString(Utilities.getDateTimeFromMills(auditTrail.getStartTime()), Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                    .build();
            responseData.add(response);
        }
        return responseData;
    }
}
