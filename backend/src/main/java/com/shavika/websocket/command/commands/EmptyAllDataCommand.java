package com.shavika.websocket.command.commands;

import com.shavika.websocket.audit.Trace;
import com.shavika.websocket.audit.TraceInfo;
import com.shavika.websocket.command.commands.models.EmptyAllDataRequest;
import com.shavika.websocket.command.commands.models.EmptyAllDataResponse;
import com.shavika.websocket.socket.SessionContext;
import com.shavika.websocket.socket.SessionInfo;
import com.shavika.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("EmptyAllData")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class EmptyAllDataCommand implements Command {

    private EmptyAllDataCommand proxy;

    @Override
    public void init(Command command) {
        this.proxy = (EmptyAllDataCommand) command;
    }

    @Trace
    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/EmptyAllData:::{}", traceInfo);
        EmptyAllDataRequest response = EmptyAllDataRequest.builder()
                .request("EmptyAllData")
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for EmptyAllData:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        EmptyAllDataResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), EmptyAllDataResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public EmptyAllDataResponse persistResult(EmptyAllDataResponse response, TraceInfo traceInfo) {
        return response;
    }
}
