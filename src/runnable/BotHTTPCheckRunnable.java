package runnable;

import me.munch.IRCBot.BotStatus;
import Util.BotLogger;
import Util.BotConfig;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.TimerTask;


public class BotHTTPCheckRunnable extends TimerTask {
	@Override
	public void run() {
		BotLogger.info("Checking HTTP connection to " + BotConfig.getString("esper.net"));
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(BotConfig.getString("esper.net"));
		} catch (MalformedURLException e) {
			BotLogger.severe("MalformedURLException");
			BotLogger.severe(e.getMessage());
			BotStatus.setHTTP(false);
			return;
		}
		try {
			connection = (HttpURLConnection)url.openConnection();
		} catch (IOException e) {
			BotLogger.severe("IOException");
			BotLogger.severe(e.getMessage());
			BotStatus.setHTTP(false);
			return;
		}
		try {
			connection.setRequestMethod("GET");
		} catch (ProtocolException e) {
			BotLogger.severe("ProtocolException");
			BotLogger.severe(e.getMessage());
			BotStatus.setHTTP(false);
			return;
		}
		try {
			connection.connect();
		} catch (IOException e) {
			BotLogger.severe("IOException");
			BotLogger.severe(e.getMessage());
			BotStatus.setHTTP(false);
			return;
		}
		try {
			if(connection.getResponseCode() == 200) {
				BotStatus.setHTTP(true);
			} else {
				BotStatus.setHTTP(false);
			}
		} catch (IOException e) {
			BotLogger.severe("IOException!");
			BotLogger.severe(e.getMessage());
			BotStatus.setHTTP(false);
			return;
		}
	}

	public static BotHTTPCheckRunnable instance() {
		return new BotHTTPCheckRunnable();
	}
}
