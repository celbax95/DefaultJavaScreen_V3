package fr.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Searcher {

	private int SEND_RATE;

	private MulticastSocket reqSender;

	private DatagramSocket responseReceiver;

	private InetAddress groupIP;

	private int hostPort;

	private Thread sender, receiver;

	private byte[] message;

	private int myID;

	public Searcher() {
		try {
			this.reqSender = new MulticastSocket();
			this.reqSender.setInterface(InetAddress.getLocalHost());
			this.reqSender.joinGroup(this.groupIP);

			this.responseReceiver = new DatagramSocket(this.hostPort - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processResponse(String data) {
		String[] splited = data.split("/");

		try {
			if (splited[0].equals(InetAddress.getLocalHost().getHostAddress())) {
				this.myID = Integer.valueOf(splited[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (this.myID != -1) {
			// TODO pass to hoster;
		}
	}

	private void receiveResponse() {
		this.receiver = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					byte[] buffer = new byte[1000];

					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

					Searcher.this.responseReceiver.receive(packet);

					Searcher.this.processResponse(new String(packet.getData()));
				} catch (Exception e) {
					e.printStackTrace();
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
								Searcher.this.groupIP, Searcher.this.hostPort);

						Searcher.this.reqSender.send(packet);

						Thread.sleep(Searcher.this.SEND_RATE);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
