package xyz.fcidd.serverstatus.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import xyz.fcidd.serverstatus.ServerStatus;
import xyz.fcidd.serverstatus.config.PluginConfig;
import xyz.fcidd.serverstatus.util.IMessenger;

public class ISocketThread extends Thread {
    private final ProxyServer bcServer;
    private final Socket socket;

    public ISocketThread(Socket socket) {
        bcServer = ServerStatus.getInstance().getProxy();
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                // 读取后端发送过来的数据
                InputStream in = socket.getInputStream();
                // 将数据类型设置为utf-8编码
                InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
                // 将后端的数据放入缓冲区
                BufferedReader br = new BufferedReader(isr);) {
            String message;
            while ((message = br.readLine()) != null) {
                // 规范：<插件ID>.<子服务器端口号>.<命名空间>.[命名空间...].<自定义消息>
                // 因为Socket只能获取子服务器的ip地址，与BC配置文件对照时，还需要子服务器端口号，故需传入
                String[] finalmessage = message.split("\\.");
                if (finalmessage[0].equals("serverstatus")) {
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
                            switch (finalmessage[2]) {
                                case "start":
                                    sendServerStartedBroadcast(mcServerInfo.getName());
                                    break;
                                case "close":
                                    sendServerClosingBroadcast(mcServerInfo.getName());
                                    break;
                                case "message":
                                    switch (finalmessage[3]) {
                                        case "start":
                                            sendCustomBroadcast(mcServerInfo.getName(),
                                                    PluginConfig.getServerStartedBroadcast());
                                            break;
                                        case "close":
                                            sendCustomBroadcast(mcServerInfo.getName(),
                                                    PluginConfig.getServerClosingBroadcast());
                                            break;
                                        case "custom":
                                            if (finalmessage.length > 4) {
                                                StringBuilder messageSentBuilder = new StringBuilder();
                                                messageSentBuilder.append(finalmessage[4]);
                                                for (int i = 5; i < finalmessage.length; i++) {
                                                    messageSentBuilder.append(".");
                                                    messageSentBuilder.append(finalmessage[i]);
                                                }
                                                sendCustomBroadcast(mcServerInfo.getName(),
                                                        messageSentBuilder.toString());
                                            } else {
                                                unidentifiedMessageFormat(4, finalmessage);
                                            }
                                            break;
                                        default:
                                            unidentifiedMessageFormat(3, finalmessage);
                                            break;
                                    }
                                    break;
                                default:
                                    unidentifiedMessageFormat(2, finalmessage);
                                    break;
                            }
                            break;
                        }
                    }
                } else {
                    unidentifiedMessageFormat(0, finalmessage);
                }
            }
        } catch (Exception e) {

        }
    }

    private void unidentifiedMessageFormat(int position, String[] message) {
        StringBuilder stringBuilder = new StringBuilder();
        if (position == 0) {
            stringBuilder.append("§4");
        }
        stringBuilder.append(message[0]);
        for (int j = 1; j <= position; j++) {
            stringBuilder.append(".");
            if (j == position) {
                stringBuilder.append("§4");
            }
            stringBuilder.append(message[j]);
        }
        IMessenger.warning("§4无法处理的消息格式：§r" + stringBuilder.toString() + "§r<-§4此处");
    }

    /**
     * 发送服务器上线提醒
     * 
     * @param mcServerID MC服务器ID
     */
    private void sendServerStartedBroadcast(String mcServerID) {
        sendBroadcast(mcServerID, PluginConfig.getServerStartedBroadcast(), false);
    }

    /**
     * 发送服务器下线提醒
     * 
     * @param mcServerID MC服务器ID
     */
    private void sendServerClosingBroadcast(String mcServerID) {
        sendBroadcast(mcServerID, PluginConfig.getServerClosingBroadcast(), false);
    }

    /**
     * 发送自定义服务器消息
     * 
     * @param mcServerID MC服务器ID
     * @param message    自定义消息
     */
    private void sendCustomBroadcast(String mcServerID, String message) {
        sendBroadcast(mcServerID, message, true);
    }

    /**
     * 发送服务器消息
     * 
     * @param mcServerID      MC服务器ID
     * @param message         自定义消息
     * @param isCustomMessage 是否是自定义消息
     */
    private void sendBroadcast(String mcServerID, String message, boolean isCustomMessage) {
        IMessenger.info("§8[§6ServerStatus§8]§r -> §2{", false);
        message = message.replace("${server_translation}", PluginConfig.getTranslateServername(mcServerID));
        String[] massageArray = message.split("");
        for (int i = 0; i < massageArray.length; i++) {
            if (massageArray[i].equals("&")) {
                if (i == 0 || !massageArray[i - 1].equals("\\")) {
                    massageArray[i] = "§";
                } else {
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
        IMessenger.info("!!QQ serverstatus " + mcServerID + " " + message.replaceAll("[§][\\s\\S]", ""), false);
        if (isCustomMessage) {
            IMessenger.info("本条消息由游戏内命令发出", false);
        }
        IMessenger.info("§2}", false);
    }
}
