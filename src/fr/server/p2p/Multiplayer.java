package fr.server.p2p;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
import java.util.Map;

public class Multiplayer {

	private static final int BYTE_AMOUNT = (int) Math.pow(2, 10);

	private MulticastSocket socket;

	private InetAddress groupIP;

	private int groupPort;

	private List<Integer> receivers; // id des receivers

	private Map<Integer, Confirmer> needConfirm;

	private PDataProcessor pDataProcessor;

	private PDataFactory pDataFactory;

	private Thread receiver;

	public Multiplayer(String groupIP, int port, List<Integer> receivers) {

		this.pDataProcessor = null;

		this.receivers = receivers;

		this.socket = null;

		this.groupIP = null;

		this.groupPort = port;

		this.receiver = null;

		this.pDataFactory = new PDataFactory();

		try {
			this.groupIP = InetAddress.getByName(groupIP);

			this.socket = new MulticastSocket(port);
			this.socket.setInterface(InetAddress.getLocalHost());
			this.socket.joinGroup(this.groupIP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void confirm(int idPData) {
		this.needConfirm.remove(idPData);
	}

	public PDataFactory getPDataFactory() {
		return this.pDataFactory;
	}

	public PDataProcessor getPDataProcessor() {
		return this.pDataProcessor;
	}

	public MulticastSocket getSocket() {
		return this.socket;
	}

	public void received(Object o) {
		if (o instanceof PData) {

			PData data = (PData) o;

			if (data.getOpId() == PData.OP.CONFIRM) {
				this.needConfirm.get(data.getId()).confirm(data);
			} else if (this.pDataProcessor != null) {
				this.pDataProcessor.newPData(data);
			}
		}
	}

	public void send(PData data) {
		try {
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bs);

			os.flush();
			os.writeObject(data);
			os.flush();
			os.close();

			byte[] buffer = bs.toByteArray();

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.groupIP, this.groupPort);

			if (data.isNeedConfirm()) {
				this.needConfirm.put(data.getId(), new Confirmer(data.getId(), this, packet, this.receivers));
			}

			this.socket.send(packet);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPDataProcessor(PDataProcessor pDataProcessor) {
		this.pDataProcessor = pDataProcessor;
	}

	public void setReceiver() {
		this.receiver = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!Thread.currentThread().isInterrupted()) {

						byte[] buffer = new byte[BYTE_AMOUNT];

						DatagramPacket packet = new DatagramPacket(buffer, buffer.length, Multiplayer.this.groupIP,
								Multiplayer.this.groupPort);

						Multiplayer.this.socket.receive(packet);

						ByteArrayInputStream bs = new ByteArrayInputStream(buffer);
						ObjectInputStream is = new ObjectInputStream(bs);

						Multiplayer.this.received(is.readObject());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void start() {
		this.stop();
		this.setReceiver();
		this.receiver.start();
	}

	public void stop() {
		if (this.receiver != null) {
			this.receiver.interrupt();
		}
	}
}
