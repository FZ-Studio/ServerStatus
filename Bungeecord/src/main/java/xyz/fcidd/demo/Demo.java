package xyz.fcidd.demo;

import lombok.SneakyThrows;
import net.md_5.bungee.api.plugin.Plugin;
import xyz.fcidd.demo.handler.ClientHandler;

import java.net.ServerSocket;

public final class Demo extends Plugin {

    @SneakyThrows
    @Override
    public void onEnable() {
        System.out.println("正在启动内置服务器，由于检测服务器");
        //启动服务器，端口为556
        ServerSocket server = new ServerSocket(556);
        System.out.println("启动服务器完成");
        ClientHandler client = new ClientHandler(server, this);
        Thread thread = new Thread(client);
        thread.setDaemon(true);
        thread.start();
    }


}
