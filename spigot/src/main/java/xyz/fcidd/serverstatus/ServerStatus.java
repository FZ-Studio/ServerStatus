package xyz.fcidd.serverstatus;

import lombok.SneakyThrows;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 插件的主类
 */
public final class ServerStatus extends JavaPlugin {

    @SneakyThrows
    @Override
    public void onEnable() {
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
        // 连接ServerStatus内置服务器 true -> start.
        new SendStatus(true, this);
    }

    @Override
    public void onDisable() {
        // 连接ServerStatus内置服务器 false -> close.
        new SendStatus(false, this);
    }
}
