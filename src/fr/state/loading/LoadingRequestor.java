package fr.state.loading;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.List;

import fr.server.serverlink.data.Request;

public class LoadingRequestor {

	private static final int SEND_RATE = 1000;

	private MulticastSocket socket;

	private InetAddress groupIP;

	private List<Integer> portsCli;

	private Thread sender;

	public LoadingRequestor(String groupIP, List<Integer> portsCli) {
		this.portsCli = portsCli;

		this.groupIP = null;
		try {
			this.groupIP = InetAddress.getByName(groupIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			this.socket = new MulticastSocket();
			this.socket.setInterface(InetAddress.getLocalHost());
			this.socket.joinGroup(this.groupIP);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removePort(int port) {
		this.portsCli.remove(port);
	}

	public void send() {
		byte[] msg = (Request.LOADING_STATE_REQ + "/").getBytes();

		for (int port : this.portsCli) {
			DatagramPacket packet = new DatagramPacket(msg, msg.length, this.groupIP, port);
			try {
				this.socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setSender() {
		this.sender = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {
					LoadingRequestor.this.send();

					try {
						Thread.sleep(SEND_RATE);
					} catch (InterruptedException e) {
						return;
					}
				}
			}
		});
		this.sender.setName("LoadingRequestor/sender");
	}

	public void start() {
		this.stop();
		this.setSender();
		this.sender.start();
	}

	public void stop() {
		if (this.sender != null) {
			this.sender.interrupt();
		}
	}
}
