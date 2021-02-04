package xyz.fcidd.serverstatus.server;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.bukkit.plugin.Plugin;

public class SenderServer {

    public static void send(Plugin plugin, String action, String message) {
        String host = plugin.getConfig().getString("socket-ip");
        int port = plugin.getConfig().getInt("socket-port");
        try (Socket socket = new Socket(host, port);
                OutputStream out = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                BufferedWriter bw = new BufferedWriter(osw);
                PrintWriter pw = new PrintWriter(bw, true);) {
            int mcPort = plugin.getServer().getPort();
            if (message == null) {
                pw.print(action + "." + mcPort);
            } else {
                pw.print(action + "." + mcPort + "." + message);
            }
        } catch (ConnectException e) {
            plugin.getLogger().info("§8[§6ServerStatus§8]§4连接服务器失败，请检查BC端是否启动，以及ip、端口是否正确");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        plugin.getLogger().info("§8[§6ServerStatus§8]§2发送成功！");
    }

    public static void sendStart(Plugin plugin) {
        send(plugin, "start", null);
    }

    public static void sendClose(Plugin plugin) {
        send(plugin, "close", null);
    }

    public static void sendMessage(Plugin plugin, String message) {
        send(plugin, "message", "custom." + message);
    }
}
