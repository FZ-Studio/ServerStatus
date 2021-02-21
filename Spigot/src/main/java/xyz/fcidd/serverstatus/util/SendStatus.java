package xyz.fcidd.serverstatus.util;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import xyz.fcidd.serverstatus.ServerStatus;

public class SendStatus implements Runnable {

    String action;
    String message;
    CommandSender sender;
    Plugin plugin;

    private SendStatus(String action, String message, CommandSender sender) {
        this.action = action;
        this.message = message;
        this.sender = sender;
        this.plugin = ServerStatus.getInstance();
    }

    private static void send(String action, String message, CommandSender sender) {
        SendStatus runnable = new SendStatus(action, message, sender);
        Thread thread = new Thread(runnable);
        // 将线程设置为守护线程
        thread.setDaemon(true);
        // 将线程启动
        thread.start();
    }

    public static void sendStart() {
        send("start", null, null);
    }

    public static void sendStartMessage(CommandSender sender) {
        send("message", "start", sender);
    }

    public static void sendClose() {
        send("close", null, null);
    }

    public static void sendCLoseMessage(CommandSender sender) {
        send("message", "close", sender);
    }

    public static void sendCustomMessage(String message) {
        send("message", "custom." + message, null);
    }

    public static void sendCustomMessage(String message, CommandSender sender) {
        send("message", "custom." + message, sender);
    }

    @Override
    public void run() {
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
            sendFeedback(sender, "§4连接服务器失败，请检查BC端是否启动，以及ip、端口是否正确",
                    Level.WARNING);
            return;
        } catch (IOException e) {
            sendFeedback(sender, "§4发送失败", Level.WARNING);
            e.printStackTrace();
            return;
        }
        sendFeedback(sender, "§2发送成功", Level.INFO);
    }

    private void sendFeedback(CommandSender sender, String message, Level level) {
        IMessenger.sendPlayerFeedback(sender, message);
        IMessenger.log(message, level);
    }
}
