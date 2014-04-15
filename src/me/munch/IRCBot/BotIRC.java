package me.munch.IRCBot;
import runnable.BotHTTPCheckRunnable;
import Util.BotConstants;
import Util.BotLogger;

import java.util.ArrayList;
import java.util.Timer;

public class BotIRC {

	private static Timer timer;
	private static ArrayList<BotConnection> connections;
	private static boolean usingProxy;

	public static void main(String[] args) {
		BotLogger.info("Initializing " + BotConstants.NAME);

		/* Check for parameters */
		if(args.length > 1) {
			BotConstants.channel = args[0];
			BotConstants.usingProxy = Boolean.parseBoolean(args[1]);
			if (BotConstants.usingProxy == true) {
				BotConstants.proxyHost = args[2];
				BotConstants.proxyPort = Integer.parseInt(args[3]);
				BotLogger.info("Using a proxy. Hostname: " + BotConstants.proxyHost + ". Proxy port: " + BotConstants.proxyPort + ".");
			}
			BotLogger.info(BotConstants.NAME + " connection to " + BotConstants.channel);
		} else {
			BotLogger.severe("Args: <channel name> <using proxy> (proxy host) (proxy port)");
			System.exit(0);
		}

		/* Initialize variables */
		timer = new Timer();
		connections = new ArrayList<BotConnection>();

		/* Set timer tasks */
		timer.scheduleAtFixedRate(BotHTTPCheckRunnable.instance(), 0L, 30000L);

		/* Create BotConnection objects */
		connections.add(new BotConnection("irc.esper.net", 6666));


	}

}
