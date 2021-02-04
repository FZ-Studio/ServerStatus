package xyz.fcidd.server.status.command;

import lombok.SneakyThrows;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import xyz.fcidd.server.status.config.PluginConfig;
import xyz.fcidd.server.status.server.StartServer;
import xyz.fcidd.server.status.util.IUtils;

import java.util.LinkedList;
import java.util.List;

public class ServerStatusCommand extends Command implements TabExecutor {
    private final Plugin plugin;

    public ServerStatusCommand(Plugin plugin) {
        super("serverstatus");
        this.plugin = plugin;
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        List<String> list = new LinkedList<>();
        if (args.length == 0) {
            list.add("serverstatus");
        } else if (args.length == 1) {
            list.add("reload");
            list.add("setport");
            list.add("help");
        }
        return list;
    }

    @SneakyThrows
    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean adminAuth = sender.hasPermission("serverstatus.command.admin");
        boolean setPortAuth = sender.hasPermission("serverstatus.command.serverstatus.setport");
        boolean reloadAuth = sender.hasPermission("serverstatus.command.serverstatus.reload");
        boolean helpAuth = sender.hasPermission("serverstatus.command.serverstatus.help");
        switch (args[0]) {
            case "setport":
                if (!setPortAuth && !adminAuth) {
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§4你没有使用该命令的权限！").create());
                } else if (args.length == 2 && IUtils.isPort(args[1])) {
                    PluginConfig.setSocketPort(Integer.parseInt(args[1]));
                    sender.sendMessage(
                            new ComponentBuilder("§8[§6ServerStatus§8]§2设置成功！输入/serverstatus reload生效").create());
                } else {
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§4请输入正确的端口号").create());
                }
                break;
            case "reload":
                if (!reloadAuth && !adminAuth) {
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§4你没有使用该命令的权限！").create());
                } else {
                    // 重载配置
                    plugin.getProxy().getLogger().info("重载配置文件中...");
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§2重载配置文件中...").create());
                    PluginConfig.loadConfig(plugin);
                    // 关闭内置服务器
                    StartServer.stopServer();
                    // 初始化内置服务器
                    StartServer.initialize(plugin);
                    plugin.getProxy().getLogger().info("已重载");
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§2已重载").create());
                }
                break;
            case "help":
                if (!helpAuth && !adminAuth) {
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§4你没有使用该命令的权限！").create());
                } else {
                    sender.sendMessage(
                            new ComponentBuilder("§bServerStatus前端使用指南：\n").append("/ServerStatus setport <端口号> 设置端口号\n")
                                    .append("/ServerStatus reload 重载配置文件").create());
                }
                break;
            default:
                sender.sendMessage(
                        new ComponentBuilder("§8[§6ServerStatus§8]§4未知指令，请输入/serverstatus help查看帮助").create());
                break;
        }
    }
}
