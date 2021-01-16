import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigTest {
    @SneakyThrows
    public static void main(String[] args) {
        Properties p=new Properties();
        InputStream inputStream=ConfigTest.class.getClassLoader().getResourceAsStream("config.properties");
        InputStreamReader inputStream1=new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        p.load(inputStream1);
        int qwq = Integer.parseInt(p.getProperty("qwq"));
        System.out.println(qwq);
    }
}
