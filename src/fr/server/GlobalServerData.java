package fr.server;

public class GlobalServerData {

	private static final String[] GROUP_IPS = {
			"230.152.201.50",
			"230.152.201.51",
			"230.152.201.52",
			"230.152.201.53",
			"230.152.201.54" };

	private static final int HUB_PORT = 9998;
	private static final int P2P_PORT = 9997;

	public static String getGroup(int serverIndex) {

		if (serverIndex < 0 || serverIndex >= GROUP_IPS.length) {
			serverIndex = 0;
		}

		return GROUP_IPS[serverIndex];
	}

	public static int getHubPort(int serverIndex) {
		return HUB_PORT;
	}

	public static int getIPAmount() {
		return GROUP_IPS.length;
	}

	public static int getP2PPort(int serverIndex) {
		return P2P_PORT;
	}
}
