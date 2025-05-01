package com.shavika.websocket.command.commands;

import com.shavika.websocket.audit.Trace;
import com.shavika.websocket.audit.TraceInfo;
import com.shavika.websocket.command.AnyUpdate;
import com.shavika.websocket.command.commands.models.KeepAliveRequest;
import com.shavika.websocket.command.commands.models.KeepAliveResponse;
import com.shavika.websocket.datasource.entity.DeviceInfo;
import com.shavika.websocket.datasource.entity.DeviceLog;
import com.shavika.websocket.datasource.repository.DeviceInfoRepository;
import com.shavika.websocket.datasource.repository.DeviceLogRepository;
import com.shavika.websocket.socket.SessionContext;
import com.shavika.websocket.socket.SessionInfo;
import com.shavika.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component("KeepAlive")
@RequiredArgsConstructor // Lombok annotation to generate a constructor with required args
public class KeepAliveCommand implements Command {


    private KeepAliveCommand proxy;

    @Autowired
    private DeviceLogRepository deviceLogRepository;

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;

    @Autowired
    private AnyUpdate anyUpdate;


    @Override
    public void init(Command command) {
        this.proxy = (KeepAliveCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) { return ""; }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        KeepAliveRequest request = Utilities.convertToMessage(sessionInfo.getMessageInput(), KeepAliveRequest.class);
        log.info("Execution:" + request);
        DeviceLog deviceLog = proxy.createDeviceLog(sessionInfo, request, traceInfo);
        proxy.updateLastConnectedTimeOfDeviceInfo(deviceLog, traceInfo);
        proxy.sendResposne(sessionInfo.getSessionId(), deviceLog, traceInfo);
        proxy.callAnyUpdate(sessionInfo, traceInfo);
    }

    @Trace
    @Transactional
    public DeviceLog createDeviceLog(SessionInfo sessionInfo, KeepAliveRequest request, TraceInfo traceInfo) {
        DeviceLog deviceLog = DeviceLog.builder()
                .terminalType(request.getTerminalType())
                .terminalId(request.getTerminalID())
                .productName(request.getProductName())
                .deviceSerialNo(request.getDeviceSerialNo())
                .deviceUID(request.getDeviceUID())
                .hostAddress(sessionInfo.getHostAddress())
                .hostPort(Integer.toString(sessionInfo.getHostPort()))
                .sessionId(sessionInfo.getSessionId())
                .deviceTime(Utilities.convertStringTimestamp(request.getDevTime(), Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                .serverTime(Utilities.getCurrentDateTime())
                .build();
        return deviceLogRepository.save(deviceLog);
    }

    @Trace
    @Transactional
    public DeviceInfo updateLastConnectedTimeOfDeviceInfo(DeviceLog deviceLog, TraceInfo traceInfo) {
        Optional<DeviceInfo> deviceInfoOptional = deviceInfoRepository.findByDeviceSerialNo(deviceLog.getDeviceSerialNo());
        return deviceInfoOptional.map(deviceInfo -> {
            deviceInfo.setLastConnectedTime(deviceLog.getServerTime());
            return deviceInfoRepository.save(deviceInfo);
        }).orElse(null);
    }

    @Trace
    public String sendResposne(String sessionId, DeviceLog deviceLog, TraceInfo traceInfo) {
        KeepAliveResponse response = KeepAliveResponse.builder()
                .response("KeepAlive")
                .result("OK")
                .devTime(Utilities.formatLocalDateTimeToUTCString(deviceLog.getDeviceTime(),
                        Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                .serverTime(Utilities.formatLocalDateTimeToUTCString(deviceLog.getServerTime(),
                        Utilities.YYY_MM_DD_T_HH_MM_SS_X))
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(sessionId, finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for LOGIN:", e);
            return "{ \"error\" : "+e.getMessage()+"}";
        }
    }

    @Trace
    public void callAnyUpdate(SessionInfo sessionInfo, TraceInfo traceInfo) {
        anyUpdate.checkAnyUpdateForDevice(sessionInfo.getDeviceSerialNo(), sessionInfo.getSessionId());
    }

}
