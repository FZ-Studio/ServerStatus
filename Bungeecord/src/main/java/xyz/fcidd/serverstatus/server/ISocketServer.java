package xyz.fcidd.serverstatus.server;

import xyz.fcidd.serverstatus.config.PluginConfig;
import xyz.fcidd.serverstatus.util.IMessenger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ISocketServer implements Runnable {

    protected static IOException e1;

    private static Thread socketServer;
    private static ServerSocket server;
    private static Socket socket;

    @Override
    public void run() {
        try {
            server = new ServerSocket(PluginConfig.getSocketPort());
            // 循环接收
            while (true) {
                socket = server.accept();
                new ISocketThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        // 无所谓~
        try {
            server.close();
            IMessenger.info("§2内置服务器正在关闭");
        } catch (IOException e) {
            IMessenger.warning("§4关闭内置服务器时出现了一些错误");
        }
        socketServer.stop();
    }

    public static void startServer() throws IOException {
        e1 = null;
        // 创建线程
        socketServer = new Thread(new ISocketServer());
        // 将线程设置为守护线程
        socketServer.setDaemon(true);
        // 将线程启动
        socketServer.start();
        IMessenger.info("§2内置服务器正在启动");
    }
}
