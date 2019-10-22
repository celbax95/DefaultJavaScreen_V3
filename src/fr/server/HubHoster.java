package fr.server;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class HubHoster {

	private MulticastSocket dataShare;

	private MulticastSocket dataReceive;

	private InetAddress groupIP;

	private int portReceive;

	private Thread dataUpdater, dataReceiver, pingTester;

	private Map<Integer, PlayerData> playersData;

	private int myID;

	private int maxPlayer;

	private Map<Integer, Long> pings;

	private List<Integer> listeningPorts;

	private List<Integer> toRemove;

	public HubHoster(int id, String username, Color color, int maxPlayer, String groupIP, int portReceive) {
		this.myID = id;

		this.pings = new HashMap<>();

		this.toRemove = new Vector<>();

		this.listeningPorts = new Vector<>();

		this.playersData = new HashMap<>();

		this.playersData.put(id, new PlayerData(id, username, color));

		this.maxPlayer = maxPlayer;

		this.portReceive = portReceive;

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
	}

	private void addPlayer(String[] splited) {

		if (this.playersData.size() < this.maxPlayer) {
			PlayerData pd = this.playerDataReceived(splited);
			if (pd != null && !this.playersData.containsKey(pd.getId())) {
				this.playersData.put(Integer.valueOf(splited[2]), pd);

				int port = Integer.valueOf(splited[1]);

				if (!this.listeningPorts.contains(port)) {
					this.listeningPorts.add(port);
				}
			}
		}
	}

	private void ping(String[] splited) {
		int i = 2;

		Integer id = Integer.valueOf(splited[i++]);

		this.pings.put(id, System.currentTimeMillis());
	}

	private PlayerData playerDataReceived(String[] splited) {

		if (splited.length != 6)
			return null;

		int i = 2;

		int id = Integer.valueOf(splited[i++]);

		String username = splited[i++];

		String colorTxt = splited[i++];

		Color color = Color.BLACK;

		if (colorTxt.substring(1, 1).equals("#") && colorTxt.length() == 7) {
			color = Color.decode(colorTxt);
		}

		return new PlayerData(id, username, color);
	}

	private void processData(String data) {
		String[] splited = data.split("/");

		switch (Request.valueOf(splited[0])) {
		case ADD:
			this.addPlayer(splited);
			break;
		case PING:
			this.addPlayer(splited);
			break;
		default:
			break;
		}
	}

	public void send(String message) {
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

	public void setDataReceiver() {
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
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void setDataUpdater() {
		this.dataUpdater = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {

					for (Integer id : HubHoster.this.playersData.keySet()) {

						if (HubHoster.this.playersData.get(id) == null) {
							continue;
						}

						PlayerData pd = HubHoster.this.playersData.get(id);

						HubHoster.this.send(Request.UPDATE + "/" + id + "/" + pd.getUsername() + "/" + "#"
								+ Integer.toHexString(pd.getColor().getRGB()).substring(2) + "/");
					}

					try {
						Thread.sleep(ServerDelays.UPDATE_RATE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void setPingTester() {
		this.pingTester = new Thread(new Runnable() {
			@Override
			public void run() {

				long last = System.currentTimeMillis();

				while (Thread.currentThread().isInterrupted() == false) {
					try {
						Thread.sleep(ServerDelays.PING_TEST_RATE);
					} catch (InterruptedException e) {
					}

					last = System.currentTimeMillis();

					for (Integer id : HubHoster.this.pings.keySet()) {
						if (HubHoster.this.pings.get(id) < last) {
							HubHoster.this.pings.remove(id);
							HubHoster.this.toRemove.add(id);
						}
					}

				}
			}
		});
	}

	public void start() {
		this.dataReceiver.start();
		this.dataUpdater.start();
	}

	public void stop() {
		this.dataReceiver.interrupt();
		this.dataUpdater.interrupt();
	}
}
