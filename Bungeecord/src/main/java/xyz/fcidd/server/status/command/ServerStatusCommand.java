package xyz.fcidd.server.status.command;

import lombok.SneakyThrows;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;
import xyz.fcidd.server.status.config.PluginConfig;
import xyz.fcidd.server.status.server.StartServer;

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
        boolean adminAuth = sender.hasPermission("server.status.admin");
        boolean setPortAuth = sender.hasPermission("server.status.setport");
        boolean reloadAuth = sender.hasPermission("server.status.reload");
        boolean helpAuth = sender.hasPermission("server.status.help");
        switch (args[0]) {
            case "setport":
                if (!setPortAuth && !adminAuth) {
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§4你没有使用该命令的权限！").create());
                } else if ((setPortAuth || adminAuth) && args.length == 2) {
                    PluginConfig.setSocketPort(Integer.parseInt(args[1]));
                    sender.sendMessage(
                            new ComponentBuilder("§8[§6ServerStatus§8]§2设置成功！请输入/serverstatus reload生效").create());
                } else {
                    sender.sendMessage(
                            new ComponentBuilder("§8[§6ServerStatus§8]§4未知指令，请输入/serverstatus help查看帮助").create());
                }
                break;
            case "reload":
                if (!reloadAuth && !adminAuth) {
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§4你没有使用该命令的权限！").create());
                } else if ((reloadAuth || adminAuth) && args.length == 1) {
                    // 重载配置
                    plugin.getProxy().broadcast(new ComponentBuilder("§8[§6ServerStatus§8]§2重载配置文件中...").create());
                    new PluginConfig(plugin);
                    // 关闭内置服务器
                    StartServer.stopServer();
                    // 初始化内置服务器
                    StartServer.initialize(plugin);
                    plugin.getProxy().broadcast(new ComponentBuilder("§8[§6ServerStatus§8]§2已重载").create());
                } else {
                    sender.sendMessage(
                            new ComponentBuilder("§8[§6ServerStatus§8]§4未知指令，请输入/serverstatus help查看帮助").create());
                }
                break;
            case "help":
                if (!helpAuth || !adminAuth) {
                    sender.sendMessage(new ComponentBuilder("§8[§6ServerStatus§8]§4你没有使用该命令的权限！").create());
                } else if ((helpAuth || adminAuth) && args.length == 1) {
                    sender.sendMessage(new ComponentBuilder("§bServerStatus 使用指南\n"
                            + "/ServerStatus setport <端口号> 设置端口号\n" + "/ServerStatus reload 重载配置文件").create());
                } else {
                    sender.sendMessage(
                            new ComponentBuilder("§8[§6ServerStatus§8]§4未知指令，请输入/serverstatus help查看帮助").create());
                }
                break;
            default:
                sender.sendMessage(
                        new ComponentBuilder("§8[§6ServerStatus§8]§4未知指令，请输入/serverstatus help查看帮助").create());
                break;
        }
    }
}
