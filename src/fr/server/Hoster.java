package fr.server;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
import java.util.Map;

public class Hoster {

	int UPDATE_RATE;

	MulticastSocket dataShare;

	MulticastSocket dataReceive;

	InetAddress groupIP;

	int portSend, portReceive;

	Thread dataRequestor, dataUpdqter, dataReceiver;

	Map<Integer, PlayerData> playersData;

	List<Integer> missingData;

	final int REQUEST = 0, UPDATE = 1;

	public Hoster() {

	}

	private void playerDataReceived(String[] data) {
		int id = Integer.valueOf(data[1]);

		// le player data est attendu
		if (this.missingData.contains(id) && this.playersData.containsKey(id)) {

			String username = data[2];

			String colorTxt = data[3];

			Color color = Color.BLACK;

			if (colorTxt.substring(1, 1).equals("#") && colorTxt.length() == 7) {
				color = Color.decode(colorTxt);
			}

			this.playersData.put(id, new PlayerData(username, color));
			this.missingData.remove((Object) id);
		}
	}

	private void processData(String data) {
		String[] splited = data.split("/");

		switch (Integer.valueOf(splited[0])) {
		case UPDATE:
			this.playerDataReceived(splited);
			break;
		case REQUEST:
			break;
		}
	}

	public void send(String message) {
		try {
			byte[] buffer = message.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, Hoster.this.groupIP,
					Hoster.this.portSend);

			Hoster.this.dataShare.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDataRequestor() {
		this.dataRequestor = new Thread(new Runnable() {
			@Override
			public void run() {
				int id = 0;

				while (Thread.currentThread().isInterrupted() == false && Hoster.this.missingData.size() > 0) {

					Hoster.this.send(Hoster.this.REQUEST + "/"
							+ Hoster.this.missingData.get(id++ % Hoster.this.missingData.size()) + "/");
				}
			}
		});
	}

	public void setDataUpdater() {
		this.dataUpdqter = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false && Hoster.this.missingData.size() > 0) {

					for (Integer id : Hoster.this.playersData.keySet()) {

						PlayerData pd = Hoster.this.playersData.get(id);

						Hoster.this.send(Hoster.this.UPDATE + "/" + id + "/" + pd.getUsername() + "/" + "#"
								+ Integer.toHexString(pd.getColor().getRGB()).substring(2) + "/");
					}

					try {
						Thread.sleep(Hoster.this.UPDATE_RATE);
					} catch (InterruptedException e) {
//						e.printStackTrace();
					}
				}
			}
		});
	}

	public void setListener() {
		this.dataReceiver = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false && Hoster.this.missingData.size() > 0) {

					try {
						byte[] buffer = new byte[1000];

						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						Hoster.this.dataReceive.receive(packet);

						Hoster.this.processData(new String(packet.getData()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
