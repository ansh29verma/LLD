package com.ansh.LogAppenderStrategies.ConcreteStartegies;

import com.ansh.LogAppenderStrategies.LogAppender;
import com.ansh.utility.LogMessage;

public class ConsoleAppender implements LogAppender {

    @Override
    public void append(LogMessage logMessage) {
        System.out.println(logMessage); // Print log to console
    }


}
