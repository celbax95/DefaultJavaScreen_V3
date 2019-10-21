package fr.server;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

public class HubJoiner implements IdSetter {

	private static final int REQUEST = 0, UPDATE = 1, ADD = 2;

	private static final int UPDATE_RATE = 1000, REQUEST_RATE = 500;

	private MulticastSocket dataSend;

	private MulticastSocket dataReceive;

	private InetAddress groupIP;

	private int portSend;
	private int portReceive;

	private Thread dataReceiver, addRequestor;

	private Map<Integer, PlayerData> playersData;

	private PlayerData myPlayer;

	private int myID;

	public HubJoiner(String playerUsername, Color playerColor, String groupIP, int portReceive, int portSend) {
		this.myID = -1;
		this.myPlayer = new PlayerData(-1, playerUsername, playerColor);

		this.portReceive = portReceive;
		this.portSend = portSend;

		this.playersData = new HashMap<>();

		try {
			this.groupIP = InetAddress.getByName(groupIP);

			this.dataSend = new MulticastSocket();
			this.dataSend.setInterface(InetAddress.getLocalHost());
			this.dataSend.joinGroup(this.groupIP);

			this.dataReceive = new MulticastSocket(portReceive);
			this.dataReceive.setInterface(InetAddress.getLocalHost());
			this.dataReceive.joinGroup(this.groupIP);

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setDataReceiver();
		this.setAddRequestor();
	}

	private void playerDataReceived(String[] data) {
		int id = Integer.valueOf(data[1]);

		if (id == this.myID) {
			this.addRequestor.interrupt();
		}
		System.out.println(this.playersData);

		// le player id est inconnu
		if (!this.playersData.containsKey(id)) {
			String username = data[2];

			String colorTxt = data[3];

			Color color = Color.BLACK;

			if (colorTxt.substring(1, 1).equals("#") && colorTxt.length() == 7) {
				color = Color.decode(colorTxt);
			}

			this.playersData.put(id, new PlayerData(id, username, color));
		} else {
			PlayerData pd = this.playersData.get(id);
			pd.setUsername(data[2]);

			String colorTxt = data[3];
			if (colorTxt.substring(1, 1).equals("#") && colorTxt.length() == 7) {
				pd.setColor(Color.decode(colorTxt));
			}
		}
	}

	private void processData(String data) {
		String[] splited = data.split("/");

		switch (Integer.valueOf(splited[0])) {
		case UPDATE:
			this.playerDataReceived(splited);
			break;
		}
	}

	public void send(String message) {
		try {
			byte[] buffer = message.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, HubJoiner.this.groupIP, this.portSend);

			HubJoiner.this.dataSend.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAddRequestor() {
		this.addRequestor = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {

					if (HubJoiner.this.myID == -1) {
						Thread.currentThread().interrupt();
						return;
					}

					HubJoiner.this.send(HubJoiner.ADD + "/" + HubJoiner.this.myPlayer.getId() + "/"
							+ HubJoiner.this.myPlayer.getUsername() + "/" + "#"
							+ Integer.toHexString(HubJoiner.this.myPlayer.getColor().getRGB()).substring(2) + "/");

					try {
						Thread.sleep(HubJoiner.REQUEST_RATE);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		});
	}

	public void setDataReceiver() {
		this.dataReceiver = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {

					try {
						byte[] buffer = new byte[1000];

						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						HubJoiner.this.dataReceive.receive(packet);

						HubJoiner.this.processData(new String(packet.getData()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@Override
	public void setId(int id) {
		this.myPlayer.setId(id);
		this.myID = id;
		this.playersData.put(id, this.myPlayer);
		this.addRequestor.start();
	}

	public void start() {
		this.dataReceiver.start();
	}

	public void stop() {
		this.dataReceiver.interrupt();
	}
}
