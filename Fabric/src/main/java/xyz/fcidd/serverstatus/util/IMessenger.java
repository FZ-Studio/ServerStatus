package xyz.fcidd.serverstatus.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import xyz.fcidd.serverstatus.translate.TranslatableKey;

public class IMessenger {

    private static final Logger logger = LogManager.getLogger("ServerStatus");
    private static final Logger nonPrefixLogger = LogManager.getLogger();
    private static final MutableText PREFIX = new LiteralText("[").formatted(Formatting.DARK_GRAY)
            .append("ServerStatus").formatted(Formatting.GOLD).append("]").formatted(Formatting.DARK_GRAY);

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
        log(msg, Level.WARN, prefix);
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
            logger.log(level, msg);
        } else {
            nonPrefixLogger.log(level, msg);
        }
    }

    /**
     * 向命令发送者发送可翻译消息
     * 
     * @param scs        发送者
     * @param messageKey 可翻译消息的key值
     */
    public static void sendPlayerFeedback(ServerCommandSource scs, TranslatableKey messageKey, String... args) {
        sendPlayerFeedback(scs, messageKey, Formatting.WHITE, args);
    }

    /**
     * 向命令发送者发送可翻译消息
     * 
     * @param scs        发送者
     * @param message    可翻译消息的key值
     * @param formatting 可翻译消息的颜色
     */
    public static void sendPlayerFeedback(ServerCommandSource scs, TranslatableKey messageKey, Formatting formatting,
            String... args) {
        sendPlayerFeedback(scs, new TranslatableText(messageKey.getKey(), (Object) args).formatted(formatting));
    }

    /**
     * 向命令发送者发送消息
     * 
     * @param scs     发送者
     * @param message 消息
     */
    public static void sendPlayerFeedback(ServerCommandSource scs, Text message) {
        try {
            scs.getPlayer();
            scs.sendFeedback(PREFIX.append(message), false);
        } catch (CommandSyntaxException e) {

        } catch (NullPointerException e) {
            warning(e.getMessage());
        }
    }
}
