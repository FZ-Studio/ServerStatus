package xyz.fcidd.serverstatus.handler.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import xyz.fcidd.serverstatus.ServerStatus;
import xyz.fcidd.serverstatus.config.ModConfig;
import xyz.fcidd.serverstatus.translate.TranslatableMessage;
import xyz.fcidd.serverstatus.util.IMessenger;
import xyz.fcidd.serverstatus.util.IUtils;
import xyz.fcidd.serverstatus.util.SendStatus;

public class ServerStatusCommandHandler {

    public static final int RESET_PORT = -1;
    public static final String RESET_HOST = null;

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
            return commandFeedback(c.getSource(), TranslatableMessage.COMMAND_SUCCEEDED_DEFAULT_PORT);
        }
        ServerStatus.getConfig().setSocketPort(socketPort);
        ServerStatus.getConfigHolder().save();
        return commandFeedback(c.getSource(), TranslatableMessage.COMMAND_SUCCEEDED_SET_PORT);
    }

    /**
     * 设置频道IP地址
     * 
     * @param c          命令环境
     * @param socketHost 频道IP地址
     * @return 命令返回值
     */
    public static int setHost(CommandContext<ServerCommandSource> c, String socketHost) {
        if (socketHost == RESET_HOST) {
            ServerStatus.getConfig().setSocketIp(ModConfig.DEFAULT_HOST);
            ServerStatus.getConfigHolder().save();
            return commandFeedback(c.getSource(), TranslatableMessage.COMMAND_SUCCEEDED_DEFAULT_HOST);
        } else if (IUtils.isHost(socketHost)) {
            ServerStatus.getConfig().setSocketIp(socketHost);
            ServerStatus.getConfigHolder().save();
            return commandFeedback(c.getSource(), TranslatableMessage.COMMAND_SUCCEEDED_SET_HOST);
        }
        return commandFeedback(c.getSource(), TranslatableMessage.COMMAND_WRONG_FORMAT_SET_HOST);
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
        return commandFeedback(c.getSource(), TranslatableMessage.COMMAND_SUCCEEDED_RELOAD);
    }

    /**
     * 发送命令帮助
     * 
     * @param c
     * @return
     */
    public static int help(CommandContext<ServerCommandSource> c) {
        TranslatableText text = new TranslatableText(TranslatableMessage.COMMAND_HELP.getStringKey());
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
     * @param scs             命令发送者
     * @param TranslatableMessage 可翻译的key
     * @param args            翻译文本中的填充字符
     * @return 命令返回值
     */
    private static int commandFeedback(ServerCommandSource scs, TranslatableMessage TranslatableMessage, String... args) {
        TranslatableText text = new TranslatableText(TranslatableMessage.getStringKey(), (Object[]) args);
        IMessenger.sendPlayerFeedback(scs, text);
        IMessenger.info(text.getString());
        return TranslatableMessage.getReturnNum();
    }
}