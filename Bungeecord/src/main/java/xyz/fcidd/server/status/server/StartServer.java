package xyz.fcidd.server.status.server;

import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.server.status.config.PluginConfig;
import xyz.fcidd.server.status.handler.ClientHandler;

import java.net.ServerSocket;

public class StartServer {
    private static ServerSocket server;
    private static Thread thread;
    private static Plugin plugin;

    /**
     * 初始化服务器
     * 
     * @param plugin 插件主类
     */
    @SneakyThrows
    public static void initialize(Plugin plugin) {
        StartServer.plugin = plugin;
        int port = PluginConfig.getSocketPort();
        // 启动服务器，端口为用户设定的port
        server = new ServerSocket(port);
        plugin.getProxy().getLogger().info("§8[§6ServerStatus§8]§2内置服务器已启动");
        // 将后端数据传入ClientHandler进行处理
        ClientHandler client = new ClientHandler(server, plugin);
        // 创建线程2
        thread = new Thread(client);
        // 将线程设置为守护线程
        thread.setDaemon(true);
        // 将线程启动
        thread.start();
    }

    /**
     * 关闭服务器
     */
    @SneakyThrows
    public static void stopServer() {
        thread.stop();
        server.close();
        plugin.getProxy().broadcast(new ComponentBuilder("§8[§6ServerStatus§8]§2内置服务器已关闭").create());
    }
}
