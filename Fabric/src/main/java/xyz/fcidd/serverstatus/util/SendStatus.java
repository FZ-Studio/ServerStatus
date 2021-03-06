package xyz.fcidd.serverstatus.util;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.Level;

import net.minecraft.server.command.ServerCommandSource;
import xyz.fcidd.serverstatus.ServerStatus;
import xyz.fcidd.serverstatus.config.ModConfig;
import xyz.fcidd.serverstatus.translate.LangManager;
import xyz.fcidd.serverstatus.translate.TranslatableKey;

public class SendStatus implements Runnable {
	private String action;
	private String message;
	private ServerCommandSource scs;

	/**
	 * 发送消息的构造方法
	 * 
	 * @param action  消息动作
	 * @param message 消息
	 * @param scs     命令发送者，自动发送时为null
	 */
	private SendStatus(String action, String message, ServerCommandSource scs) {
		this.action = action;
		this.message = message;
		this.scs = scs;
	}

	/**
	 * 发送消息
	 * 
	 * @param action  消息动作
	 * @param message 消息
	 * @param scs     命令发送者，自动发送时为null
	 */
	private static void send(String action, String message, ServerCommandSource scs) {
		SendStatus runnable = new SendStatus(action, message, scs);
		Thread thread = new Thread(runnable);
		// 将线程设置为守护线程
		thread.setDaemon(true);
		// 将线程启动
		thread.start();
	}

	/**
	 * 发送服务器上线提醒
	 */
	public static void sendStart() {
		send("start", null, null);
	}

	/**
	 * 手动发送服务器上线提醒
	 * 
	 * @param scs 命令发送者
	 */
	public static void sendStartMessage(ServerCommandSource scs) {
		send("message", "start", scs);
	}

	/**
	 * 发送服务器下线提醒
	 */
	public static void sendClose() {
		send("close", null, null);
	}

	/**
	 * 手动发送服务器下线提醒
	 * 
	 * @param scs 命令发送者
	 */
	public static void sendCLoseMessage(ServerCommandSource scs) {
		send("message", "close", scs);
	}

	/**
	 * 发送自定义消息
	 * 
	 * @param message 消息
	 */
	public static void sendCustomMessage(String message) {
		send("message", "custom." + message, null);
	}

	/**
	 * 手动发送自定义消息
	 * 
	 * @param message 消息
	 * @param scs     命令发送者
	 */
	public static void sendCustomMessage(String message, ServerCommandSource scs) {
		send("message", "custom." + message, scs);
	}

	/**
	 * 发送消息
	 */
	@Override
	public void run() {
		ModConfig config = ServerStatus.getConfig();
		try (Socket socket = new Socket(config.getSocketIp(), config.getSocketPort());
				OutputStream out = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
				BufferedWriter bw = new BufferedWriter(osw);
				PrintWriter pw = new PrintWriter(bw, true);) {
			if (message == null) {
				pw.print("serverstatus." + ServerStatus.getMcPort() + "." + action);
			} else {
				pw.print("serverstatus." + ServerStatus.getMcPort() + "." + action + "." + message);
			}
		} catch (ConnectException e) {
			sendFeedback(scs, LangManager.getTranslated(TranslatableKey.CONNECT_FAILED), Level.WARN);
			return;
		} catch (IOException e) {
			sendFeedback(scs, LangManager.getTranslated(TranslatableKey.SEND_FAILED), Level.WARN);
			e.printStackTrace();
			return;
		}
		sendFeedback(scs, LangManager.getTranslated(TranslatableKey.SEND_SUCCEEDED), Level.INFO);
	}

	/**
	 * 发送反馈，可以向控制台以及玩家发送反馈
	 * 
	 * @param scs
	 * @param message
	 * @param args
	 */
	private void sendFeedback(ServerCommandSource scs, String message, Level level) {
		if (scs != null) {
			IMessenger.sendPlayerFeedback(scs, message);
		}
		IMessenger.log(message, level);
	}
}
