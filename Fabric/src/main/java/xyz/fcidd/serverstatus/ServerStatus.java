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

	public static ServerStatus getInstance() {
		return instance;
	}

	public static int getMcPort() {
		return mcPort;
	}

	public static ConfigHolder<ModConfig> getConfigHolder() {
		return configHolder;
	}

	public static ModConfig getConfig() {
		return config;
	}

	public static void setConfig(ModConfig config) {
		ServerStatus.config = config;
	}

	public static MinecraftServer getServer() {
		return server;
	}

	@Override
	public void onInitialize() {
		ServerStatus.instance = this;
		AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
		ServerStatus.configHolder = AutoConfig.getConfigHolder(ModConfig.class);
		ServerStatus.config = ServerStatus.configHolder.getConfig();
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			if (dedicated) {
				ServerStatusCommand.bgServerStatus(dispatcher);
			}
		});
		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			ServerStatus.server = server;
		});
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			ServerStatus.mcPort = server.getServerPort();
			SendStatus.sendStart();
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> SendStatus.sendClose());
	}
}
