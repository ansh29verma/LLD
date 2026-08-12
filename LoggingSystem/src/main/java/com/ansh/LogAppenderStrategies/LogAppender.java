package com.ansh.LogAppenderStrategies;

import com.ansh.utility.LogMessage;

public interface LogAppender {
    void append(LogMessage logMessage);
}

