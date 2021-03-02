package xyz.fcidd.serverstatus;

import java.io.IOException;

import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.serverstatus.command.ServerStatusCommand;
import xyz.fcidd.serverstatus.config.PluginConfig;
import xyz.fcidd.serverstatus.server.ISocketServer;
import xyz.fcidd.serverstatus.util.IMessenger;

/**
 * 插件主类
 */
public final class ServerStatus extends Plugin {

    private static ServerStatus instance;

    public static ServerStatus getInstance() {
        return instance;
    }

    /**
     * 继承并重写加载方法
     */
    @Override
    public void onEnable() {

        ServerStatus.instance = this;
        // 初始化配置文件
        try {
            PluginConfig.loadConfig(this);
            IMessenger.info("§2配置文件加载成功！");
        } catch (Exception e) {
            IMessenger.warning("§4配置文件加载失败！");
            e.printStackTrace();
            return;
        }
        // 初始化服务器
        try {
            ISocketServer.startServer();
        } catch (IOException e) {
            IMessenger.warning("§4内置服务器启动失败！");
            e.printStackTrace();
            return;
        }
        // 注册指令
        getProxy().getPluginManager().registerCommand(this, new ServerStatusCommand(this));
        // 已加载
        IMessenger.info("§2ServerStatus已加载！");
    }

    /**
     * 继承并重写卸载方法
     */
    @Override
    public void onDisable() {
        ISocketServer.stopServer();
        // 已停用
        IMessenger.info("§2ServerStatus已停用！");
    }
}
