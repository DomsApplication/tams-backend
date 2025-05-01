package com.shavika.websocket.command.commands;

import com.shavika.websocket.audit.Trace;
import com.shavika.websocket.audit.TraceInfo;
import com.shavika.websocket.command.commands.models.EmptyTimeLogRequest;
import com.shavika.websocket.command.commands.models.GetUserDataRequest;
import com.shavika.websocket.command.commands.models.GetUserDataResponse;
import com.shavika.websocket.datasource.entity.UserFingers;
import com.shavika.websocket.datasource.entity.UserInfo;
import com.shavika.websocket.datasource.entity.UserInfoExt;
import com.shavika.websocket.datasource.repository.UserFingersRepository;
import com.shavika.websocket.datasource.repository.UserInfoExtRepository;
import com.shavika.websocket.datasource.repository.UserInfoRepository;
import com.shavika.websocket.socket.SessionContext;
import com.shavika.websocket.socket.SessionInfo;
import com.shavika.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component("GetNextUserData")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class GetNextUserDataCommand implements Command {

    private GetNextUserDataCommand proxy;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoExtRepository userInfoExtRepository;

    @Autowired
    private UserFingersRepository userFingersRepository;

    @Override
    public void init(Command command) {
        this.proxy = (GetNextUserDataCommand) command;
    }

    @Trace
    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/GetNextUserData:::{}", traceInfo);
        GetUserDataRequest response = GetUserDataRequest.builder().request("GetNextUserData").build();

        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for GetNextUserData:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        GetUserDataResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(),
                GetUserDataResponse.class);
        log.info("Execution:" + response);
        if(response.getResult().equals("OK")) {
            proxy.insertUserInfoData(response, traceInfo);
            proxy.insertUserInfoExtData(response, traceInfo);
            proxy.insertUserFingerData(response, traceInfo);
        }
    }

    @Trace
    @Transactional
    public UserInfo insertUserInfoData(GetUserDataResponse response, TraceInfo traceInfo) {
        // Check if the record exists
        Optional<UserInfo> existingUserInfo = userInfoRepository.findByUserId(response.getUserID());
        UserInfo userInfo = null;
        String user_name = Utilities.decodeName(response.getName());
        LocalDateTime user_period_start = Utilities.convertToDateTime(
                Utilities.decodeUserPeriod(Integer.valueOf(response.getUserPeriodStart())));
        LocalDateTime user_period_end = Utilities.convertToDateTime(
                Utilities.decodeUserPeriod(Integer.valueOf(response.getUserPeriodEnd())));

        if (existingUserInfo.isPresent()) {
            userInfo = existingUserInfo.get();
            userInfo.setName(user_name);
            userInfo.setPrivilege(response.getPrivilege());
            userInfo.setCompany(null);
            userInfo.setDepartmentId(response.getDepart());
            userInfo.setDepartmentName(null);
            userInfo.setEnabled(Utilities.convertToBoolean(response.getEnabled()));
            userInfo.setUserPeriodUsed(Utilities.convertToBoolean(response.getUserPeriodUsed()));
            userInfo.setUserPeriodStart(user_period_start);
            userInfo.setUserPeriodEnd(user_period_end);
        } else {
            userInfo = UserInfo.builder()
                    .userId(user_name)
                    .name(user_name)
                    .privilege(response.getPrivilege())
                    .company(null)
                    .departmentId(response.getDepart())
                    .departmentName(null)
                    .enabled(Utilities.convertToBoolean(response.getEnabled()))
                    .userPeriodUsed(Utilities.convertToBoolean(response.getUserPeriodUsed()))
                    .userPeriodStart(user_period_start)
                    .userPeriodEnd(user_period_end)
                    .build();
        }
        return userInfoRepository.save(userInfo);
    }

    @Trace
    @Transactional
    public UserInfoExt insertUserInfoExtData(GetUserDataResponse response, TraceInfo traceInfo) {
        // Check if the record exists
        Optional<UserInfoExt> existingUserInfoExt = userInfoExtRepository.findByUserId(response.getUserID());
        UserInfoExt userInfoExt = null;
        if (existingUserInfoExt.isPresent()) {
            userInfoExt = existingUserInfoExt.get();
            userInfoExt.setUserId(response.getUserID());
            userInfoExt.setCard(response.getCard());
            userInfoExt.setPassword(response.getPwd());
            userInfoExt.setFaceEnrolled(Utilities.convertToBoolean(response.getFaceEnrolled()));
            userInfoExt.setFaceData(response.getFaceData().getBytes());
            userInfoExt.setPhotoCaptured(Boolean.FALSE.booleanValue());
            userInfoExt.setPhotoSize(0);
            userInfoExt.setPhotoData(null);
            userInfoExt.setTimeSet1(response.getTimeSet1());
            userInfoExt.setTimeSet2(response.getTimeSet2());
            userInfoExt.setTimeSet3(response.getTimeSet3());
            userInfoExt.setTimeSet4(response.getTimeSet4());
            userInfoExt.setTimeSet5(response.getTimeSet5());
        } else {
            userInfoExt = UserInfoExt.builder()
                    .userId(response.getUserID())
                    .card(response.getCard())
                    .password(response.getPwd())
                    .faceEnrolled(Utilities.convertToBoolean(response.getFaceEnrolled()))
                    .faceData(response.getFaceData().getBytes())
                    .photoCaptured(Boolean.FALSE.booleanValue())
                    .photoSize(0)
                    .photoData(null)
                    .timeSet1(response.getTimeSet1())
                    .timeSet2(response.getTimeSet2())
                    .timeSet3(response.getTimeSet3())
                    .timeSet4(response.getTimeSet4())
                    .timeSet5(response.getTimeSet5())
                    .build();
        }
        return userInfoExtRepository.save(userInfoExt);
    }

    @Trace
    @Transactional
    public void insertUserFingerData(GetUserDataResponse response, TraceInfo traceInfo) {

        Map<String, Map<String, Boolean>> userFingerData = Utilities.decodeFingers(Integer.valueOf(response.getFingers()));

        // Iterate over the outer map
        for (Map.Entry<String, Map<String, Boolean>> outerEntry : userFingerData.entrySet()) {
            String finger = outerEntry.getKey();
            Map<String, Boolean> details = outerEntry.getValue();

            // Check if the record exists
            Optional<UserFingers> existingUserFingers = userFingersRepository
                    .findByUserIdAndFingerId(response.getUserID(), Integer.valueOf(finger));
            UserFingers userFingers = null;
            if (existingUserFingers.isPresent()) {
                userFingers = existingUserFingers.get();
                userFingers.setEnrolled(details.get("enrolled"));
                userFingers.setDuress(details.get("duress"));
                userFingers.setFingerData(null);
            } else {
                userFingers = UserFingers.builder()
                        .userId(response.getUserID())
                        .fingerId(Integer.valueOf(finger))
                        .enrolled(details.get("enrolled"))
                        .duress(details.get("duress"))
                        .fingerData(null)
                        .build();
            }
            userFingersRepository.save(userFingers);
        }
    }


}
