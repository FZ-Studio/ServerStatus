package xyz.fcidd.serverstatus.util;

import com.osiris.dyml.DYModule;
import com.osiris.dyml.DreamYaml;

import xyz.fcidd.serverstatus.ServerStatus;
import xyz.fcidd.serverstatus.config.PluginConfig;

public class Updater {
    public static void updateConfig(String currentVersion) throws Exception {
        switch (currentVersion) {
            // 1.0.0
            case "":
                DreamYaml config = PluginConfig.getConfig();
                DYModule serverClosedBroadcast = config.add("server-closed-broadcast");
                PluginConfig.setServerClosingBroadcast(config.getAddedModuleByKeys("server-closed-broadcast")
                        .setKey(PluginConfig.SERVER_CLOSING_BROADCAST).getValue());
                config.remove(serverClosedBroadcast);
                config.save();
                break;
            // 3.0.1
            case "3.0.1":
                break;
            default:
                break;
        }
        PluginConfig.setVersion(ServerStatus.getInstance().getVersion());
    }
}
