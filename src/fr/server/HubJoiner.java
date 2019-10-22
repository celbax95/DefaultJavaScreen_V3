package fr.server;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

public class HubJoiner implements IdSetter {

	private MulticastSocket dataSend;

	private MulticastSocket dataReceive;

	private InetAddress groupIP;

	private int portSend;
	private int portReceive;

	private Thread dataReceiver, addRequestor, pinger, updateTester;

	private Map<Integer, PlayerData> playersData;

	Map<Integer, Long> updates;

	private PlayerData myPlayer;

	private int myID;

	public HubJoiner(String playerUsername, Color playerColor, String groupIP, int portSend) {
		this.myID = -1;
		this.myPlayer = new PlayerData(-1, playerUsername, playerColor);

		this.portSend = portSend;

		this.updates = new HashMap<>();

		this.playersData = new HashMap<>();

		try {
			this.groupIP = InetAddress.getByName(groupIP);

			this.dataSend = new MulticastSocket();
			this.dataSend.setInterface(InetAddress.getLocalHost());
			this.dataSend.joinGroup(this.groupIP);

			this.dataReceive = new MulticastSocket();
			this.dataReceive.setInterface(InetAddress.getLocalHost());
			this.dataReceive.joinGroup(this.groupIP);

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.portReceive = this.dataReceive.getLocalPort();

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

		this.updates.put(id, System.currentTimeMillis());
	}

	private void processData(String data) {
		String[] splited = data.split("/");

		switch (Request.valueOf(splited[0])) {
		case UPDATE:
			this.playerDataReceived(splited);
			break;
		default:
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

					HubJoiner.this.send(Request.ADD + "/" + HubJoiner.this.portReceive + "/"
							+ HubJoiner.this.myPlayer.getId() + "/" + HubJoiner.this.myPlayer.getUsername() + "/" + "#"
							+ Integer.toHexString(HubJoiner.this.myPlayer.getColor().getRGB()).substring(2) + "/");

					try {
						Thread.sleep(ServerDelays.REQUEST_RATE);
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
		this.pinger.start();
	}

	private void setPinger() {
		this.pinger = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {

					if (HubJoiner.this.myID == -1) {
						Thread.currentThread().interrupt();
						return;
					}

					HubJoiner.this.send(Request.PING + "/" + HubJoiner.this.portReceive + "/"
							+ HubJoiner.this.myPlayer.getId() + "/");

					try {
						Thread.sleep(ServerDelays.PING_RATE);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		});
	}

	public void setUpdateTester() {
		this.updateTester = new Thread(new Runnable() {
			@Override
			public void run() {

				while (Thread.currentThread().isInterrupted() == false) {
					try {
						Thread.sleep(ServerDelays.UPDATE_TEST_RATE);
					} catch (InterruptedException e) {
					}

					long time = System.currentTimeMillis();

					for (Integer id : HubJoiner.this.updates.keySet()) {
						if (updates.get(id) > ) {

						}
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
		this.addRequestor.interrupt();
		this.pinger.interrupt();
	}
}
