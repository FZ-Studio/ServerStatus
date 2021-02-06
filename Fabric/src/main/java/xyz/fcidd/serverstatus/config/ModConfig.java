package xyz.fcidd.serverstatus.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "server_status")
public class ModConfig implements ConfigData {
	public static final int DEFAULT_PORT = 556;
	public static final String DEFAULT_HOST = "localhost";

	@Comment(value = "连接bc的serverStatus的内置服务器ip及端口，ip默认为localhost，端口默认为556")
	private String socketIp = "localhost";
	private int socketPort = 556;
	@Comment(value = "是否允许4级op使用指令")
	private boolean allowOpCommand = false;

	public String getSocketIp() {
		return socketIp;
	}

	public void setSocketIp(String socketIp) {
		this.socketIp = socketIp;
	}

	public int getSocketPort() {
		return socketPort;
	}

	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	public boolean getAllowOpCommand() {
		return allowOpCommand;
	}

}
