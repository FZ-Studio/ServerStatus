package xyz.fcidd.server.status.server;

import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.server.status.config.LoadConfig;
import xyz.fcidd.server.status.handler.ClientHandler;

import java.net.ServerSocket;

public class StartServer {

    /**
     * 将插件的主类传进本类
     * @param plugin 插件主类
     */
    public StartServer(Plugin plugin) {
        initialize(plugin);
    }

    /**
     * 初始化服务器
     * @param plugin 插件主类
     */
    @SneakyThrows
    public static void initialize(Plugin plugin) {
        int port = Integer.parseInt(LoadConfig.getPort());
        System.out.println("[ServerStatus]正在启动内置服务器，用于检测服务器，端口为" + port);
        //启动服务器，端口为用户设定的port
        ServerSocket server = new ServerSocket(port);
        System.out.println("[ServerStatus]启动内置服务器完成");
        //将后端数据传入ClientHandler进行处理
        ClientHandler client = new ClientHandler(server, plugin);
        //创建线程
        Thread thread = new Thread(client);
        //将线程设置为守护线程
        thread.setDaemon(true);
        //将线程启动
        thread.start();
    }
}
