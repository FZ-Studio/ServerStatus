package xyz.fcidd.serverstatus.handler.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import xyz.fcidd.serverstatus.ServerStatus;
import xyz.fcidd.serverstatus.config.ModConfig;
import xyz.fcidd.serverstatus.translate.LangManager;
import xyz.fcidd.serverstatus.translate.TranslatableKey;
import xyz.fcidd.serverstatus.util.IMessenger;
import xyz.fcidd.serverstatus.util.IUtils;
import xyz.fcidd.serverstatus.util.SendStatus;

public class ServerStatusCommandHandler {

    public static final int RESET_PORT = -1;
    public static final String RESET = null;

    /**
     * 主命令的权限要求
     * 
     * @param requirement
     * @return
     */
    public static boolean mainRequires(ServerCommandSource requirement) {
        try {
            return requirement.getPlayer().hasPermissionLevel(4) && ServerStatus.getConfig().getAllowOpCommand();
        } catch (CommandSyntaxException e) {
            // 数据包有四级权限或使用控制台输入
            return requirement.hasPermissionLevel(4);
        }
    }

    /**
     * 设置频道端口号
     * 
     * @param c          命令环境
     * @param socketPort 频道端口号
     * @return 命令返回值
     */
    public static int setPort(CommandContext<ServerCommandSource> c, int socketPort) {
        if (socketPort == RESET_PORT) {
            ServerStatus.getConfig().setSocketPort(ModConfig.DEFAULT_PORT);
            ServerStatus.getConfigHolder().save();
            commandFeedback(c.getSource(), LangManager.getTranslated(TranslatableKey.COMMAND_DEFAULT_PORT_SUCCEEDED,
                    String.valueOf(ModConfig.DEFAULT_PORT)));
            return 1;
        }
        ServerStatus.getConfig().setSocketPort(socketPort);
        ServerStatus.getConfigHolder().save();
        commandFeedback(c.getSource(),
                LangManager.getTranslated(TranslatableKey.COMMAND_SETPORT_SUCCEEDED, String.valueOf(socketPort)));
        return 1;
    }

    /**
     * 设置频道IP地址
     * 
     * @param c          命令环境
     * @param socketHost 频道IP地址
     * @return 命令返回值
     */
    public static int setLang(CommandContext<ServerCommandSource> c, String lang) {
        if (lang == RESET) {
            ServerStatus.getConfig().setLang(ModConfig.DEFAULT_LANG);
            ServerStatus.getConfigHolder().save();
            commandFeedback(c.getSource(),
                    LangManager.getTranslated(TranslatableKey.COMMAND_DEFAULT_LANG_SUCCEEDED, ModConfig.DEFAULT_LANG));
            return 1;
        } else {
            if (ServerStatus.getConfig().setLang(lang)) {
                ServerStatus.getConfigHolder().save();
                commandFeedback(c.getSource(), LangManager.getTranslated(TranslatableKey.COMMAND_SETLANG_SUCCEEDED, lang));
                return 1;
            } else {
                IMessenger.sendPlayerFeedback(c.getSource(),
                        LangManager.getTranslated(TranslatableKey.COMMAND_SETLANG_WRONG_FORMAT));
                return 0;
            }
        }
    }

    /**
     * 设置频道IP地址
     * 
     * @param c          命令环境
     * @param socketHost 频道IP地址
     * @return 命令返回值
     */
    public static int setHost(CommandContext<ServerCommandSource> c, String socketHost) {
        if (socketHost == RESET) {
            ServerStatus.getConfig().setSocketIp(ModConfig.DEFAULT_HOST);
            ServerStatus.getConfigHolder().save();
            commandFeedback(c.getSource(),
                    LangManager.getTranslated(TranslatableKey.COMMAND_DEFAULT_HOST_SUCCEEDED, ModConfig.DEFAULT_HOST));
            return 1;
        } else if (IUtils.isHost(socketHost)) {
            ServerStatus.getConfig().setSocketIp(socketHost);
            ServerStatus.getConfigHolder().save();
            commandFeedback(c.getSource(), LangManager.getTranslated(TranslatableKey.COMMAND_SETHOST_SUCCEEDED, socketHost));
            return 1;
        }
        IMessenger.sendPlayerFeedback(c.getSource(), LangManager.getTranslated(TranslatableKey.COMMAND_SETHOST_WRONG_FORMAT));
        return 0;
    }

    /**
     * 重载配置文件
     * 
     * @param c 命令环境
     * @return 命令返回值
     */
    public static int reload(CommandContext<ServerCommandSource> c) {
        ConfigHolder<ModConfig> config = ServerStatus.getConfigHolder();
        config.load();
        ServerStatus.setConfig(config.get());
        commandFeedback(c.getSource(), LangManager.getTranslated(TranslatableKey.COMMAND_RELOAD_SUCCEEDED));
        return 1;
    }

    /**
     * 发送命令帮助
     * 
     * @param c
     * @return
     */
    public static int help(CommandContext<ServerCommandSource> c) {
        LiteralText text = new LiteralText(LangManager.getTranslated(TranslatableKey.COMMAND_HELP));
        try {
            c.getSource().getPlayer();
            IMessenger.sendPlayerFeedback(c.getSource(), text);
        } catch (CommandSyntaxException e) {
            IMessenger.info("\n" + text.getString());
        }
        return 1;
    }

    /**
     * 发送服务器上线提醒
     * 
     * @param c 命令环境
     * @return 命令返回值
     */
    public static int startMessage(CommandContext<ServerCommandSource> c) {
        SendStatus.sendStartMessage(c.getSource());
        return 1;
    }

    /**
     * 发送服务器下线提醒
     * 
     * @param c 命令环境
     * @return 命令返回值
     */
    public static int closeMessage(CommandContext<ServerCommandSource> c) {
        SendStatus.sendCLoseMessage(c.getSource());
        return 1;
    }

    /**
     * 发送自定义消息
     * 
     * @param c       命令环境
     * @param message 自定义消息
     * @return 命令返回值
     */
    public static int customMessage(CommandContext<ServerCommandSource> c, String message) {
        SendStatus.sendCustomMessage(message, c.getSource());
        return 1;
    }

    /**
     * 发送命令反馈
     * 
     * @param scs                 命令发送者
     * @param TranslatableMessage 可翻译的key
     * @param args                翻译文本中的填充字符
     * @return 命令返回值
     */
    private static void commandFeedback(ServerCommandSource scs, String message) {
        IMessenger.sendPlayerFeedback(scs, message);
        IMessenger.info(message);
    }
}