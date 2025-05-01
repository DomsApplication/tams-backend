package com.shavika.websever.api;

import com.shavika.websever.api.webmodels.ErrorResponse;
import com.shavika.websever.services.AccessLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/access")
public class AccessLogController {

    private AccessLogService accessLogService;

    @GetMapping("/timelog/{deviceSerialId}")
    public ResponseEntity<?> getTimelogDetails(@PathVariable("deviceSerialId") String deviceSerialId) {
        try {
            return new ResponseEntity<>(accessLogService.getTimeLogList(deviceSerialId), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/adminlog/{deviceSerialId}")
    public ResponseEntity<?> getAdminlogDetails(@PathVariable("deviceSerialId") String deviceSerialId) {
        try {
            return new ResponseEntity<>(accessLogService.getAdminLogList(deviceSerialId), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
