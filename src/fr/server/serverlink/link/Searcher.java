package fr.server.serverlink.link;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import fr.server.serverlink.data.Request;

public class Searcher {

	private static final int SEND_RATE = 1500;

	private MulticastSocket reqSender;

	private DatagramSocket responseReceiver;

	private InetAddress groupIP;

	private int linkerPort;

	private Thread sender, receiver;

	private byte[] message;

	private int myID;

	private IdSetter idSetter;

	public Searcher(IdSetter idSetter, String groupIP, int linkerPort) {
		this.myID = -1;

		this.idSetter = idSetter;

		this.linkerPort = linkerPort;
		try {
			this.groupIP = InetAddress.getByName(groupIP);

			this.reqSender = new MulticastSocket();
			this.reqSender.setInterface(InetAddress.getLocalHost());
			this.reqSender.joinGroup(this.groupIP);

			this.responseReceiver = new DatagramSocket();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.message = (Request.REQID + "/" + this.responseReceiver.getLocalPort() + "/").getBytes();
	}

	private void processResponse(String data) {
		String[] splited = data.split("/");

		try {
			if (splited[1].equals(InetAddress.getLocalHost().getHostAddress())) {
				this.myID = Integer.valueOf(splited[2]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (this.myID != -1) {
			this.idSetter.setId(this.myID);
			this.stop();
		}
	}

	private void setReceiver() {
		this.receiver = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {
					try {
						byte[] buffer = new byte[1000];

						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						Searcher.this.responseReceiver.receive(packet);

						Searcher.this.processResponse(new String(packet.getData()));
					} catch (Exception e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		});
	}

	private void setSender() {
		this.sender = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {
					try {
						DatagramPacket packet = new DatagramPacket(Searcher.this.message, Searcher.this.message.length,
								Searcher.this.groupIP, Searcher.this.linkerPort);

						Searcher.this.reqSender.send(packet);

						Thread.sleep(Searcher.SEND_RATE);

					} catch (Exception e) {
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		});
	}

	public void start() {
		this.myID = -1;

		if (this.receiver != null) {
			this.receiver.interrupt();
		}
		this.setReceiver();
		this.receiver.start();

		if (this.sender != null) {
			this.sender.interrupt();
		}
		this.setSender();
		this.sender.start();
	}

	public void stop() {
		this.receiver.interrupt();
		this.sender.interrupt();
	}
}
