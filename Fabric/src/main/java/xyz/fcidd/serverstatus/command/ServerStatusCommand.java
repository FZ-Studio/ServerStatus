package xyz.fcidd.serverstatus.command;

import net.minecraft.server.command.ServerCommandSource;
import xyz.fcidd.serverstatus.handler.command.ServerStatusCommandHandler;

import com.mojang.brigadier.CommandDispatcher;
import static com.mojang.brigadier.arguments.IntegerArgumentType.*;
import static com.mojang.brigadier.arguments.StringArgumentType.*;

import static net.minecraft.server.command.CommandManager.*;

public class ServerStatusCommand {

    /**
     * 注册命令
     * 
     * @param dispatcher 注册器
     */
    public static void bgServerStatus(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(
            literal("bgserverstatus")
            .requires(ServerStatusCommandHandler::mainRequires)
            .then(
                literal("setport")
                .then(
                    argument("port", integer(0, 65535))
                    .executes(c -> ServerStatusCommandHandler.setPort(c, getInteger(c, "port")))
                )
                .executes(c -> ServerStatusCommandHandler.setPort(c, ServerStatusCommandHandler.RESET_PORT))
            )
            .then(
                literal("sethost")
                .then(
                    argument("host", string())
                    .executes(c -> ServerStatusCommandHandler.setHost(c, getString(c, "host")))
                )
                .executes(c -> ServerStatusCommandHandler.setHost(c, ServerStatusCommandHandler.RESET_HOST))
            )
            .then(
                literal("reload")
                .executes(ServerStatusCommandHandler::reload)
            )
            .then(
                literal("help")
                .executes(ServerStatusCommandHandler::help)
            )
            .then(
                literal("message")
                .then(
                    literal("start")
                    .executes(ServerStatusCommandHandler::startMessage)
                )
                .then(
                    literal("close")
                    .executes(ServerStatusCommandHandler::closeMessage)
                )
                .then(
                    literal("custom").then(
                        argument("customMessage", greedyString())
                        .executes(c -> ServerStatusCommandHandler.customMessage(c, getString(c, "customMessage")))
                    )
                )
            )
            .executes(ServerStatusCommandHandler::help)
        );
    }
}
