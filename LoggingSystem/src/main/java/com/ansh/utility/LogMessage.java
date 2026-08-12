package com.ansh.utility;

import com.ansh.COR.ConcreteLogHandlers.LogLevel;
import lombok.Data;

@Data
public class LogMessage {

    private final LogLevel level;
    // The actual log message content
    private final String message;
    // Timestamp when the log message was created
    private final long timestamp;
    // Constructor to initialize log level and message, setting the timestamp to current time
    public LogMessage(LogLevel level, String message) {
        this.level = level;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }



    // Formats the log message as a string with level, timestamp, and message
    @Override
    public String toString() {
        return "[" + level + "] " + timestamp + " - " + message;
    }
}
