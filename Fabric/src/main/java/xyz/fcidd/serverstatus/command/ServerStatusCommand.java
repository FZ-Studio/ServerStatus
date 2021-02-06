package xyz.fcidd.serverstatus.command;

import net.minecraft.server.command.ServerCommandSource;
import xyz.fcidd.serverstatus.handler.CommandHandler;

import com.mojang.brigadier.CommandDispatcher;
import static com.mojang.brigadier.arguments.IntegerArgumentType.*;
import static com.mojang.brigadier.arguments.StringArgumentType.*;

import static net.minecraft.server.command.CommandManager.*;

public class ServerStatusCommand {
    

    public static void bgServerStatus(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(
            literal("bgserverstatus")
            .requires(CommandHandler::mainRequires)
            .then(
                literal("setport")
                .then(
                    argument("port", integer(0, 65535))
                    .executes(c -> CommandHandler.setPort(c, getInteger(c, "port")))
                )
                .executes(c -> CommandHandler.setPort(c, CommandHandler.RESET_PORT))
            )
            .then(
                literal("sethost")
                .then(
                    argument("host", string())
                    .executes(c -> CommandHandler.setHost(c, getString(c, "host")))
                )
                .executes(c -> CommandHandler.setHost(c, CommandHandler.RESET_HOST))
            )
            .then(
                literal("reload")
                .executes(CommandHandler::reload)
            )
            .then(
                literal("help")
                .executes(CommandHandler::help)
            )
            .then(
                literal("message")
                .then(
                    literal("start")
                    .executes(CommandHandler::startMessage)
                )
                .then(
                    literal("close")
                    .executes(CommandHandler::closeMessage)
                )
                .then(
                    literal("custom").then(
                        argument("customMessage", greedyString())
                        .executes(c -> CommandHandler.customMessage(c, getString(c, "customMessage")))
                    )
                )
            )
            .executes(CommandHandler::help)
        );
    }
}
