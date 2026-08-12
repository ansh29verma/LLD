package com.ansh.COR.ConcreteLogHandlers;

import com.ansh.COR.LogHandler;
import com.ansh.LogAppenderStrategies.LogAppender;

import java.util.logging.Logger;

public class DebugLogger extends LogHandler {
    public DebugLogger(int level, LogAppender appender) {
        super(level, appender);
    }

    @Override
    protected void write(String message) {
        System.out.println("DEBUG: " + message);
    }
}
