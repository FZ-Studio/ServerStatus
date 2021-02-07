package xyz.fcidd.serverstatus.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import xyz.fcidd.serverstatus.ServerStatus;

public class IMessenger {

    private static final Logger logger = ServerStatus.getInstance().getLogger();
    private static final Logger nonPrefixLogger = Logger.getLogger("");

    public static void info(String msg) {
        info(msg, true);
    }

    public static void info(String msg, boolean prefix) {
        log(msg, Level.INFO, prefix);
    }

    public static void warning(String msg) {
        warning(msg, true);
    }

    public static void warning(String msg, boolean prefix) {
        log(msg, Level.WARNING, prefix);
    }

    public static void log(String msg, Level level) {
        log(msg, level, true);
    }

    public static void log(String msg, Level level, boolean prefix) {
        if (prefix) {
            logger.log(level, msg);
        } else {
            nonPrefixLogger.log(level, msg);
        }
    }

    public static void sendPlayerFeedback(CommandSender sender, String message) {
        if (sender != null && !(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("§8[§6ServerStatus§8]§r" + message);
        }
    }
}
