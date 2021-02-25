package xyz.fcidd.serverstatus.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import xyz.fcidd.serverstatus.translate.LangManager;

@Config(name = "server_status")
public class ModConfig implements ConfigData {
	public static final int DEFAULT_PORT = 556;
	public static final String DEFAULT_HOST = "localhost";
	public static final String DEFAULT_LANG = "zh_cn";

	@Comment(value = "连接bc的serverStatus的内置服务器ip及端口，ip默认为localhost，端口默认为556")
	private String socketIp = "localhost";
	private int socketPort = 556;
	@Comment(value = "是否允许4级op使用指令")
	private boolean allowOpCommand = false;
	@Comment(value = "语言(zh_cn)，默认为zh_cn")
	private String lang = "zh_cn";

	public String getLang() {
		return lang.toLowerCase();
	}

	public boolean setLang(String lang) {
		lang = lang.toLowerCase();
		if (checkLang(lang)) {
			this.lang = lang;
			return true;
		}
		return false;
	}

	public static boolean checkLang(String lang) {
		return LangManager.getLangsList().contains(lang);
	}

	/**
	 * 获取消息通道的IP
	 * 
	 * @return 消息通道的IP
	 */
	public String getSocketIp() {
		return socketIp;
	}

	/**
	 * 设置消息通道的IP
	 * 
	 * @param socketIp 消息通道的IP
	 */
	public void setSocketIp(String socketIp) {
		this.socketIp = socketIp;
	}

	/**
	 * 获取消息通道的端口
	 * 
	 * @return 消息通道的端口
	 */
	public int getSocketPort() {
		return socketPort;
	}

	/**
	 * 设置消息通道的端口
	 * 
	 * @param socketPort 消息通道的端口
	 */
	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	/**
	 * 获取是否允许op使用指令
	 * 
	 * @return 否允许op使用指令
	 */
	public boolean getAllowOpCommand() {
		return allowOpCommand;
	}

}
