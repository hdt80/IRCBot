package runnable;

import me.munch.IRCBot.*;
import Util.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.Socket;
import java.util.HashMap;

public class BotConnectionRunnable implements Runnable {
	private final String hostname;
	private final int port;
	private Socket socket;
	private long lastSet;
	private Proxy proxy;

	public BotConnectionRunnable(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		this.lastSet = System.currentTimeMillis();
	}

	@Override
	public void run() {
		final PrintWriter out;
		final BufferedReader in;
		try {
			if (BotConstants.usingProxy == true) {
				proxy= new Proxy(Type.HTTP, new InetSocketAddress(BotConstants.proxyHost, BotConstants.proxyPort));
				socket = new Socket(proxy);				
			} else {				
				socket = new Socket(hostname, port);
			}
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
		} catch (IOException e) {
			BotLogger.severe(e.getMessage());
			BotStatus.setIRC(false);
			return;
		}
		/*
		 * Class for responding to IRC input
		 */
		class BotConnectionHandler {

			public void write(String write) {
				BotLogger.info("OUT: " + write);
				out.write(write + "\r\n");
				out.flush();
			}

			public HashMap<String, String> getMessageInfo(String input) {
				String[] inputSplit = input.split(" ");
				HashMap<String, String> info = new HashMap<String, String>();
				if (input.startsWith(":")) {
					info.put("sender", inputSplit[0].replace(":", ""));
					info.put("code", inputSplit[1]);
					String message = "";
					for (int i = 4; i < inputSplit.length; i++) {
						message += inputSplit[i] + " ";
					}
					info.put("receiver", inputSplit[2]);
					info.put("message", message);
					if (info.get("message").equals("!test")) {
						write("Testerino");
					}
				} else {
					info.put("sender", "SERVER");
					info.put("receiver", "*");
					info.put("code", inputSplit[0]);
					info.put("message", inputSplit[1].replace(":", ""));
				}
				return info;
			}

			public void respond(HashMap<String, String> msgInfo) {
				if (msgInfo.get("code").equalsIgnoreCase("ping")) {
					write("PONG :" + msgInfo.get("message"));
				}
			}
		}
		BotConnectionHandler c = new BotConnectionHandler();
		String inputLine;
		boolean sentAuth = false, joinedChannels = false;
		try {
			while ((inputLine = in.readLine()) != null) {
				HashMap<String, String> msgInfo = c.getMessageInfo(inputLine);
				/* Perform authentication */
				if (msgInfo.get("code").equalsIgnoreCase("notice")
						&& msgInfo.get("message").toLowerCase()
								.contains("ident") && !sentAuth) {
					c.write("NICK " + BotConstants.NICK);
					c.write(String.format("USER %s %s %s :%s",
							BotConstants.NICK, BotConstants.NICK,
							BotConstants.NICK, BotConstants.NICK));
					sentAuth = true;
				}

				/* Join channels */
				if (msgInfo.get("code").equalsIgnoreCase("mode") && sentAuth
						&& !joinedChannels) {
					lastSet = System.currentTimeMillis();
					BotStatus.setIRC(true);
					BotLogger.info("Finished connecting to " + hostname + ":"
							+ port);
					c.write("JOIN #" + BotConstants.channel);
					c.write("SAY Testerino");
					joinedChannels = true;
				}

				/* Perform checking for commands/messages */
				if (!msgInfo.get("code").equalsIgnoreCase("372")) {
					BotLogger.info("IN: " + inputLine);
				}
				c.respond(msgInfo);
				if (System.currentTimeMillis() - lastSet > 10000) {
					lastSet = System.currentTimeMillis();
					BotStatus.setIRC(true);
				}

			}
		} catch (IOException e) {
			BotLogger.severe(e.getMessage());
			BotStatus.setIRC(false);
			return;
		}
	}
}
