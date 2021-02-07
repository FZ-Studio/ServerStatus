package xyz.fcidd.serverstatus;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.serverstatus.command.ServerStatusCommand;
import xyz.fcidd.serverstatus.config.PluginConfig;
import xyz.fcidd.serverstatus.server.StartServer;
import xyz.fcidd.serverstatus.util.IMessenger;

/**
 * 插件主类
 */
public final class ServerStatus extends Plugin {
    @Getter
    private static ServerStatus instance;

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
        StartServer.initialize(this);
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
        StartServer.stopServer();
        // 已停用
        IMessenger.info("§2ServerStatus已停用！");
    }
}
