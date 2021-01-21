package xyz.fcidd.server.status.handler;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.server.status.config.PluginConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {

    private final ServerSocket server;
    private final ProxyServer bcServer;

    /**
     * 将sever和plugin传进本类做进一步的处理
     * 
     * @param server 内置服务器
     * @param plugin 插件主类
     */
    public ClientHandler(ServerSocket server, Plugin plugin) {
        this.server = server;
        this.bcServer = plugin.getProxy();
    }

    /**
     * 多线程重写run方法
     */
    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            // 循环接收
            Socket socket = server.accept();
            // 读取后端发送过来的数据
            InputStream in = socket.getInputStream();
            // 将数据类型设置为utf-8编码
            InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
            // 将后端的数据放入缓冲区
            BufferedReader br = new BufferedReader(isr);
            String message;
            while ((message = br.readLine()) != null) {
                String[] finalMessage = message.split("\\.");
                bcServer.getConfig().getServers().values().forEach(mcServerInfo -> {
                    InetSocketAddress mcServerAddress = mcServerInfo.getAddress();
                    String host = mcServerAddress.getHostName();
                    if (host.equals("localhost")) {
                        host = "127.0.0.1";
                    }
                    int port = mcServerAddress.getPort();
                    if (host.equals(socket.getInetAddress().getHostName())
                            && port == Integer.parseInt(finalMessage[1])) {
                        String translatedServerName = PluginConfig.getTranslateServername(mcServerInfo.getName());
                        if (finalMessage[0].equals("start")) {
                            String broadcast = PluginConfig.getServerStartedBroadcast()
                                    .replace("${server_translation}", translatedServerName).replace("&", "§")
                                    .replace("\\§", "&");
                            bcServer.broadcast(new ComponentBuilder(broadcast).create());
                        } else if (finalMessage[0].equals("close")) {
                            String broadcast = PluginConfig.getServerClosedeBroadcast()
                                    .replace("${server_translation}", translatedServerName).replace("&", "§")
                                    .replace("\\§", "&");
                            bcServer.broadcast(new ComponentBuilder(broadcast).create());
                        }
                    }
                });
                // 将从后端发过来的消息发送给玩家
            }
            // 关闭本次会话
            socket.close();
        }
    }
}
