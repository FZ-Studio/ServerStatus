package xyz.fcidd.server.status.config;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LoadConfig {
    private static Properties config;
    private static InputStreamReader finalInput;

    static {
        try {
            //创建读取配置文件的对象
            config=new Properties();
            //使用类加载读取配置文件
            InputStream input = new FileInputStream("./plugins/ServerStatus/config.properties");
            //将读取的配置文件转为UTF-8编码
            finalInput = new InputStreamReader(input, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重载配置文件
     */
    @SneakyThrows
    public static void reloadConfig(){
        config.load(finalInput);
    }

    /**
     * 读取配置文件的端口
     * @return 配置文件的端口
     */
    public static String getPort(){
        return config.getProperty("server.port");
    }
}
