package Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

public class BotLogger {

	public static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

	public static void log(Level level, String message) {
		Date date = new Date();
		StringBuilder sb = new StringBuilder();
		sb.append(format.format(date)).append(" [");
		sb.append(level.toString()).append("]: ");
		sb.append(message);
		System.out.println(sb.toString());
	}

	public static void info(String message) {
		log(Level.INFO, message);
	}

	public static void warn(String message) {
		log(Level.WARNING, message);
	}

	public static void severe(String message) {
		log(Level.SEVERE, message);
	}
}
