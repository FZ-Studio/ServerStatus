package xyz.fcidd.server.status.handler;

import lombok.SneakyThrows;
import xyz.fcidd.server.status.config.LoadConfig;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class ConfigFileHandler {
    /**
     * 初始化配置文件
     */
    @SneakyThrows
    public static void initialize() {
        //配置文件夹途径
        File configMike = new File("./plugins/ServerStatus/");
        //配置文件夹的文件
        File configFile = new File("./plugins/ServerStatus/config.properties");
        //读取resource目录下的config.properties
        InputStream resource = ConfigFileHandler.class.getClassLoader().getResourceAsStream("config.properties");
        //以10Kb的速度写入
        byte[] data = new byte[1024 * 10];
        int len;
        //如果配置文件夹不存在
        if (!configFile.exists()) {
            //创建配置文件夹
            configMike.mkdirs();
            //将文件输出到目标文件夹
            RandomAccessFile raf = new RandomAccessFile("./plugins/ServerStatus/config.properties", "rw");
            //将数据写入进去
            while ((len = resource.read(data)) != -1) {
                raf.write(data, 0, len);
            }
            //解除文件占用
            resource.close();
            raf.close();
        } else {
            //读取配置文件的port
            String portConfig = LoadConfig.getPort();
            RandomAccessFile raf = new RandomAccessFile("./plugins/ServerStatus/config.properties", "rw");
            //如果端口为空
            if (portConfig == null) {
                //将指针移到末尾
                raf.seek(raf.length());
                //读取58字节
                data=new byte[58];
                len=resource.read(data);
                //将读到的数据写入文件
                raf.write(data,0,len);
            }
            //解除文件占用
            resource.close();
            raf.close();
            //重载配置文件
            LoadConfig.reloadConfig();
        }
    }
}
