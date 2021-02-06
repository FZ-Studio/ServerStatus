package xyz.fcidd.serverstatus.handler;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import xyz.fcidd.serverstatus.ServerStatus;
import xyz.fcidd.serverstatus.config.ModConfig;
import xyz.fcidd.serverstatus.translate.TranslatableKey;
import xyz.fcidd.serverstatus.util.ILogger;
import xyz.fcidd.serverstatus.util.IUtils;
import xyz.fcidd.serverstatus.util.SendStatus;

public class CommandHandler {
    
	public static final int RESET_PORT = -1;
	public static final String RESET_HOST = null;

    public static boolean mainRequires(ServerCommandSource requirement) {
        try {
            return requirement.getPlayer().hasPermissionLevel(4) && ServerStatus.getConfig().getAllowOpCommand();
        } catch (CommandSyntaxException e) {
            // 数据包有四级权限或使用控制台输入
            return requirement.hasPermissionLevel(4);
        }
    }

    public static int setPort(CommandContext<ServerCommandSource> c, int socketPort) {
        if (socketPort == RESET_PORT) {
            ServerStatus.getConfig().setSocketPort(ModConfig.DEFAULT_PORT);
            ServerStatus.getConfigHolder().save();
            return commandFeedback(c.getSource(), TranslatableKey.DEFAULT_PORT_SUCCEEDED);
        }
        ServerStatus.getConfig().setSocketPort(socketPort);
        ServerStatus.getConfigHolder().save();
        return commandFeedback(c.getSource(), TranslatableKey.SET_PORT_SUCCEEDED);
    }

    public static int setHost(CommandContext<ServerCommandSource> c, String socketHost) {
        if (socketHost == RESET_HOST) {
            ServerStatus.getConfig().setSocketIp(ModConfig.DEFAULT_HOST);
            ServerStatus.getConfigHolder().save();
            return commandFeedback(c.getSource(), TranslatableKey.DEFAULT_HOST_SUCCEEDED);
        } else if (IUtils.isHost(socketHost)) {
            ServerStatus.getConfig().setSocketIp(socketHost);
            ServerStatus.getConfigHolder().save();
            return commandFeedback(c.getSource(), TranslatableKey.SET_HOST_SUCCEEDED);
        }
        return commandFeedback(c.getSource(), TranslatableKey.SET_HOST_WRONG_FORMAT);
    }

    public static int reload(CommandContext<ServerCommandSource> c) {
        ConfigHolder<ModConfig> config = ServerStatus.getConfigHolder();
        config.load();
        ServerStatus.setConfig(config.get());
        return commandFeedback(c.getSource(), TranslatableKey.RELOAD_SUCCEEDED);
    }

    public static int help(CommandContext<ServerCommandSource> c) {
        return commandFeedback(c.getSource(), TranslatableKey.HELP);
    }

    public static int startMessage(CommandContext<ServerCommandSource> c) {
        SendStatus.sendStartMessage(c.getSource());
        return 1;
    }

    public static int closeMessage(CommandContext<ServerCommandSource> c) {
        SendStatus.sendCLoseMessage(c.getSource());
        return 1;
    }

    public static int customMessage(CommandContext<ServerCommandSource> c, String message) {
        SendStatus.sendCustomMessage(message, c.getSource());
        return 1;
    }

    public static int commandFeedback(ServerCommandSource scs, TranslatableKey feedback, String... args) {
        TranslatableText text = new TranslatableText(feedback.getKey(), (Object[]) args);
        scs.sendFeedback(text, false);
        ILogger.info(text.getString());
        return feedback.getReturnNum();
    }
}