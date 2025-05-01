package com.shavika.websocket.command.commands;

import com.shavika.websocket.audit.Trace;
import com.shavika.websocket.audit.TraceInfo;
import com.shavika.websocket.command.commands.models.TakeOffManagerRequest;
import com.shavika.websocket.command.commands.models.TakeOffManagerResponse;
import com.shavika.websocket.socket.SessionContext;
import com.shavika.websocket.socket.SessionInfo;
import com.shavika.websocket.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("TakeOffManager")
@RequiredArgsConstructor // lombok annotation to generate a constructor with required args
public class TakeOffManagerCommand implements Command {

    private TakeOffManagerCommand proxy;

    @Override
    public void init(Command command) {
        this.proxy = (TakeOffManagerCommand) command;
    }

    @Trace
    @Override
    public String sender(TraceInfo traceInfo) {
        log.info("SENDER/TakeOffManager:::{}", traceInfo);
        TakeOffManagerRequest response = TakeOffManagerRequest.builder()
                .request("TakeOffManager")
                .build();
        try {
            String finalResponse = Utilities.convertToXmlString(response);
            SessionContext.sendResponse(traceInfo.getSessionId(), finalResponse);
            return finalResponse;
        } catch (Exception e) {
            log.error("Error in send response for TakeOffManager:", e);
            return "{ \"error\" : " + e.getMessage() + "}";
        }
    }

    @Trace
    @Override
    public void execute(SessionInfo sessionInfo, TraceInfo traceInfo) {
        TakeOffManagerResponse response = Utilities.convertToResponseMessage(sessionInfo.getMessageInput(), TakeOffManagerResponse.class);
        log.info("Execution:" + response);
        proxy.persistResult(response, traceInfo);
    }

    @Trace
    public TakeOffManagerResponse persistResult(TakeOffManagerResponse response, TraceInfo traceInfo) {
        return response;
    }
}
