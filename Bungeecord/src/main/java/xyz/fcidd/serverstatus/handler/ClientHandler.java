package xyz.fcidd.serverstatus.handler;

import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.serverstatus.config.PluginConfig;
import xyz.fcidd.serverstatus.util.IMessenger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class ClientHandler implements Runnable {

    private final ServerSocket server;
    private final ProxyServer bcServer;
    private boolean condition;
    private boolean running;

    /**
     * 关闭该线程
     */
    public void stop() {
        this.condition = false;
        while (running) {

        }
    }

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
        this.running = true;
        this.condition = true;
        while (this.condition) {
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
                String[] finalmessage = message.split("\\.");
                String hostSent = socket.getInetAddress().getHostName();
                Collection<ServerInfo> servers = bcServer.getConfig().getServers().values();
                for (ServerInfo mcServerInfo : servers) {
                    InetSocketAddress mcServerAddress = mcServerInfo.getAddress();
                    String host = mcServerAddress.getHostName();
                    if (host.equals("localhost")) {
                        host = "127.0.0.1";
                    }
                    int port = mcServerAddress.getPort();
                    if (host.equals(hostSent) && port == Integer.parseInt(finalmessage[1])) {
                        String translateServerName = PluginConfig.getTranslateServername(mcServerInfo.getName());
                        switch (finalmessage[0]) {
                            case "start":
                                sendServerStartedBroadcast(translateServerName, bcServer);
                                break;
                            case "close":
                                sendServerClosingBroadcast(translateServerName, bcServer);
                                break;
                            case "message":
                                switch (finalmessage[2]) {
                                    case "start":
                                        sendCustomBroadcast(translateServerName, bcServer,
                                                PluginConfig.getServerStartedBroadcast());
                                        break;
                                    case "close":
                                        sendCustomBroadcast(translateServerName, bcServer,
                                                PluginConfig.getServerClosingBroadcast());
                                        break;
                                    case "custom":
                                        if (finalmessage.length > 3) {
                                            StringBuilder messageSentBuilder = new StringBuilder();
                                            messageSentBuilder.append(finalmessage[3]);
                                            for (int i = 4; i < finalmessage.length; i++) {
                                                messageSentBuilder.append(".");
                                                messageSentBuilder.append(finalmessage[i]);
                                            }
                                            sendCustomBroadcast(translateServerName, bcServer,
                                                    messageSentBuilder.toString());
                                        } else {
                                            bcServer.getLogger().warning("§8[§6ServerStatus§8]§4无法处理的消息格式");
                                        }
                                        break;
                                    default:
                                        bcServer.getLogger().warning("§8[§6ServerStatus§8]§4无法处理的消息格式");
                                        break;
                                }
                                break;
                            default:
                                bcServer.getLogger().warning("§8[§6ServerStatus§8]§4无法处理的消息格式");
                                break;
                        }
                        break;
                    }
                }
            }
            // 关闭本次会话
            socket.close();
        }
        this.running = false;
    }

    /**
     * 发送服务器上线提醒
     * 
     * @param translateServerName 从serverID翻译得来的server名称字符串
     * @param bcServer            服务器
     */
    private static void sendServerStartedBroadcast(String translateServerName, ProxyServer bcServer) {
        sendBroadcast(translateServerName, bcServer, PluginConfig.getServerStartedBroadcast(), false);
    }

    /**
     * 发送服务器下线提醒
     * 
     * @param translateServerName 从serverID翻译得来的server名称字符串
     * @param bcServer            服务器
     */
    private static void sendServerClosingBroadcast(String translateServerName, ProxyServer bcServer) {
        sendBroadcast(translateServerName, bcServer, PluginConfig.getServerClosingBroadcast(), false);
    }

    /**
     * 发送自定义服务器消息
     * 
     * @param translateServerName 从serverID翻译得来的server名称字符串
     * @param bcServer            服务器
     * @param message             自定义消息
     */
    private static void sendCustomBroadcast(String translateServerName, ProxyServer bcServer, String message) {
        sendBroadcast(translateServerName, bcServer, message, true);
    }

    /**
     * 发送服务器消息
     * 
     * @param translateServerName 从serverID翻译得来的server名称字符串
     * @param bcServer            服务器
     * @param message             自定义消息
     * @param isCustomMessage     是否是自定义消息
     */
    private static void sendBroadcast(String translateServerName, ProxyServer bcServer, String message,
            boolean isCustomMessage) {
        IMessenger.info("§8[§6ServerStatus§8]§r -> §2{", false);
        message = message.replace("${server_translation}", translateServerName);
        String[] massageArray = message.split("");
        for (int i = 0; i < massageArray.length; i++) {
            if (massageArray[i].equals("&")) {
                if (i == 0 || !massageArray[i - 1].equals("\\")) {
                    massageArray[i] = "§";
                }else{
                    massageArray[i - 1] = "";
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : massageArray) {
            stringBuilder.append(string);
        }
        message = stringBuilder.toString();
        // 将消息发送给玩家
        bcServer.broadcast(new ComponentBuilder(message).create());
        IMessenger.info("!!QQ " + message.replaceAll("[§][\\s\\S]", ""), false);
        if (isCustomMessage) {
            IMessenger.info("本条消息由游戏内命令发出", false);
        }
        IMessenger.info("§2}", false);
    }
}
