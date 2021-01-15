import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    private final Socket socket;

    @SneakyThrows
    public Client() {
        socket=new Socket("localhost",556);
    }
    @SneakyThrows
    public void start(){
        OutputStream out = socket.getOutputStream();
        OutputStreamWriter osw=new OutputStreamWriter(out,StandardCharsets.UTF_8);
        BufferedWriter bw=new BufferedWriter(osw);
        PrintWriter pw=new PrintWriter(bw,true);
        pw.print("§5§7xx服务器上线啦!");
        pw.close();
    }

    public static void main(String[] args) {
        Client client=new Client();
        client.start();
    }
}
