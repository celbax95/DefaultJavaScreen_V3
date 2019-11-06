package fr.state.loading;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Map;

import fr.serverlink.data.ServerData;

public class LoadingScreen {

	Map<Integer, Integer> linkIDPorts;
	LoadingRequestor requestor;
	MulticastSocket socket;

	public LoadingScreen(int serverIndex, Map<Integer, Integer> linkIDPorts, LoadingRequestor requestor) {
		this.linkIDPorts = linkIDPorts;
		this.requestor = requestor;

		int p2pPort = 9997;

		InetAddress address = null;
		try {
			address = InetAddress.getByName(ServerData.getGroup(serverIndex));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			this.socket = new MulticastSocket(p2pPort);
			this.socket.setInterface(InetAddress.getLocalHost());
			this.socket.joinGroup(address);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
