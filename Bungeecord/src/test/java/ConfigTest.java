import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigTest {
    @SneakyThrows
    public static void main(String[] args) {
        Properties pro=new Properties();
        InputStream input = new FileInputStream("./plugins/ServerStatus/config.properties");
        InputStreamReader finalInput=new InputStreamReader(input);
        OutputStream out=new FileOutputStream("./plugins/ServerStatus/config.properties");
        OutputStreamWriter writer=new OutputStreamWriter(out, StandardCharsets.UTF_8);
        pro.load(finalInput);
        pro.setProperty("server.port","999");
        pro.store(writer,"");
    }
}
