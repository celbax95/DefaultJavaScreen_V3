package fr.server.serverlink.hub;

import java.awt.Color;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.server.serverlink.data.HubPlayerData;
import fr.server.serverlink.data.Request;
import fr.server.serverlink.data.ServerDelays;
import fr.server.serverlink.link.IdSetter;

public abstract class HubJoiner implements IdSetter {

	private MulticastSocket dataSend;

	private MulticastSocket dataReceive;

	private InetAddress groupIP;

	private int portSend;
	private int portReceive;

	private Thread dataReceiver, addRequestor, pinger, updateTester;

	private Map<Integer, HubPlayerData> playersData;

	private Map<Integer, Long> updates;

	private HubPlayerData myPlayer;

	private int myID;

	private boolean ready;

	public HubJoiner(String playerUsername, Color playerColor, String groupIP, int portSend) {
		this.myID = -1;
		this.myPlayer = new HubPlayerData(-1, playerUsername, playerColor, false);

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
			this.dataReceive.setSoTimeout(2000);

		} catch (Exception e) {
			e.printStackTrace();
		}

		this.portReceive = this.dataReceive.getLocalPort();
	}

	public void close() {
		this.stop();
		this.closeSockets();
	}

	public void closeSockets() {
		if (this.dataSend != null) {
			this.dataSend.close();
		}
		if (this.dataReceive != null) {
			this.dataReceive.close();
		}
	}

	public abstract void gameStarted();

	public abstract void idAssigned(int id);

	public abstract void noMorePlayer();

	public abstract void playerAdded(int id, String username, Color color);

	private void playerDataReceived(String[] data) {
		int id = Integer.valueOf(data[1]);

		if (id == this.myID) {
			this.addRequestor.interrupt();
		}

		int i = 2;

		// le player id est inconnu
		if (!this.playersData.containsKey(id)) {
			String username = data[i++];

			String colorTxt = data[i++];

			Color color = Color.BLACK;

			if (colorTxt.substring(0, 1).equals("#") && colorTxt.length() == 7) {
				color = Color.decode(colorTxt);
			}

			boolean ready = Boolean.parseBoolean(data[i++]);

			this.playersData.put(id, new HubPlayerData(id, username, color, ready));

			if (Thread.currentThread().isInterrupted() == false) {
				this.playerAdded(id, username, color);
				this.readyChanged(id, ready);
			}

		} else {
			HubPlayerData pd = this.playersData.get(id);
			pd.setUsername(data[i++]);

			String colorTxt = data[i++];
			if (colorTxt.substring(0, 1).equals("#") && colorTxt.length() == 7) {
				pd.setColor(Color.decode(colorTxt));
			}

			boolean ready = Boolean.parseBoolean(data[i++]);

			if (ready != pd.isReady()) {
				pd.setReady(ready);
				this.readyChanged(id, ready);
			}
		}

		this.updates.put(id, System.currentTimeMillis());
	}

	public abstract void playerRemoved(int id);

	private void processData(String data) {
		String[] splited = data.split("/");

		switch (Request.valueOf(splited[0])) {
		case UPDATE:
			this.playerDataReceived(splited);
			break;
		case LOADING_STATE_REQ:
			this.gameStarted();
		default:
			break;
		}
	}

	public abstract void readyChanged(int id, boolean ready);

	private void send(String message) {
		try {
			byte[] buffer = message.getBytes();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.groupIP, this.portSend);

			HubJoiner.this.dataSend.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setAddRequestor() {
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
		this.addRequestor.setName("HubJoiner/addRequestor");
	}

	private void setDataReceiver() {
		this.dataReceiver = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {

					try {
						byte[] buffer = new byte[1000];

						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						try {

							HubJoiner.this.dataReceive.receive(packet);

							HubJoiner.this.processData(new String(packet.getData()));

						} catch (SocketTimeoutException e) {
						}
					} catch (Exception e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		});
		this.dataReceiver.setName("HubJoiner/dataReceiver");
	}

	@Override
	public void setId(int id) {
		this.myPlayer.setId(id);
		this.myID = id;
		this.playersData.put(id, this.myPlayer);
		this.idAssigned(id);

		if (this.addRequestor != null) {
			this.addRequestor.interrupt();
		}
		this.setAddRequestor();
		this.addRequestor.start();

		if (this.pinger != null) {
			this.pinger.interrupt();
		}
		this.setPinger();
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
							+ HubJoiner.this.myPlayer.getId() + "/" + HubJoiner.this.ready + "/");

					try {
						Thread.sleep(ServerDelays.PING_RATE);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		});
		this.pinger.setName("HubJoiner/pinger");
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	private void setUpdateTester() {
		this.updateTester = new Thread(new Runnable() {
			@Override
			public void run() {

				while (Thread.currentThread().isInterrupted() == false) {
					try {
						Thread.sleep(ServerDelays.UPDATE_TEST_RATE);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}

					long testTime = System.currentTimeMillis() - ServerDelays.UPDATE_TEST_RATE;

					List<Integer> idsToRemove = new ArrayList<>();

					for (Integer id : HubJoiner.this.updates.keySet()) {

						if (id != HubJoiner.this.myID && HubJoiner.this.updates.get(id) < testTime) {
							idsToRemove.add(id);
						}
					}

					for (Integer id : idsToRemove) {
						HubJoiner.this.updates.remove(id);
						HubJoiner.this.playersData.remove(id);
						HubJoiner.this.playerRemoved(id);
					}

					if (idsToRemove.size() > 0 && HubJoiner.this.playersData.size() == 1) {
						HubJoiner.this.noMorePlayer();
					}
				}
			}
		});
		this.updateTester.setName("HubJoiner/updateTester");
	}

	public void start() {
		if (this.dataReceiver != null) {
			this.dataReceiver.interrupt();
		}
		this.setDataReceiver();
		this.dataReceiver.start();

		if (this.updateTester != null) {
			this.updateTester.interrupt();
		}
		this.setUpdateTester();
		this.updateTester.start();
	}

	public void stop() {

		if (this.dataReceiver != null) {
			this.dataReceiver.interrupt();
		}
		if (this.addRequestor != null) {
			this.addRequestor.interrupt();
		}
		if (this.pinger != null) {
			this.pinger.interrupt();
		}
		if (this.updateTester != null) {
			this.updateTester.interrupt();
		}

		this.playersData.clear();

		this.myPlayer.setId(-1);
		this.myID = -1;
	}
}
