package xyz.fcidd.serverstatus;

import xyz.fcidd.serverstatus.command.ServerStatusCommands;
import xyz.fcidd.serverstatus.util.SendStatus;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 插件的主类
 */
public final class ServerStatus extends JavaPlugin {
    
    private static ServerStatus instance;

    public static ServerStatus getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        ServerStatus.instance = this;
        // 初始化配置文件
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
        // 注册命令
        getCommand("bgserverstatus").setExecutor(new ServerStatusCommands());
        // 连接ServerStatus内置服务器
        SendStatus.sendStart();
    }

    @Override
    public void onDisable() {
        // 连接ServerStatus内置服务器
        SendStatus.sendClose();
    }
}
