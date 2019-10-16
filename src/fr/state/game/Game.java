package fr.state.game;

import java.awt.Graphics2D;
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

public class Game {

	private GameState gameState;

	private Player player;

	private InetAddress ip;
	private int portR;

	private DatagramSocket dsR;

	private Thread tr;

	private int portS;

	private DatagramSocket dsS;

	private Thread ts;

	private Input lastInput;

	private Object syncLastInput;
	private Object syncPlayer;

	public Game(GameState gameState) {
		this.gameState = gameState;
		this.player = new Player();
		this.player.setPos(new Point(200, 200));
		this.player.setSize(new Point(200, 200));
		this.syncLastInput = new Object();
		this.syncPlayer = new Object();

		try {
			this.ip = InetAddress.getByName("192.168.43.220");
		} catch (UnknownHostException e1) {
		}

		this.portR = 10001;
		this.portS = 10000;

		this.tr = null;
		this.ts = null;

		try {
			this.dsR = new DatagramSocket(this.portR);
			this.dsS = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		this.listen();
		this.send();
	}

	public void draw(Graphics2D g, double dt) {
		this.player.draw(g, dt);
	}

	public GameState getGameState() {
		return this.gameState;
	}

	private void listen() {
		this.tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {

						byte[] buffer = new byte[8192];
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

						Game.this.dsR.receive(packet);

						ByteArrayInputStream bs = new ByteArrayInputStream(buffer);
						ObjectInputStream is = new ObjectInputStream(bs);

						Object object = is.readObject();
						is.close();

						Game.this.received(object);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void received(Object o) {
		Player sPlayer = (Player) o;
		this.player = sPlayer;
	}

	public void resetForces() {
		this.player.resetForces();
	}

	private void send() {
		this.ts = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(1);

						ByteArrayOutputStream bs = new ByteArrayOutputStream(16384);
						ObjectOutputStream os = new ObjectOutputStream(bs);

						os.flush();
						os.writeObject(Game.this.lastInput);
						os.flush();

						byte[] buffer = bs.toByteArray();
						DatagramPacket packet = new DatagramPacket(buffer, buffer.length, Game.this.ip,
								Game.this.portS);

						Game.this.dsS.send(packet);
						os.close();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setMenuState(GameState gameState) {
		this.gameState = gameState;
	}

	public void start() {
		this.tr.start();
		this.ts.start();
	}

	public void update(Input input, double dt) {
		this.lastInput = input;
		// this.player.update(input, dt);
	}

}
