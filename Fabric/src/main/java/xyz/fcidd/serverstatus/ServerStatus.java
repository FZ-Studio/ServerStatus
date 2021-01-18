package xyz.fcidd.serverstatus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ServerStatus implements ModInitializer {
	private String serverIp;
	private int serverPort;
	private int mcPort;

	@Override
	public void onInitialize() {
		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
			serverIp = config.socketIp;
			serverPort = config.socketPort;
		});
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			mcPort = server.getServerPort();
			sendState(true);
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> sendState(false));
	}

	@SneakyThrows
	private void sendState(boolean b) {
		try (Socket socket = new Socket(serverIp, serverPort);
				OutputStream out = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
				BufferedWriter bw = new BufferedWriter(osw);
				PrintWriter pw = new PrintWriter(bw, true);) {
			pw.print(b ? "start." + mcPort : "close." + mcPort);
		} catch (ConnectException e) {
			System.err.println("[ServerStatus]连接服务器失败，请检查BC端是否启动，以及ip、端口是否正确");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

@Config(name = "server_status")
class ModConfig implements ConfigData {
	@Comment(value = "连接bc的serverStatus的内置服务器ip及端口，ip默认为localhost，端口默认为556")
	String socketIp = "localhost";
	int socketPort = 556;
}
