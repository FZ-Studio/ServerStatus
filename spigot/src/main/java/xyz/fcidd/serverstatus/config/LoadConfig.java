package xyz.fcidd.serverstatus.config;

import xyz.fcidd.serverstatus.connect.ConnectServer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LoadConfig {
    private static Properties config;

    static {
        try {
            config = new Properties();
            InputStream input = new FileInputStream("./plugins/ServerStatus/config.properties");
            InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            config.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Properties getConfig(){
        try {
            config = new Properties();
            InputStream input = new FileInputStream("./plugins/ServerStatus/config.properties");
            InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            config.load(reader);
            input.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public static void connectServer() {
        getConfig();
        String serverIp = config.getProperty("serverStatus-server.ip");
        int port = 0;
        int index = serverIp.lastIndexOf(":");
        if (index == -1) {
            port = 556;
            new ConnectServer(serverIp, port);
        }else {
            String host = serverIp.substring(0, index).trim();
            port = Integer.parseInt(serverIp.substring(index + 1).trim());
            new ConnectServer(host,port);
        }

    }
}
