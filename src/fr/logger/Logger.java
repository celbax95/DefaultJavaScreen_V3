package fr.logger;

public class Logger {
	public static void err(String error) {
		sendLog("ERROR", error);
	}

	public static void inf(String info) {
		sendLog("INFO", info);
	}

	public static void obs(String obs) {
		sendLog("OBS", obs);
	}

	private static void sendLog(String type, String log) {
		System.out.println("[LOG-" + type + "] : " + log);
	}

	public static void warn(String warning) {
		sendLog("WARNING", warning);
	}
}
