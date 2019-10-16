package fr.state.game.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import fr.state.game.solo.Player;
import fr.util.point.Point;

public class Server {

	private InetAddress ip;
	private int portR;

	private DatagramSocket dsR;

	private Thread tr;

	private int portS;

	private DatagramSocket dsS;

	private Thread ts;

	private Player player;

	public Server() {
		this.player = new Player();

		this.player.setPos(new Point(200, 200));
		this.player.setSize(new Point(200, 200));

		try {
			this.ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}

		this.portR = 10000;
		this.portS = 10001;

		this.tr = null;
		this.ts = null;

		try {
			this.dsR = new DatagramSocket(this.portR);
			this.dsS = new DatagramSocket();
		} catch (SocketException e) {
		}

		this.listen();
		this.send();
	}

	private void listen() {
		this.tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {

						byte[] buffer = new byte[8192];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						Server.this.dsR.receive(packet);

						ByteArrayInputStream bs = new ByteArrayInputStream(buffer);
						ObjectInputStream is = new ObjectInputStream(bs);

						Object object = is.readObject();
						is.close();

						Server.this.received(object);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void received(Object o) {
		System.out.println("Objet Recu server");
	}

	private void send() {
		this.ts = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						ByteArrayOutputStream bs = new ByteArrayOutputStream(8192);
						ObjectOutputStream os = new ObjectOutputStream(bs);

						os.flush();
						os.writeObject(Server.this.player);
						os.flush();

						byte[] buffer = bs.toByteArray();
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length, Server.this.ip,
								Server.this.portS);

						Server.this.dsS.send(packet);
						os.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void start() {
		this.tr.start();
		this.ts.start();
	}
}
