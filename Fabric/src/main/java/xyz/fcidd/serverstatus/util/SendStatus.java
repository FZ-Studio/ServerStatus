package xyz.fcidd.serverstatus.util;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import xyz.fcidd.serverstatus.ServerStatus;
import xyz.fcidd.serverstatus.config.ModConfig;
import xyz.fcidd.serverstatus.translate.TranslatableKey;

public class SendStatus implements Runnable {
	private String action;
	private String message;
	private ServerCommandSource scs;

	private SendStatus(String action, String message, ServerCommandSource scs) {
		this.action = action;
		this.message = message;
		this.scs = scs;
	}

	private static void send(String action, String message, ServerCommandSource scs) {
		SendStatus runnable = new SendStatus(action, message, scs);
		Thread thread = new Thread(runnable);
		// 将线程设置为守护线程
		thread.setDaemon(true);
		// 将线程启动
		thread.start();
	}

	public static void sendStart() {
		send("start", null, null);
	}

	public static void sendStartMessage(ServerCommandSource scs) {
		send("message", "start", scs);
	}

	public static void sendClose() {
		send("close", null, null);
	}

	public static void sendCLoseMessage(ServerCommandSource scs) {
		send("message", "close", scs);
	}

	public static void sendCustomMessage(String message) {
		send("message", "custom." + message, null);
	}

	public static void sendCustomMessage(String message, ServerCommandSource scs) {
		send("message", "custom." + message, scs);
	}

	@Override
	public void run() {
		ModConfig config = ServerStatus.getConfig();
		try (Socket socket = new Socket(config.getSocketIp(), config.getSocketPort());
				OutputStream out = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
				BufferedWriter bw = new BufferedWriter(osw);
				PrintWriter pw = new PrintWriter(bw, true);) {
			if (message == null) {
				pw.print(action + "." + ServerStatus.getMcPort());
			} else {
				pw.print(action + "." + ServerStatus.getMcPort() + "." + message);
			}
		} catch (ConnectException e) {
			sendFeedback(scs, TranslatableKey.CONNECT_FAILED);
			return;
		} catch (IOException e) {
			sendFeedback(scs, TranslatableKey.SEND_FAILED);
			e.printStackTrace();
			return;
		}
		sendFeedback(scs, TranslatableKey.SEND_SUCCEEDED);
	}

	private void sendFeedback(ServerCommandSource scs, TranslatableKey message, String... args) {
		TranslatableText text = new TranslatableText(message.getKey(), (Object[]) args);
		if (scs != null) {
			scs.sendFeedback(text, false);
		}
		if (message.getReturnNum() == 0) {
			ILogger.warn(text.getString());
		} else {
			ILogger.info(text.getString());
		}
	}
}
