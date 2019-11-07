package fr.server.serverlink.link;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

import fr.server.serverlink.data.Request;

public class Linker {

	private InetAddress groupIP;

	private int portLinker;

	private MulticastSocket reqListener;
	private DatagramSocket responseSender;

	private Map<String, Integer> connections;

	private Thread listener;

	private int myID;

	private int curID;

	public Linker(int myID, String groupLinker, int portLinker) {

		this.connections = new HashMap<>();
		this.portLinker = portLinker;

		this.myID = myID;

		this.curID = 0;

		try {
			this.groupIP = InetAddress.getByName(groupLinker);

			this.reqListener = new MulticastSocket(this.portLinker);
			this.reqListener.setInterface(InetAddress.getLocalHost());
			this.reqListener.joinGroup(this.groupIP);

			this.responseSender = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processPacket(InetAddress inetAddress, String data) {

		String[] splited = data.split("/");

		if (Request.valueOf(splited[0]) != Request.REQID)
			return;

		int askerPort = Integer.valueOf(splited[1]);

		int id;

		String key = inetAddress.getHostAddress() + ":" + askerPort;

		if (this.connections.containsKey(key)) {
			id = this.connections.get(key);
		} else {
			// id client
			if (this.curID == this.myID) {
				this.curID++;
				id = this.curID;
			} else {
				id = this.curID;
			}
			this.curID++;

			this.connections.put(key, id);
		}

		this.sendResponse(inetAddress, askerPort, id);
	}

	private void sendResponse(InetAddress askerIP, int askerPort, int id) {

		byte[] buffer = (Request.REQID + "/" + askerIP.getHostAddress() + "/" + String.valueOf(id) + "/").getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, askerIP, askerPort);

		try {
			this.responseSender.send(packet);
		} catch (IOException e) {
			Thread.currentThread().interrupt();
			return;
		}
	}

	private void setListener() {
		this.listener = new Thread(new Runnable() {

			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {

					byte[] buffer = new byte[1000];

					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

					try {
						Linker.this.reqListener.receive(packet);
					} catch (IOException e) {
						Thread.currentThread().interrupt();
						return;
					}

					Linker.this.processPacket(packet.getAddress(), new String(packet.getData()));
				}

				try {
					Linker.this.reqListener.leaveGroup(Linker.this.groupIP);
				} catch (IOException e) {
					Thread.currentThread().interrupt();
					return;
				}
				Linker.this.reqListener.close();
			}
		});
	}

	public void start() {
		if (this.listener != null) {
			this.listener.interrupt();
		}
		this.setListener();
		this.listener.start();
	}

	public void stop() {
		this.listener.interrupt();
	}
}
