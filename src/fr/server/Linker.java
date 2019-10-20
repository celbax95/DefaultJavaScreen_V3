package fr.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class Linker {

	InetAddress groupIP;

	int portLinker;

	MulticastSocket reqListener;
	DatagramSocket responseSender;

	List<InetAddress> connections;

	Thread listener;

	public Linker(String groupLinker, int portLinker) {

		this.connections = new ArrayList<>();
		this.portLinker = portLinker;

		try {
			this.groupIP = InetAddress.getByName(groupLinker);

			this.reqListener = new MulticastSocket(this.portLinker);
			this.reqListener.setInterface(InetAddress.getLocalHost());
			this.reqListener.joinGroup(this.groupIP);

			this.responseSender = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		this.setListener();
	}

	private void processPacket(InetAddress inetAddress, String data) {

		String[] splited = data.split("/");

		int askerPort = Integer.valueOf(splited[0]);

		if (askerPort < 0 || askerPort > 65000) {
			System.err.println("Le port reçu est invalide");
			return;
		}

		int id;
		if (this.connections.contains(inetAddress)) {
			id = this.connections.indexOf(inetAddress);
		} else {
			// id client
			id = this.connections.size();

			this.connections.add(inetAddress);
		}

		this.sendResponse(inetAddress, askerPort, id);
	}

	private void sendResponse(InetAddress askerIP, int askerPort, int id) {

		byte[] buffer = (askerIP + "/" + String.valueOf(id) + "/").getBytes();

		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, askerIP, askerPort);

		try {
			this.responseSender.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
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
						e.printStackTrace();
					}

					Linker.this.processPacket(packet.getAddress(), new String(packet.getData()));
				}

				try {
					Linker.this.reqListener.leaveGroup(Linker.this.groupIP);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Linker.this.reqListener.close();
			}
		});
	}

	public void start() {
		this.listener.start();
	}

	public void stop() {
		this.listener.interrupt();
	}
}
