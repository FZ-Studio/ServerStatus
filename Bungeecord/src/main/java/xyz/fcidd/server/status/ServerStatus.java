package xyz.fcidd.server.status;

import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.server.status.command.ServerStatusCommand;
import xyz.fcidd.server.status.config.PluginConfig;
import xyz.fcidd.server.status.server.StartServer;

/**
 * 插件主类
 */
public final class ServerStatus extends Plugin {

    @SneakyThrows
    @Override
    public void onEnable() {
        // 初始化配置文件
        PluginConfig.loadConfig(this);
        // 初始化服务器
        StartServer.initialize(this);
        // 注册指令
        getProxy().getPluginManager().registerCommand(this, new ServerStatusCommand(this));
        // 已加载
        getProxy().getConsole().sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§2ServerStatus已加载！").create());
    }

    @Override
    public void onDisable() {
        StartServer.stopServer();
        // 已停用
        getProxy().getConsole().sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§2ServerStatus已停用！").create());
    }
}
