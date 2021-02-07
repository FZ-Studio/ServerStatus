package xyz.fcidd.serverstatus;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import xyz.fcidd.serverstatus.command.ServerStatusCommand;
import xyz.fcidd.serverstatus.config.ModConfig;
import xyz.fcidd.serverstatus.util.SendStatus;

public class ServerStatus implements ModInitializer {
	private static ServerStatus instance;
	private static int mcPort;
	private static ConfigHolder<ModConfig> configHolder;
	private static ModConfig config;
	private static MinecraftServer server;

	/**
	 * 获取所有mod方法
	 * 
	 * @return mod
	 */
	public static ServerStatus getInstance() {
		return instance;
	}

	/**
	 * 获取服务器端口
	 * 
	 * @return 服务器端口
	 */
	public static int getMcPort() {
		return mcPort;
	}

	/**
	 * 获取配置文件容器
	 * 
	 * @return 配置文件容器
	 */
	public static ConfigHolder<ModConfig> getConfigHolder() {
		return configHolder;
	}

	/**
	 * 获取mod配置
	 * 
	 * @return mod配置
	 */
	public static ModConfig getConfig() {
		return config;
	}

	/**
	 * 设置mod配置
	 * 
	 * @param config
	 */
	public static void setConfig(ModConfig config) {
		ServerStatus.config = config;
	}

	/**
	 * 获取所有服务器方法
	 * 
	 * @return 服务器
	 */
	public static MinecraftServer getServer() {
		return server;
	}

	/**
	 * 主方法
	 */
	@Override
	public void onInitialize() {
		ServerStatus.instance = this;
		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		ServerStatus.configHolder = AutoConfig.getConfigHolder(ModConfig.class);
		ServerStatus.config = ServerStatus.configHolder.getConfig();
		// 注册命令
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			if (dedicated) {
				ServerStatusCommand.bgServerStatus(dispatcher);
			}
		});
		// 正在开服
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			// 传递服务器
			ServerStatus.server = server;
		});
		// 开服完成
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			// 传递端口号
			ServerStatus.mcPort = server.getServerPort();
			SendStatus.sendStart();
		});
		// 正在关服
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> SendStatus.sendClose());
	}
}
