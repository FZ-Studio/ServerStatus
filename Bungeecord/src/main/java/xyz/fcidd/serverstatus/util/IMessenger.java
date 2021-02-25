package xyz.fcidd.serverstatus.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.fcidd.serverstatus.ServerStatus;

public class IMessenger {

    private static final Logger logger = Logger.getLogger("");
    private static final CommandSender CONSOLE = ServerStatus.getInstance().getProxy().getConsole();
    private static final String PREFIX = "§8[§6ServerStatus§8]§r";

    /**
     * INFO
     * 
     * @param msg 消息
     */
    public static void info(String msg) {
        info(msg, true);
    }

    /**
     * INFO
     * 
     * @param msg    消息
     * @param prefix 是否有前缀
     */
    public static void info(String msg, boolean prefix) {
        log(msg, Level.INFO, prefix);
    }

    /**
     * WARNING
     * 
     * @param msg 消息
     */
    public static void warning(String msg) {
        warning(msg, true);
    }

    /**
     * WARNING
     * 
     * @param msg    消息
     * @param prefix 是否有前缀
     */
    public static void warning(String msg, boolean prefix) {
        log(msg, Level.WARNING, prefix);
    }

    /**
     * LOG
     * 
     * @param msg   消息
     * @param level 消息等级
     */
    public static void log(String msg, Level level) {
        log(msg, level, true);
    }

    /**
     * LOG
     * 
     * @param msg    消息
     * @param level  消息等级
     * @param prefix 是否有前缀
     */
    public static void log(String msg, Level level, boolean prefix) {
        if (prefix) {
            logger.log(level, PREFIX + msg);
        } else {
            logger.log(level, msg);
        }
    }

    /**
     * 向命令发送者发送反馈
     * 
     * @param sender  发送者
     * @param message 消息
     */
    public static void sendPlayerFeedback(CommandSender sender, String message) {
        if (sender != null && !sender.equals(CONSOLE)) {
            sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§r").append(message).create());
        }
    }
}
