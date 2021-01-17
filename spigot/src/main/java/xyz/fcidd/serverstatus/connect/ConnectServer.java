package xyz.fcidd.serverstatus.connect;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ConnectServer {
    /**
     * @param host ip或域名
     * @param port 端口
     */
    public ConnectServer(String host, int port) {
        connect(host, port);
    }

    /**
     * 连接到serverStatus内置服务器
     *
     * @param host ip或域名
     * @param port 端口
     */
    public void connect(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            OutputStream out = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw, true);
            pw.print("§7xx服务器上线啦!");
            pw.close();
        } catch (ConnectException e) {
            System.out.println("连接服务器失败,请检查ip或端口是否正确");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
