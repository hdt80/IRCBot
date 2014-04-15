package me.munch.IRCBot;
import Util.BotLogger;

public final class BotStatus {

	private static long http_start;
	private static long irc_start;
	private static boolean http;
	private static boolean irc;

	static {
		http_start = System.currentTimeMillis();
		irc_start = System.currentTimeMillis();
		http = false;
		irc = false;
	}

	public static boolean getHTTP() {
		return http;
	}

	public static boolean getIRC() {
		return irc;
	}

	public static void setHTTP(boolean bool) {
		if(http) {
			if(bool) {
				BotLogger.info("HTTP Connection healthy for the past " + Math.round((System.currentTimeMillis() - http_start) / 1000.0)  + "s!");
			} else {
				BotLogger.severe("HTTP Connection detected unhealthy!");
				http_start = System.currentTimeMillis();
			}
		} else {
			if(bool) {
				BotLogger.info("HTTP Connection back to healthy!");
				http_start = System.currentTimeMillis();
			} else {
				BotLogger.warn("HTTP Connection unhealthy for the past " + Math.round((System.currentTimeMillis() - http_start) / 1000.0) + "s!");
			}
		}
		http = bool;
	}

	public static void setIRC(boolean bool) {
		if(irc) {
			if(bool) {
				BotLogger.info("IRC Connection healthy for the past " + Math.round((System.currentTimeMillis() - irc_start) / 1000.0) + " seconds!");
			} else {
				BotLogger.severe("IRC Connection detected unhealthy!");
				irc_start = System.currentTimeMillis();
			}
		} else {
			if(bool) {
				BotLogger.info("IRC Connection back to healthy!");
				irc_start = System.currentTimeMillis();
			} else {
				BotLogger.warn("IRC Connection unhealthy for the past " + Math.round((System.currentTimeMillis() - irc_start) / 1000.0) + " seconds!");
			}
		}
		irc = bool;
	}

}