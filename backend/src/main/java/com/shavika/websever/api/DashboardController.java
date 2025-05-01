package com.shavika.websever.api;

import com.shavika.websever.api.webmodels.ErrorResponse;
import com.shavika.websever.api.webmodels.HealthCheck;
import com.shavika.websever.services.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    @GetMapping("/widgets")
    public ResponseEntity<?> getWidgets() {
        try {
            return new ResponseEntity<>(dashboardService.getWidgetsData(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/devicelog")
    public ResponseEntity<?> getDeviceLog() {
        try {
            return new ResponseEntity<>(dashboardService.getDashboardDeviceLogData(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/timelog")
    public ResponseEntity<?> getTimeLog() {
        try {
            return new ResponseEntity<>(dashboardService.getDashboardTimeLogData(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
