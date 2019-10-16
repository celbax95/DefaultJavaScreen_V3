package fr.state.game.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;

public class Confirmer {

	private static final int MS_TO_REPEAT = 100;

	private Map<Integer, Boolean> confirmations;

	private int pDataID;

	private Multiplayer multiplayer;

	private MulticastSocket socket;

	private DatagramPacket packet;

	public Confirmer(int pDataID, Multiplayer multiplayer, DatagramPacket packet, int[] ids) {
		this.multiplayer = multiplayer;

		this.socket = multiplayer.getSocket();

		this.packet = packet;

		this.pDataID = pDataID;

		this.confirmations = new HashMap<>();
		for (int id : ids) {
			this.confirmations.put(id, false);
		}
	}

	public void confirm(PData data) {
		this.confirmations.put(data.getId(), true);
	}

	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						Thread.sleep(MS_TO_REPEAT);
					} catch (InterruptedException e) {
						e.printStackTrace();
						Thread.currentThread().interrupt();
						return;
					}

					boolean tmpConfirmed = true;
					for (Boolean confirmation : Confirmer.this.confirmations.values()) {
						if (confirmation == false) {
							tmpConfirmed = false;
							break;
						}
					}

					if (!tmpConfirmed) {
						try {
							Confirmer.this.socket.send(Confirmer.this.packet);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						Confirmer.this.multiplayer.confirm(Confirmer.this.pDataID);
						Thread.currentThread().interrupt();
						return;
					}
				}
			}
		}).start();
	}
}
