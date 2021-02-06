package xyz.fcidd.serverstatus.command;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import xyz.fcidd.serverstatus.ServerStatus;
import xyz.fcidd.serverstatus.util.SendStatus;
import xyz.fcidd.serverstatus.util.IUtils;

public class ServerStatusCommands implements TabCompleter, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Plugin plugin = ServerStatus.getInstance();
        // 查询权限
        boolean adminAuth = true;
        boolean setPortAuth = true;
        boolean setHostAuth = true;
        boolean reloadAuth = true;
        boolean helpAuth = true;
        boolean messageAuth = true;
        String consolePrefix = "\n";
        if (!(sender instanceof ConsoleCommandSender)) {
            adminAuth = sender.hasPermission("serverstatus.command.admin");
            setPortAuth = sender.hasPermission("serverstatus.command.serverstatus.setport");
            setHostAuth = sender.hasPermission("serverstatus.command.serverstatus.sethost");
            reloadAuth = sender.hasPermission("serverstatus.command.serverstatus.reload");
            messageAuth = sender.hasPermission("serverstatus.command.serverstatus.message");
            helpAuth = setPortAuth || setHostAuth || reloadAuth || messageAuth;
            consolePrefix = null;
        }
        // 命令判断逻辑
        if (args.length == 0) {
            if (!helpAuth && !adminAuth) {
                sender.sendMessage("§8[§6ServerStatus§8]§4你没有使用该命令的权限！");
            } else {
                sender.sendMessage(consolePrefix
                        + "§bServerStatus后端使用指南：\n"
                        + "/bgServerStatus setport <端口号> 设置端口号\n"
                        + "/bgServerStatus sethost <IP> 设置IP地址\n"
                        + "/bgServerStatus reload 重载配置文件\n"
                        + "/bgServerStatus message start 发送服务器上线消息\n"
                        + "/bgServerStatus message close 发送服务器下线消息\n"
                        + "/bgServerStatus message custom <消息> 发送自定义消息\n");
            }
        } else {
            switch (args[0]) {
                case "setport":
                    if (!setPortAuth && !adminAuth) {
                        sender.sendMessage("§8[§6ServerStatus§8]§4你没有使用该命令的权限！");
                    } else if (args.length == 2 && IUtils.isPort(args[1])) {
                        plugin.getConfig().set("socket-port", Integer.parseInt(args[1]));
                        plugin.saveConfig();
                        sender.sendMessage("§8[§6ServerStatus§8]§2设置成功！");
                    } else {
                        sender.sendMessage("§8[§6ServerStatus§8]§4请输入正确的端口号");
                    }
                    break;
                case "sethost":
                    if (!setHostAuth && !adminAuth) {
                        sender.sendMessage("§8[§6ServerStatus§8]§4你没有使用该命令的权限！");
                    } else if (args.length == 2 && IUtils.isHost(args[1])) {
                        plugin.getConfig().set("socket-host", args[1]);
                        plugin.saveConfig();
                        sender.sendMessage("§8[§6ServerStatus§8]§2设置成功！");
                    } else {
                        sender.sendMessage("§8[§6ServerStatus§8]§4请输入正确的IP地址");
                    }
                    break;
                case "reload":
                    if (!reloadAuth && !adminAuth) {
                        sender.sendMessage("§8[§6ServerStatus§8]§4你没有使用该命令的权限！");
                    } else {
                        // 重载配置
                        plugin.getServer().getLogger().info("重载配置文件中...");
                        sender.sendMessage("§8[§6ServerStatus§8]§2重载配置文件中...");
                        plugin.reloadConfig();
                        plugin.getServer().getLogger().info("已重载");
                        sender.sendMessage("§8[§6ServerStatus§8]§2已重载");
                    }
                    break;
                case "help":
                    if (!helpAuth && !adminAuth) {
                        sender.sendMessage("§8[§6ServerStatus§8]§4你没有使用该命令的权限！");
                    } else {
                        sender.sendMessage(consolePrefix
                                + "§bServerStatus后端使用指南：\n"
                                + "/bgServerStatus setport <端口号> 设置端口号\n"
                                + "/bgServerStatus sethost <IP> 设置IP地址\n"
                                + "/bgServerStatus reload 重载配置文件\n"
                                + "/bgServerStatus message start 发送服务器上线消息\n"
                                + "/bgServerStatus message close 发送服务器下线消息\n"
                                + "/bgServerStatus message custom <消息> 发送自定义消息\n");
                    }
                    break;
                case "message":
                    if (!messageAuth && !adminAuth) {
                        sender.sendMessage("§8[§6ServerStatus§8]§4你没有使用该命令的权限！");
                    } else {
                        switch (args[1]) {
                            case "start":
                                SendStatus.sendStartMessage(sender);
                                break;
                            case "close":
                                SendStatus.sendCLoseMessage(sender);
                                break;
                            case "custom":
                                if (args.length == 3) {
                                    SendStatus.sendCustomMessage(args[2], sender);
                                } else {
                                    sender.sendMessage("§8[§6ServerStatus§8]§4请输入正确的消息");
                                }
                                break;
                            default:
                                sender.sendMessage("§8[§6ServerStatus§8]§4请输入正确的消息类型");
                                break;
                        }
                    }
                    break;
                default:
                    sender.sendMessage("§8[§6ServerStatus§8]§4未知指令，请输入/bgServerStatus help查看帮助");
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new LinkedList<>();
        if (args.length == 1) {
            list.add("message");
            list.add("setport");
            list.add("sethost");
            list.add("reload");
            list.add("help");
        } else if (args.length == 2 && args[0].equals("message")) {
            list.add("start");
            list.add("close");
            list.add("custom");
        }
        return list;
    }
}
