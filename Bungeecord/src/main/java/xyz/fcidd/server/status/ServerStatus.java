package xyz.fcidd.server.status;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.server.status.command.ServerStatusCommand;
import xyz.fcidd.server.status.handler.ConfigFileHandler;
import xyz.fcidd.server.status.server.StartServer;

/**
 * 插件主类
 */
public final class ServerStatus extends Plugin {

    @SneakyThrows
    @Override
    public void onEnable() {
        //初始化配置文件
        ConfigFileHandler.initialize();
        //初始化服务器
        StartServer.initialize(this);
        //注册指令
        ProxyServer.getInstance().getPluginManager().registerCommand(this,new ServerStatusCommand(this));
    }

}
