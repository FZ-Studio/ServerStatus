package xyz.fcidd.serverstatus;

import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class ServerStatus extends JavaPlugin {

    @SneakyThrows
    @Override
    public void onEnable() {
        Socket socket = new Socket("localhost", 556);
        OutputStream out = socket.getOutputStream();
        OutputStreamWriter osw=new OutputStreamWriter(out, StandardCharsets.UTF_8);
        BufferedWriter bw=new BufferedWriter(osw);
        PrintWriter pw=new PrintWriter(bw,true);
        pw.print("§5§7xx服务器上线啦!");
        pw.close();
    }

}
