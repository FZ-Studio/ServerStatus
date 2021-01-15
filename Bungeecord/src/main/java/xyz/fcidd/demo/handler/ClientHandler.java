package xyz.fcidd.demo.handler;

import lombok.SneakyThrows;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class ClientHandler implements Runnable {

    private ServerSocket server;
    private Plugin plugin;

    public ClientHandler(ServerSocket server, Plugin plugin) {
        this.server=server;
        this.plugin = plugin;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            //循环接收
            Socket socket = server.accept();
            //读取后端发送过来的数据
            InputStream in = socket.getInputStream();
            //将数据类型设置为utf-8编码
            InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
            //将后端的数据放入缓冲区
            BufferedReader br = new BufferedReader(isr);
            String message;
            while ((message = br.readLine()) != null) {
                //将消息输出
                //System.out.println(message);
                Collection<ProxiedPlayer> players = plugin.getProxy().getPlayers();
                String finalMessage = message;
                System.out.println(finalMessage);
                players.forEach(player -> player.sendMessage(finalMessage));
            }
            //关闭本次会话
            socket.close();
        }
    }
}
