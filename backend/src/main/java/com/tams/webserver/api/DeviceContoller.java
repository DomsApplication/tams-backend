package com.tams.webserver.api;

import com.tams.webserver.api.webmodels.ErrorResponse;
import com.tams.webserver.api.webmodels.DeviceDataSyncRequest;
import com.tams.webserver.api.webmodels.DeviceInfoResponse;
import com.tams.webserver.services.DeviceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/device")
public class DeviceContoller {

    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity getAllDevices() {
        try {
            List<DeviceInfoResponse> deviceList = deviceService.getAllDeviceInfo();
            return new ResponseEntity<>(deviceList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse resposne = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(resposne, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("registered")
    public ResponseEntity getAllRegisteredDevices() {
        try {
            List<DeviceInfoResponse> deviceList = deviceService.getAllDeviceInfo(true);
            return new ResponseEntity<>(deviceList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse resposne = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(resposne, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("unregistered")
    public ResponseEntity getAllUnRegisteredDevices() {
        try {
            List<DeviceInfoResponse> deviceList = deviceService.getAllDeviceInfo(false);
            return new ResponseEntity<>(deviceList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse resposne = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(resposne, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{deviceSerialNo}")
    public ResponseEntity<?> getDeviceInfoByDeviceId(
            @PathVariable("deviceSerialNo") String deviceSerialNo) {
        try {
            return new ResponseEntity<>(deviceService.getDeviceInfoByDeviceId(deviceSerialNo), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{deviceSerialNo}/logs")
    public ResponseEntity<?> getDeviceLogByTimePeriod(
            @PathVariable("deviceSerialNo") String deviceSerialNo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            return new ResponseEntity<>(deviceService.getDeviceLogData(deviceSerialNo, start, end), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updatedeviceflag")
    public ResponseEntity<?> updateuserFlags(@RequestBody DeviceDataSyncRequest deviceDataSyncRequest) {
        try {
            return new ResponseEntity<>(deviceService.updateDeviceCommandFlags(deviceDataSyncRequest), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerDevice(@RequestBody Map<String,String> registerData) {
        try {
            return new ResponseEntity<>(deviceService.updateDeviceInfo(registerData), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder().message(e.getMessage()).build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
