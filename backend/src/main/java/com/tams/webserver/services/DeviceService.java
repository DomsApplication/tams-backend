package com.tams.webserver.services;

import com.tams.webserver.api.webmodels.DeviceDataSyncRequest;
import com.tams.webserver.api.webmodels.DeviceInfoResponse;
import com.tams.webserver.api.webmodels.DeviceLogResponse;
import com.tams.webserver.datasource.entity.DeviceCommands;
import com.tams.webserver.datasource.entity.DeviceInfo;
import com.tams.webserver.datasource.entity.enums.UserFlagStatus;
import com.tams.webserver.datasource.repository.DeviceCommandsRepository;
import com.tams.webserver.datasource.repository.DeviceInfoRepository;
import com.tams.webserver.datasource.repository.DeviceLogRepository;
import com.tams.webserver.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceService {

    private DeviceInfoRepository deviceInfoRepository;

    private DeviceLogRepository deviceLogRepository;

    private DeviceCommandsRepository deviceCommandsRepository;

    public List<DeviceInfoResponse> getAllDeviceInfo() throws Exception {
        try {
            // Fetch the list of DeviceInfo entities from the repository
            List<DeviceInfo> entityData = deviceInfoRepository.findAll();

            // Create a list to hold the response data
            List<DeviceInfoResponse> responseData = new ArrayList<>();

            // Iterate through the entity list and convert each DeviceInfo to DeviceInfoResponse
            for (DeviceInfo deviceInfo : entityData) {
                DeviceInfoResponse deviceInfoResponse = DeviceInfoResponse.builder()
                        .id(deviceInfo.getId()) // assuming 'id' is inherited from BaseEntity
                        .deviceSerialNo(deviceInfo.getDeviceSerialNo())
                        .cloudId(deviceInfo.getCloudId())
                        .productName(deviceInfo.getProductName())
                        .terminalType(deviceInfo.getTerminalType())
                        .status(deviceInfo.getStatus().name()) // Enum to String conversion
                        .registeredOn(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getRegisteredOn(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                        .loggedOn(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getLoggedOn(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                        .sessionId(deviceInfo.getSessionId())
                        .hostAddress(deviceInfo.getHostAddress())
                        .hostPort(deviceInfo.getHostPort())
                        .lastConnectedTime(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getLastConnectedTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                        .isRegistered(deviceInfo.getIsRegistered())
                        .build();

                // Add the transformed response object to the response list
                responseData.add(deviceInfoResponse);
            }
            // Return the transformed list of DeviceInfoResponse
            return responseData;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<DeviceInfoResponse> getAllDeviceInfo(Boolean isRegistered) throws Exception {
        try {
            // Fetch the list of DeviceInfo entities from the repository
            List<DeviceInfo> entityData;
            if (isRegistered){
                entityData = deviceInfoRepository.findAllRegistered();
            } else {
                entityData = deviceInfoRepository.findAllUnRegistered();
            }

            // Create a list to hold the response data
            List<DeviceInfoResponse> responseData = new ArrayList<>();

            // Iterate through the entity list and convert each DeviceInfo to DeviceInfoResponse
            for (DeviceInfo deviceInfo : entityData) {
                DeviceInfoResponse deviceInfoResponse = DeviceInfoResponse.builder()
                        .id(deviceInfo.getId()) // assuming 'id' is inherited from BaseEntity
                        .deviceSerialNo(deviceInfo.getDeviceSerialNo())
                        .cloudId(deviceInfo.getCloudId())
                        .productName(deviceInfo.getProductName())
                        .terminalType(deviceInfo.getTerminalType())
                        .status(deviceInfo.getStatus().name()) // Enum to String conversion
                        .registeredOn(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getRegisteredOn(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                        .loggedOn(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getLoggedOn(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                        .sessionId(deviceInfo.getSessionId())
                        .hostAddress(deviceInfo.getHostAddress())
                        .hostPort(deviceInfo.getHostPort())
                        .lastConnectedTime(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getLastConnectedTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                        .isRegistered(deviceInfo.getIsRegistered())
                        .build();

                // Add the transformed response object to the response list
                responseData.add(deviceInfoResponse);
            }
            // Return the transformed list of DeviceInfoResponse
            return responseData;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<DeviceLogResponse> getDeviceLogData(String deviceSerialNo, LocalDateTime start, LocalDateTime end) {
        return deviceLogRepository.findDeviceLogsWithinPeriod(deviceSerialNo, start, end).stream()
                .map(deviceLog -> DeviceLogResponse.builder()
                        .deviceSerialNo(deviceLog.getDeviceSerialNo())
                        .deviceTime(Utilities.formatLocalDateTimeToUTCString(deviceLog.getDeviceTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                        .build())
                .collect(Collectors.toList());
    }


    public DeviceInfoResponse getDeviceInfoByDeviceId(String deviceSerialNo) {
        Optional<DeviceInfo> existingDeviceInfo = deviceInfoRepository.findByDeviceSerialNo(deviceSerialNo);
        if (existingDeviceInfo.isPresent()) {
            DeviceInfo deviceInfo = existingDeviceInfo.get();
            DeviceInfoResponse deviceInfoResponse = DeviceInfoResponse.builder()
                    .id(deviceInfo.getId()) // assuming 'id' is inherited from BaseEntity
                    .deviceSerialNo(deviceInfo.getDeviceSerialNo())
                    .cloudId(deviceInfo.getCloudId())
                    .productName(deviceInfo.getProductName())
                    .terminalType(deviceInfo.getTerminalType())
                    .status(deviceInfo.getStatus().name()) // Enum to String conversion
                    .registeredOn(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getRegisteredOn(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                    .loggedOn(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getLoggedOn(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                    .sessionId(deviceInfo.getSessionId())
                    .hostAddress(deviceInfo.getHostAddress())
                    .hostPort(deviceInfo.getHostPort())
                    .lastConnectedTime(Utilities.formatLocalDateTimeToUTCString(deviceInfo.getLastConnectedTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_Z))
                    .build();
            return deviceInfoResponse;
        }
        return null;
    }

    @Transactional
    public String updateDeviceCommandFlags(DeviceDataSyncRequest deviceDataSyncRequest) throws  Exception {
        if(null != deviceDataSyncRequest.getDeviceSerialNo()) {
            Optional<DeviceCommands> existingDeviceCommands = deviceCommandsRepository
                    .findByDeviceSerialNo(deviceDataSyncRequest.getDeviceSerialNo());
            DeviceCommands deviceCommands = null;
            if (existingDeviceCommands.isPresent()) {
                deviceCommands = existingDeviceCommands.get();
                deviceCommands.setGetDepartment(deviceDataSyncRequest.isGetDepartment() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                deviceCommands.setSetDepartment(deviceDataSyncRequest.isSetDepartment() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                deviceCommands.setEmptyTimeLog(deviceDataSyncRequest.isEmptyTimeLog() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                deviceCommands.setEmptyManageLog(deviceDataSyncRequest.isEmptyManageLog() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                deviceCommands.setEmptyUserEnrollmentData(deviceDataSyncRequest.isEmptyUserEnrollmentData() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                deviceCommands.setEmptyAllData(deviceDataSyncRequest.isEmptyAllData() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                deviceCommandsRepository.save(deviceCommands);
            }
        }
        return "Successfully updated the command flag '" + deviceDataSyncRequest.getDeviceSerialNo() + "'";
    }

    @Transactional
    public int updateDeviceInfo(Map<String, String> updatedData) throws  Exception {
        int status = 0;
        if(null != updatedData) {
            String deviceSerialNo = updatedData.get("deviceSerialNo");
            status = deviceInfoRepository.registerDevice(deviceSerialNo);
        }
        return status;
    }
}
