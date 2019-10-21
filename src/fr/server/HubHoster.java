package fr.server;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

public class HubHoster {

	private int UPDATE_RATE = 1000;

	private final int UPDATE = 1, ADD = 2;

	private MulticastSocket dataShare;

	private MulticastSocket dataReceive;

	private InetAddress groupIP;

	private int portSend, portReceive;

	private Thread dataUpdater, dataReceiver;

	private Map<Integer, PlayerData> playersData;

	private int myID;

	private int maxPlayer;

	public HubHoster(int id, int maxPlayer, String groupIP, int portReceive, int portSend) {
		this.myID = id;

		this.maxPlayer = maxPlayer;

		this.portReceive = portReceive;
		this.portSend = portSend;

		this.playersData = new HashMap<>();

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

	public void addPlayer(int id, PlayerData pd) {

		boolean startThread = this.playersData.size() == 0;

		this.playersData.put(id, pd);
		if (startThread) {
			this.dataUpdater.start();
		}
	}

	private void addPlayer(String[] splited) {

		if (this.playersData.size() < this.maxPlayer) {
			PlayerData pd = this.playerDataReceived(splited);
			if (pd != null) {
				this.addPlayer(Integer.valueOf(splited[1]), pd);
			}
		}
	}

	private PlayerData playerDataReceived(String[] splited) {

		if (splited.length != 5)
			return null;

		int id = Integer.valueOf(splited[1]);

		String username = splited[2];

		String colorTxt = splited[3];

		Color color = Color.BLACK;

		if (colorTxt.substring(1, 1).equals("#") && colorTxt.length() == 7) {
			color = Color.decode(colorTxt);
		}

		return new PlayerData(id, username, color);
	}

	private void processData(String data) {
		String[] splited = data.split("/");

		switch (Integer.valueOf(splited[0])) {
		case ADD:
			this.addPlayer(splited);
			break;
		}
	}

	public void send(String message) {
		try {
			byte[] buffer = message.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, HubHoster.this.groupIP,
					HubHoster.this.portSend);

			HubHoster.this.dataShare.send(packet);
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

						HubHoster.this.send(HubHoster.this.UPDATE + "/" + id + "/" + pd.getUsername() + "/" + "#"
								+ Integer.toHexString(pd.getColor().getRGB()).substring(2) + "/");
					}

					try {
						Thread.sleep(HubHoster.this.UPDATE_RATE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void start() {
		this.dataReceiver.start();
	}

	public void stop() {
		this.dataReceiver.interrupt();
		this.dataUpdater.interrupt();
	}
}
