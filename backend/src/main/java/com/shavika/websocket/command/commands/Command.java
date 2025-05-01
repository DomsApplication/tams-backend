package com.shavika.websocket.command.commands;

import com.shavika.websocket.audit.TraceInfo;
import com.shavika.websocket.socket.SessionInfo;

public interface Command {

    void init(Command command);

    String sender(TraceInfo traceInfo);

    void execute(SessionInfo sessionInfo, TraceInfo traceInfo);

}
