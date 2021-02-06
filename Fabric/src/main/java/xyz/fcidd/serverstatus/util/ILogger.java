package xyz.fcidd.serverstatus.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;

public class ILogger {
    private static final Logger logger = LogManager.getLogger("ServerStatus", ParameterizedMessageFactory.INSTANCE);

    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void warn(String msg) {
        logger.log(Level.WARN, msg);
    }

    public static void log(String msg, Level level) {
        logger.log(level, msg);
    }
}
