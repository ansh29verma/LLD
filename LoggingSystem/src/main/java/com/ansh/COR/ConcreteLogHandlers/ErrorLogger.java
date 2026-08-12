package com.ansh.COR.ConcreteLogHandlers;

import com.ansh.COR.LogHandler;
import com.ansh.LogAppenderStrategies.LogAppender;

public class ErrorLogger extends LogHandler {
    public ErrorLogger(int level, LogAppender appender) {
        super(level, appender);
    }

    @Override
    protected void write(String message) {
        System.out.println("ERROR: " + message);
    }
}
