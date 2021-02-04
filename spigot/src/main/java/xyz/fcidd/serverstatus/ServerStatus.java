package xyz.fcidd.serverstatus;

import lombok.Getter;
import lombok.SneakyThrows;
import xyz.fcidd.serverstatus.command.ServerStatusCommands;
import xyz.fcidd.serverstatus.server.SenderServer;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 插件的主类
 */
public final class ServerStatus extends JavaPlugin {
    @Getter
    private static ServerStatus instance;

    @SneakyThrows
    @Override
    public void onEnable() {
        this.instance = this;
        getConfig().options().header("连接bc端本插件的内置服务器ip及端口，ip默认为localhost，端口默认为556");
        if (!getConfig().contains("socket-ip")) {
            getConfig().addDefault("socket-ip", null);
            getConfig().set("socket-ip", "localhost");
        }
        if (!getConfig().contains("socket-port")) {
            getConfig().addDefault("socket-port", null);
            getConfig().set("socket-port", 556);
        }
        saveConfig();
        getCommand("bgserverstatus").setExecutor(new ServerStatusCommands());
        // 连接ServerStatus内置服务器
        SenderServer.sendStart(this);
    }

    @Override
    public void onDisable() {
        // 连接ServerStatus内置服务器
        SenderServer.sendClose(this);
    }
}
