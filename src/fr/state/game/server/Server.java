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

import fr.inputs.Input;
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

	private Thread tl;

	private Player player;

	private Input input;

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
		this.tl = null;

		try {
			this.dsR = new DatagramSocket(this.portR);
			this.dsS = new DatagramSocket();
		} catch (SocketException e) {
		}

		this.listen();
		this.send();
		this.gameLoop();
	}

	public void gameLoop() {
		this.tl = new Thread(new Runnable() {
			@Override
			public void run() {
				final double MS_TO_SLEEP = 2;
				double elapsed = 0;
				double lastFrame = System.currentTimeMillis();
				double currentTime = System.currentTimeMillis();
				double updates = 40;
				double lastUpdate = System.currentTimeMillis();
				double dtUpdates = 1000 / updates;

				double accuUpdate = 0;

				while (!Thread.currentThread().isInterrupted()) {
					try {
						// init
						currentTime = System.currentTimeMillis();

						elapsed = currentTime - lastFrame;

						accuUpdate += elapsed;

						if (accuUpdate < dtUpdates) {
							long sleepTime = (long) Math.floor(dtUpdates - accuUpdate);

							if (sleepTime > MS_TO_SLEEP) {
								Thread.sleep(sleepTime);
								continue;
							}
						}

						lastFrame = currentTime;

						while (accuUpdate > dtUpdates) {
							Server.this.updateGame(Server.this.input, (currentTime - lastUpdate) / 1000);
							accuUpdate -= dtUpdates;
							lastUpdate = currentTime;
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void listen() {
		this.tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!Thread.currentThread().isInterrupted()) {

						byte[] buffer = new byte[16384];
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
		this.input = (Input) o;
	}

	private void send() {
		this.ts = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!Thread.currentThread().isInterrupted()) {
						Thread.sleep(1);

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
		this.tl.start();
	}

	private void updateGame(Input input, double dt) {
		if (input == null)
			return;

		this.player.update(input, dt);
	}
}
