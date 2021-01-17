package xyz.fcidd.server.status.config;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LoadConfig {
    public static Properties config;
    public static InputStream input;
    public static InputStreamReader finalInput;
    public static OutputStream out;
    public static OutputStreamWriter writer;

    static {
        try {
            //创建读取配置文件的对象
            config=new Properties();
            //使用类加载读取配置文件
            input = new FileInputStream("./plugins/ServerStatus/config.properties");
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
        input = new FileInputStream("./plugins/ServerStatus/config.properties");
        finalInput= new InputStreamReader(input,StandardCharsets.UTF_8);
        config.load(finalInput);
        input.close();
        finalInput.close();
    }

    /**
     * 读取配置文件的端口
     * @return 配置文件的端口
     */
    public static String getPort(){
        return config.getProperty("server.port");
    }

    /**
     * 设置配置文件的端口
     * @param port 配置文件的端口
     */
    @SneakyThrows
    public static void setPort(String port){
        input = new FileInputStream("./plugins/ServerStatus/config.properties");
        finalInput= new InputStreamReader(input,StandardCharsets.UTF_8);
        out=new FileOutputStream("./plugins/ServerStatus/config.properties");
        writer=new OutputStreamWriter(out, StandardCharsets.UTF_8);
        config.load(finalInput);
        config.setProperty("server.port",port);
        config.store(writer,null);
        input.close();
        finalInput.close();
        out.close();
        writer.close();
    }
}
