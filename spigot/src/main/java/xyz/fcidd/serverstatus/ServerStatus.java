package xyz.fcidd.serverstatus;

import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.fcidd.serverstatus.config.LoadConfig;
import xyz.fcidd.serverstatus.handler.ConfigFileHandler;

/**
 * 插件的主类
 */
public final class ServerStatus extends JavaPlugin {

    @SneakyThrows
    @Override
    public void onEnable() {
        //初始化配置文件
        ConfigFileHandler.initialize();
        //连接ServerStatus内置服务器
        LoadConfig.connectServer();
    }
}
