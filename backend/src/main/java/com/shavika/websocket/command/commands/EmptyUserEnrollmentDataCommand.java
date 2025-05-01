package com.shavika.websocket.command.commands;

import com.shavika.websocket.audit.Trace;
import com.shavika.websocket.audit.TraceInfo;
import com.shavika.websocket.command.commands.models.EmptyUserEnrollmentDataRequest;
import com.shavika.websocket.command.commands.models.EmptyUserEnrollmentDataResponse;
import com.shavika.websocket.socket.SessionContext;
import com.shavika.websocket.socket.SessionInfo;
import com.shavika.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("EmptyUserEnrollmentData")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class EmptyUserEnrollmentDataCommand implements Command {

    private EmptyUserEnrollmentDataCommand proxy;

    @Override
    public void init(Command command) {
        this.proxy = (EmptyUserEnrollmentDataCommand) command;
    }

    @Trace
    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/EmptyUserEnrollmentData:::{}", traceInfo);
        EmptyUserEnrollmentDataRequest response = EmptyUserEnrollmentDataRequest.builder()
                .request("EmptyUserEnrollmentData")
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for EmptyUserEnrollmentData:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        EmptyUserEnrollmentDataResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), EmptyUserEnrollmentDataResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public EmptyUserEnrollmentDataResponse persistResult(EmptyUserEnrollmentDataResponse response, TraceInfo traceInfo) {
        return response;
    }
}
