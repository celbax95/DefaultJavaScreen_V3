package fr.state.loading;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.server.p2p.Multiplayer;
import fr.server.p2p.PData;
import fr.server.p2p.PData.OP;
import fr.server.p2p.PDataFactory;
import fr.server.p2p.PDataProcessor;
import fr.util.point.Point;

public class LoadingCore implements PDataProcessor {

	List<Integer> ids;
	Multiplayer multiplayer;
	int myId;

	// Partie host
	LoadingRequestor requestor;
	List<Integer> waitingIds;

	Map<Integer, PlayerData> players;

	Thread sender;

	public LoadingCore(Multiplayer m, int myId, List<Integer> ids, LoadingRequestor requestor) {
		this.ids = ids;
		this.requestor = requestor;

		this.waitingIds = null;
		if (requestor != null) {
			this.waitingIds = new LinkedList<>();
			for (Integer id : ids) {
				this.waitingIds.add(id);
			}
		}

		this.myId = myId;

		this.multiplayer = m;
		this.multiplayer.setPDataProcessor(this);
	}

	@Override
	public void newPData(PData pdata) {
		OP op = pdata.getOpId();

		System.out.println("okok");

		if (pdata.getId() != this.myId) {
			if (op == OP.PLAYER_STATE) {
				Object[] data = pdata.getData();

				int i = 0;

				this.players.put(pdata.getId(), new PlayerData(pdata.getId(), (String) data[i++], (Point) data[i++],
						(Point) data[i++], (Color) data[i++]));

				if (this.waitingIds != null) {
					this.waitingIds.remove((Object) pdata.getId());
					if (this.waitingIds.isEmpty()) {
						this.requestor.stop();
					}
				}
			}
		}
	}

	public void setSender() {
		this.sender = new Thread(new Runnable() {
			@Override
			public void run() {
				PDataFactory factory = new PDataFactory();
				while (Thread.currentThread().isInterrupted() == false) {
					PData data = factory.createPlayerState(LoadingCore.this.myId, "coucou", new Point(100, 100),
							new Point(100, 100), Color.black);

					LoadingCore.this.multiplayer.send(data);

					if (LoadingCore.this.requestor != null) {
						System.out.println("send1");
					} else {
						System.out.println("send2");
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}

	public void start() {
		this.stop();
		this.multiplayer.start();
		if (this.requestor != null) {
			this.requestor.start();
		}
		this.setSender();
		this.sender.start();
	}

	public void stop() {
		this.multiplayer.stop();
		this.requestor.stop();
		if (this.sender != null) {
			this.sender.interrupt();
		}
	}
}