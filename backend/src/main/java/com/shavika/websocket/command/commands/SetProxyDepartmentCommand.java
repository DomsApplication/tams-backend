package com.shavika.websocket.command.commands;

import com.shavika.websocket.audit.Trace;
import com.shavika.websocket.audit.TraceInfo;
import com.shavika.websocket.command.commands.models.SetProxyDepartmentRequest;
import com.shavika.websocket.command.commands.models.SetProxyDepartmentResponse;
import com.shavika.websocket.datasource.entity.ProxyDepartment;
import com.shavika.websocket.datasource.repository.ProxyDepartmentRepository;
import com.shavika.websocket.socket.SessionContext;
import com.shavika.websocket.socket.SessionInfo;
import com.shavika.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component("SetProxyDept")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class SetProxyDepartmentCommand implements Command {

    private SetProxyDepartmentCommand proxy;

    @Autowired
    private ProxyDepartmentRepository proxyDepartmentRepository;

    @Override
    public void init(Command command) {
        this.proxy = (SetProxyDepartmentCommand) command;
    }

    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/SetProxyDept:::{}", traceInfo);
        Optional<ProxyDepartment> existingProxyDepartment = proxyDepartmentRepository.findByProxyId(traceInfo.getDeptId());
        if (existingProxyDepartment.isEmpty() || null == existingProxyDepartment.get().getProxyName()) {
            return "{ \"error\" : \"Did not found the Proxy Department record for the ProxyID " + traceInfo.getDeptId() + "\"}";
        }
        ProxyDepartment proxyDepartment = existingProxyDepartment.get();
        SetProxyDepartmentRequest response = SetProxyDepartmentRequest.builder()
                .request("SetProxyDept")
                .proxyNo(proxyDepartment.getProxyId())
                .data(proxyDepartment.getProxyName())
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for SetProxyDept:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        SetProxyDepartmentResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), SetProxyDepartmentResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public SetProxyDepartmentResponse persistResult(SetProxyDepartmentResponse response, TraceInfo traceInfo) {
        return response;
    }

}
