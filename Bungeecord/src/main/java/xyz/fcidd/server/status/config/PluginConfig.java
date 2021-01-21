package xyz.fcidd.server.status.config;

import java.io.File;
import java.util.List;

import com.osiris.dyml.DreamYaml;

import net.md_5.bungee.api.plugin.Plugin;

public class PluginConfig {

    public static DreamYaml config;

    public PluginConfig(Plugin plugin) throws Exception {
        File file = new File(plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            file.createNewFile();
        }
        DreamYaml config = new DreamYaml(file);
        config.load();
        config.add("socket-port").setDefValue("556").setComment("内置服务器端口，默认端口为556");
        config.add("translate-servername").setDefValues("server1: 例子1", "server2: 例子2")
                .setComments("将服务器id翻译成名称，使用如下格式：", "- server1: 例子1", "- server2: 例子2");
        config.add("server-started-broadcast").setDefValue("&6${server_translation} is online now!")
                .setComment("支持颜色代码如“&6”，转义“\\&” -> “&”，使用“${server_translation}”代表上下线的服务器");
        config.add("server-closed-broadcast").setDefValue("&6${server_translation} is offline now!");
        config.save();
        this.config = config;
    }

    public static String getTranslateServername(String serverID) {
        List<String> serverNames = config.getLoadedModuleByKeys("translate-servername").getValues();
        for (String serverKY : serverNames) {
            String[] serverName = serverKY.replace("\n", "").replaceAll("\\s", "").split(":");
            if (serverName[0].equals(serverID))
                return serverName[1];
        }
        return serverID;
    }

    public static String getServerStartedBroadcast() {
        return config.getLoadedModuleByKeys("server-started-broadcast").getValue();
    }

    public static String getServerClosedeBroadcast() {
        return config.getLoadedModuleByKeys("server-closed-broadcast").getValue();
    }

    public static int getSocketPort() {
        return config.getLoadedModuleByKeys("socket-port").asInt();
    }

    public static void setSocketPort(int port) throws Exception {
        config.getLoadedModuleByKeys("socket-port").setValue(String.valueOf(port));
        config.save();
    }
}
