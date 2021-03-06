package fr.server.serverlink.hub;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import fr.server.serverlink.data.HubPlayerData;
import fr.server.serverlink.data.Request;
import fr.server.serverlink.data.ServerDelays;

public abstract class HubHoster {

	private MulticastSocket dataShare;

	private MulticastSocket dataReceive;

	private InetAddress groupIP;

	private Thread dataUpdater, dataReceiver, pingTester;

	private Map<Integer, HubPlayerData> playersData;

	private int maxPlayer;

	private Map<Integer, Long> pings;

	private Map<Integer, Integer> linkIDPorts;

	private List<Integer> listeningPorts;

	public HubHoster(int id, String username, Color color, int maxPlayer, String groupIP, int portReceive) {

		this.pings = new HashMap<>();

		this.linkIDPorts = new HashMap<>();
		this.listeningPorts = new Vector<>();

		this.playersData = new HashMap<>();

		this.playersData.put(id, new HubPlayerData(id, username, color, true));

		this.maxPlayer = maxPlayer;

		try {
			this.groupIP = InetAddress.getByName(groupIP);

			this.dataShare = new MulticastSocket();
			this.dataShare.setInterface(InetAddress.getLocalHost());
			this.dataShare.joinGroup(this.groupIP);

			this.dataReceive = new MulticastSocket(portReceive);
			this.dataReceive.setInterface(InetAddress.getLocalHost());
			this.dataReceive.joinGroup(this.groupIP);

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setDataReceiver();
		this.setDataUpdater();
		this.setPingTester();
	}

	private void addPlayer(String[] splited) {
		if (this.playersData.size() < this.maxPlayer) {
			HubPlayerData pd = this.playerDataReceived(splited);
			if (pd != null && !this.playersData.containsKey(pd.getId())) {
				this.playersData.put(Integer.valueOf(splited[2]), pd);

				int port = Integer.valueOf(splited[1]);

				this.linkIDPorts.put(pd.getId(), port);

				if (!this.listeningPorts.contains(port)) {
					this.listeningPorts.add(port);
				}

				this.playerAdded(pd.getId(), pd.getUsername(), pd.getColor());
				this.readyChanged(pd.getId(), pd.isReady());
			}
		}
	}

	public void closeSockets() {
		if (this.dataShare != null) {
			this.dataShare.close();
		}
		if (this.dataReceive != null) {
			this.dataReceive.close();
		}
	}

	public abstract void gameStarting(boolean state);

	public List<Integer> getListeningPorts() {
		return this.listeningPorts;
	}

	public abstract void noMorePlayer();

	private void ping(String[] data) {
		int i = 2;

		Integer id = Integer.valueOf(data[i++]);

		this.pings.put(id, System.currentTimeMillis());

		// Ready

		boolean ready = Boolean.parseBoolean(data[i++]);

		if (this.playersData.containsKey(id)) {
			if (ready != this.playersData.get(id).isReady()) {
				this.playersData.get(id).setReady(ready);
				this.readyChanged(id, ready);
			}
		}
	}

	public abstract void playerAdded(int id, String username, Color color);

	private HubPlayerData playerDataReceived(String[] splited) {

		if (splited.length != 6)
			return null;

		int i = 2;

		int id = Integer.valueOf(splited[i++]);

		String username = splited[i++];

		String colorTxt = splited[i++];

		Color color = Color.BLACK;

		if (colorTxt.substring(0, 1).equals("#") && colorTxt.length() == 7) {
			color = Color.decode(colorTxt);
		}

		return new HubPlayerData(id, username, color, false);
	}

	public abstract void playerRemoved(int id);

	private void processData(String data) {
		String[] splited = data.split("/");

		switch (Request.valueOf(splited[0])) {
		case ADD:
			this.addPlayer(splited);
			break;
		case PING:
			this.ping(splited);
			break;
		default:
			break;
		}
	}

	public abstract void readyChanged(int id, boolean ready);

	private void send(String message) {
		try {
			byte[] buffer = message.getBytes();

			for (int port : this.listeningPorts) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, HubHoster.this.groupIP, port);

				HubHoster.this.dataShare.send(packet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setDataReceiver() {
		this.dataReceiver = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {

					try {
						byte[] buffer = new byte[1000];

						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						HubHoster.this.dataReceive.receive(packet);

						HubHoster.this.processData(new String(packet.getData()));
					} catch (Exception e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		});
		this.dataReceiver.setName("hubHoster/dataReceiver");
	}

	private void setDataUpdater() {
		this.dataUpdater = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {

					for (Integer id : HubHoster.this.playersData.keySet()) {

						if (HubHoster.this.playersData.get(id) == null) {
							continue;
						}

						HubPlayerData pd = HubHoster.this.playersData.get(id);

						HubHoster.this.send(Request.UPDATE + "/" + id + "/" + pd.getUsername() + "/" + "#"
								+ Integer.toHexString(pd.getColor().getRGB()).substring(2) + "/" + pd.isReady() + "/");
					}

					try {
						Thread.sleep(ServerDelays.UPDATE_RATE);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		});
		this.dataUpdater.setName("HubHoster/dataUpdater");
	}

	private void setPingTester() {
		this.pingTester = new Thread(new Runnable() {
			@Override
			public void run() {

				long testTime = System.currentTimeMillis() - ServerDelays.PING_TEST_RATE;

				while (Thread.currentThread().isInterrupted() == false) {
					try {
						Thread.sleep(ServerDelays.PING_TEST_RATE);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}

					testTime = System.currentTimeMillis() - ServerDelays.PING_TEST_RATE;

					List<Integer> idsToRemove = new ArrayList<>();

					for (Integer id : HubHoster.this.pings.keySet()) {
						if (HubHoster.this.pings.get(id) < testTime) {
							idsToRemove.add(id);
						}
					}

					for (Integer id : idsToRemove) {
						HubHoster.this.pings.remove(id);
						HubHoster.this.playersData.remove(id);
						HubHoster.this.linkIDPorts.remove(id);
						HubHoster.this.playerRemoved(id);
					}

					if (idsToRemove.size() > 0) {
						HubHoster.this.updatePorts();
						if (HubHoster.this.playersData.size() == 1) {
							HubHoster.this.noMorePlayer();
						}
					}
				}
			}
		});
		this.pingTester.setName("HubHoster/pingTester");
	}

	public void start() {
		if (this.dataReceiver != null) {
			this.dataReceiver.interrupt();
		}
		this.setDataReceiver();
		this.dataReceiver.start();

		if (this.dataUpdater != null) {
			this.dataUpdater.interrupt();
		}
		this.setDataUpdater();
		this.dataUpdater.start();

		if (this.pingTester != null) {
			this.pingTester.interrupt();
		}
		this.setPingTester();
		this.pingTester.start();
	}

	public void startGame() {
		for (HubPlayerData p : this.playersData.values()) {
			if (p.isReady() == false)
				return;
		}
		this.gameStarting(true);
	}

	public void stop() {
		if (this.dataReceiver != null) {
			this.dataReceiver.interrupt();
		}
		if (this.dataUpdater != null) {
			this.dataUpdater.interrupt();
		}
		if (this.pingTester != null) {
			this.pingTester.interrupt();
		}
	}

	private void updatePorts() {
		List<Integer> ports = new ArrayList<>();

		for (Integer id : this.linkIDPorts.keySet()) {

			int port = this.linkIDPorts.get(id);

			if (!ports.contains(port)) {
				ports.add(port);
			}
		}

		this.listeningPorts = new ArrayList<>(ports);
	}
}
