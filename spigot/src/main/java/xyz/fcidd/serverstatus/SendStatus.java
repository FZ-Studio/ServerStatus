package xyz.fcidd.serverstatus;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.bukkit.plugin.Plugin;

public class SendStatus {

    private static Plugin plugin;

    public SendStatus(boolean b, Plugin plugin) {
        this.plugin = plugin;
        int mcPort = plugin.getServer().getPort();
        String host = plugin.getConfig().getString("socket-ip");
        int port = plugin.getConfig().getInt("socket-port");
        send(host, port, b, mcPort);
    }

    /**
     * 连接到serverStatus内置服务器
     *
     * @param host ip或域名
     * @param port 端口
     */
    public static void send(String host, int port, boolean b, int mcPort) {
        try (Socket socket = new Socket(host, port);
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw, true);) {
            pw.print(b ? "start." + mcPort : "close." + mcPort);
            
        } catch (ConnectException e) {
            plugin.getLogger().info("§8[§6ServerStatus§8]§4连接服务器失败，请检查BC端是否启动，以及ip、端口是否正确");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
