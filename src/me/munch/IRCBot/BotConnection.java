package me.munch.IRCBot;
import runnable.BotConnectionRunnable;
import Util.BotLogger;

public class BotConnection {

	private final String hostname;
	private final int port;
	private boolean initialized = false;

	public BotConnection(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		init();
	}

	public void init() {
		if(initialized) return;
		BotLogger.info("Initializing connection to " + hostname + ":" + port);
		socket();
		initialized = true;
	}

	private void socket() {
		if(initialized) return;
		new Thread(new BotConnectionRunnable(hostname, port)).start();
	}

}
