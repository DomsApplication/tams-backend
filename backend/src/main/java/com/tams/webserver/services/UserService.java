package com.tams.webserver.services;

import com.tams.webserver.api.webmodels.*;
import com.tams.webserver.datasource.repository.*;
import com.tams.webserver.datasource.entity.Department;
import com.tams.webserver.datasource.entity.UserCommands;
import com.tams.webserver.datasource.entity.UserInfo;
import com.tams.webserver.datasource.entity.UserInfoExt;
import com.tams.webserver.datasource.entity.enums.UserFlagStatus;
import com.tams.webserver.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private UserInfoRepository userInfoRepository;
    private UserInfoExtRepository userInfoExtRepository;
    private UserCommandsRepository userCommandsRepository;
    private DepartmentRepository departmentRepository;

    private UserFingersRepository userFingersRepository;
    public List<UserListResponse> getUserList() {
        List<Object[]> rawResults = userInfoRepository.findUserDetails();
        List<UserListResponse> responseData = new ArrayList<>();

        for (Object[] result : rawResults) {

            UserListResponse userListResponse = UserListResponse.builder()
                    .userId((String) result[0])
                    .username(((String) result[1]).trim())
                    .privilege((String) result[2])
                    .enabled(((Boolean) result[3]) ? "Yes" : "No")
                    .departmentId((String) result[4])
                    .faceDataAvailable(((Boolean) result[5]) ? "Yes" : "No")
                    .fingerDataAvailable(((Long) result[6]) > 0 ? "Yes" : "No")
                    .photoDataAvailable(((Boolean) result[7]) ? "Yes" : "No")
                    .passwordAvailable(( null == result[8] ) ? "No" : "Yes")
                    .cardAvailable(( null == result[9] ) ? "No" : "Yes")
                    .build();
            responseData.add(userListResponse);
        }
        return responseData;
    }

    public UserListResponse getUserByUserId(String userId) {
        List<Object[]> rawResults = userInfoRepository.findUserDetailsByUserId(userId);
        UserListResponse userListResponse = null;
        for (Object[] result : rawResults) {
            userListResponse = UserListResponse.builder()
                    .userId((String) result[0])
                    .username(((String) result[1]).trim())
                    .privilege((String) result[2])
                    .enabled(((Boolean) result[3]) ? "Yes" : "No")
                    .departmentId((String) result[4])
                    .faceDataAvailable(((Boolean) result[5]) ? "Yes" : "No")
                    .fingerDataAvailable(((Long) result[6]) > 0 ? "Yes" : "No")
                    .photoDataAvailable(((Boolean) result[7]) ? "Yes" : "No")
                    .passwordAvailable(( null == result[8] ) ? "No" : "Yes")
                    .cardAvailable(( null == result[9] ) ? "No" : "Yes")
                    .build();
        }
        return userListResponse;
    }

    @Transactional
    public UserDataResponse syncUserData(UserDataRequest userDataRequest) throws  Exception {
        this.validateUserRequest(userDataRequest);
        this.insertUserInfoData(userDataRequest);
        this.insertUserInfoExtData(userDataRequest);
        this.insertUserCommand(userDataRequest);
    return UserDataResponse.builder()
            .userId(userDataRequest.getUserId().trim())
            .message("User created successfully...")
            .build();
    }

    private  void validateUserRequest(UserDataRequest userDataRequest) throws IllegalArgumentException {
        StringBuilder errors = new StringBuilder();
        if (userDataRequest.getUserId() == null || !Pattern.matches("^[a-zA-Z0-9_]{3,20}$", userDataRequest.getUserId())) {
            errors.append("User ID must be 3-20 alphanumeric characters, underscores, or hyphens.\n");
        }
        if (userDataRequest.getName() == null || !Pattern.matches("^[a-zA-Z0-9_ ]{6,30}$", userDataRequest.getName())) {
            errors.append("User Name must be 6-30 alphanumeric characters, underscores, or hyphens, or space.\n");
        }
        if (userDataRequest.getDepartmentId() == null || userDataRequest.getDepartmentId().isEmpty()) {
            errors.append("Department needs to be selected.\n");
        }
        if (userDataRequest.getPrivilege() == null || userDataRequest.getPrivilege().isEmpty()) {
            errors.append("Privilege needs to be selected.\n");
        }
        if (null != userDataRequest.getSelecteddevice() && userDataRequest.getSelecteddevice().size() > 0) {
            List<String> selectedDevices = userDataRequest.getSelecteddevice();
            if (selectedDevices == null || selectedDevices.isEmpty()) {
                errors.append("At least one device needs to be selected for user sync.\n");
            }
        }
        if (errors.length() > 0) {
            throw new IllegalArgumentException(errors.toString().trim());
        }
    }

    @Transactional
    private UserInfo insertUserInfoData(UserDataRequest userDataRequest) throws  Exception {
        // Check if the record exists
        Optional<UserInfo> existingUserInfo = userInfoRepository.findByUserId(userDataRequest.getUserId().trim());
        UserInfo userInfo = null;
        LocalDateTime user_period_date_time = Utilities
                .convertStringTimestamp("2000-01-01-T00:00:00Z", Utilities.YYY_MM_DD_T_HH_MM_SS_X);
        Optional<Department> existingDept = departmentRepository.findByDepartmentId(userDataRequest.getDepartmentId().trim());
        String deptName = existingDept.isPresent() ? existingDept.get().getDepartmentName() : "";
        if (existingUserInfo.isPresent()) {
            userInfo = existingUserInfo.get();
            userInfo.setName(userDataRequest.getName());
            userInfo.setPrivilege(userDataRequest.getPrivilege());
            userInfo.setCompany(null);
            userInfo.setDepartmentId(userDataRequest.getDepartmentId());
            userInfo.setDepartmentName(deptName);
            userInfo.setEnabled(userDataRequest.isEnabled());
        } else {
            userInfo = UserInfo.builder()
                    .userId(userDataRequest.getUserId().trim())
                    .name(userDataRequest.getName())
                    .privilege(userDataRequest.getPrivilege())
                    .company(null)
                    .departmentId(userDataRequest.getDepartmentId())
                    .departmentName(deptName)
                    .enabled(userDataRequest.isEnabled())
                    .userPeriodUsed(false)
                    .userPeriodStart(user_period_date_time)
                    .userPeriodEnd(user_period_date_time)
                    .build();
        }
        return userInfoRepository.save(userInfo);
    }

    @Transactional
    private UserInfoExt insertUserInfoExtData(UserDataRequest userDataRequest) throws  Exception {
        // Check if the record exists
        Optional<UserInfoExt> existingUserInfoExt = userInfoExtRepository.findByUserId(userDataRequest.getUserId().trim());
        UserInfoExt userInfoExt = null;
        if (existingUserInfoExt.isEmpty()) {
            userInfoExt = UserInfoExt.builder()
                    .userId(userDataRequest.getUserId())
                    .card(null)
                    .password(null)
                    .faceEnrolled(false)
                    .faceData(null)
                    .photoCaptured(Boolean.FALSE.booleanValue())
                    .photoSize(0)
                    .photoData(null)
                    .timeSet1(String.valueOf(0))
                    .timeSet2(String.valueOf(0))
                    .timeSet3(String.valueOf(0))
                    .timeSet4(String.valueOf(0))
                    .timeSet5(String.valueOf(0))
                    .build();
        }
        return userInfoExtRepository.save(userInfoExt);
    }

    @Transactional
    private void insertUserCommand(UserDataRequest userDataRequest) throws  Exception {
        if (null != userDataRequest.getSelecteddevice() && userDataRequest.getSelecteddevice().size() > 0) {
            String userId = userDataRequest.getUserId().trim();
            List<String> selectedDevices = userDataRequest.getSelecteddevice();
            for (String deviceSerialNo : selectedDevices) {
                Optional<UserCommands> existingUserCommands = userCommandsRepository
                        .findUserCommandByDeviceSerialNoAndUserId(deviceSerialNo.trim(), userId);
                UserCommands userCommands = null;
                if (existingUserCommands.isEmpty()) {
                    userCommands = UserCommands.builder()
                            .deviceSerialNo(deviceSerialNo.trim())
                            .userId(userId)
                            .getUserData(UserFlagStatus.DRAFT)
                            .setUserData(userDataRequest.isDownloadUserInfo() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT)
                            .getNextUserDataExt((UserFlagStatus.DRAFT))
                            .getUserPassword((UserFlagStatus.DRAFT))
                            .getUserCardNo((UserFlagStatus.DRAFT))
                            .enrollFaceByPhoto((UserFlagStatus.DRAFT))
                            .getFingerData((UserFlagStatus.DRAFT))
                            .setFingerData((UserFlagStatus.DRAFT))
                            .getFaceData((UserFlagStatus.DRAFT))
                            .setFaceData((UserFlagStatus.DRAFT))
                            .getUserPhoto((UserFlagStatus.DRAFT))
                            .setUserPhoto((UserFlagStatus.DRAFT))
                            .getFirstGlog((UserFlagStatus.DRAFT))
                            .getNextGlog((UserFlagStatus.DRAFT))
                            .build();
                    userCommandsRepository.save(userCommands);
                }
            }
        }
    }

    @Transactional
    public String deleteUserInfoData(String userId, boolean deleteRequiredInDevices) throws Exception {
        userInfoRepository.findByUserId(userId.trim()).ifPresent(userInfoRepository::delete);
        userInfoExtRepository.findByUserId(userId.trim()).ifPresent(userInfoExtRepository::delete);
        if (deleteRequiredInDevices) {
            userCommandsRepository.findrecordsByuserId(userId).stream().forEach(record -> userCommandsRepository.delete(record));
        }
        return "Successfully deleted the user record '" + userId + "'";
    }

    public byte[] getUserPhotoByUserId(String userId) {
        return userInfoExtRepository.findByUserId(userId.trim()).map(UserInfoExt::getPhotoData).orElse(null);
    }

    public List<UserFingerResponse> getUserFingerByUserId(String userId) {
        return userFingersRepository.findByUserId(userId.trim())
                .stream().map(finger -> {
                    return UserFingerResponse.builder()
                            .userId(finger.getUserId())
                            .fingerId(finger.getFingerId())
                            .enrolled(finger.getEnrolled())
                            .duress(finger.getDuress())
                            .fingerData((null != finger.getFingerData()))
                            .build();
                }).collect(Collectors.toList());
    }

    @Transactional
    public String updateUserCommandFlags(UserDataSyncRequest userDataSyncRequest) throws  Exception {
        if(null != userDataSyncRequest.getUserId()
                && null != userDataSyncRequest.getSelectedDevice()
                && userDataSyncRequest.getSelectedDevice().size() > 0) {
            String userId = userDataSyncRequest.getUserId();
            for(String deviceSerialNo: userDataSyncRequest.getSelectedDevice()) {
                Optional<UserCommands> existingUserCommands = userCommandsRepository
                        .findUserCommandByDeviceSerialNoAndUserId(deviceSerialNo.trim(), userId);
                UserCommands userCommands = null;
                if (existingUserCommands.isPresent()) {
                    userCommands = existingUserCommands.get();
                    userCommands.setSetUserData(userDataSyncRequest.isSetUserData() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                    userCommands.setSetFingerData(userDataSyncRequest.isSetFingerData() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                    userCommands.setSetFaceData(userDataSyncRequest.isSetFaceData() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                    userCommands.setSetUserPhoto(userDataSyncRequest.isSetUserPhoto() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);

                    userCommands.setGetUserData(userDataSyncRequest.isGetUserData() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                    userCommands.setGetFingerData(userDataSyncRequest.isGetFingerData() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                    userCommands.setGetFaceData(userDataSyncRequest.isGetFaceData() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                    userCommands.setGetUserPhoto(userDataSyncRequest.isGetUserPhoto() ? UserFlagStatus.NEW: UserFlagStatus.DRAFT);
                    userCommandsRepository.save(userCommands);
                }
            }
        }
        return "Successfully updated the command flag '" + userDataSyncRequest.getUserId() + "'";
    }

}
