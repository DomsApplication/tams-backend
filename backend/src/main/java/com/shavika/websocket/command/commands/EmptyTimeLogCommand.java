package com.shavika.websocket.command.commands;

import com.shavika.websocket.audit.Trace;
import com.shavika.websocket.audit.TraceInfo;
import com.shavika.websocket.command.commands.models.EmptyTimeLogRequest;
import com.shavika.websocket.command.commands.models.EmptyTimeLogResponse;
import com.shavika.websocket.socket.SessionContext;
import com.shavika.websocket.socket.SessionInfo;
import com.shavika.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("EmptyTimeLog")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class EmptyTimeLogCommand implements Command {

    private EmptyTimeLogCommand proxy;

    @Override
    public void init(Command command) {
        this.proxy = (EmptyTimeLogCommand) command;
    }

    @Trace
    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/EmptyTimeLog:::{}", traceInfo);
        EmptyTimeLogRequest response = EmptyTimeLogRequest.builder()
                .request("EmptyTimeLog")
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for SetUserPhoto:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        EmptyTimeLogResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), EmptyTimeLogResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public EmptyTimeLogResponse persistResult(EmptyTimeLogResponse response, TraceInfo traceInfo) {
        return response;
    }
}
