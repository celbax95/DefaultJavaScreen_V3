package fr.state.loading;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
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

	public LoadingCore(Multiplayer multiplayer, int myId, List<Integer> ids, LoadingRequestor requestor) {
		this.ids = ids;
		this.requestor = requestor;

		this.waitingIds = null;
		if (requestor != null) {
			this.waitingIds = new LinkedList<>();
			for (Integer id : ids) {
				if (id != myId) {
					this.waitingIds.add(id);
				}
			}
		}

		DatafilesManager dfm = DatafilesManager.getInstance();
		XMLManager manager = dfm.getXmlManager();

		Object profileConf = dfm.getFile("profile");

		String username = (String) manager.getParam(profileConf, "username", "");
		String colorHex = (String) manager.getParam(profileConf, "color", "#000000");
		Color color = Color.decode(colorHex);

		this.players = new HashMap<>();
		// TODO Generer les positions des joueurs
		this.players.put(myId, new PlayerData(myId, username, new Point(), color));

		this.myId = myId;

		this.multiplayer = multiplayer;
		this.multiplayer.setPDataProcessor(this);
	}

	@Override
	public void newPData(PData pdata) {
		OP op = pdata.getOpId();

		if (pdata.getId() != this.myId) {
			if (op == OP.PLAYER_STATE) {
				Object[] data = pdata.getData();

				int i = 0;

				this.players.put(pdata.getId(),
						new PlayerData(pdata.getId(), (String) data[i++], (Point) data[i++], (Color) data[i++]));

				if (this.waitingIds != null) {
					this.waitingIds.remove((Object) pdata.getId());
					if (this.waitingIds.isEmpty()) {
						this.requestor.stop();
						this.waitingIds = null;
						this.requestor = null;
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
				PlayerData myPlayer = LoadingCore.this.players.get(LoadingCore.this.myId);
				while (Thread.currentThread().isInterrupted() == false) {
					PData data = factory.createPlayerState(LoadingCore.this.myId, myPlayer.getUsername(),
							myPlayer.getPos(), myPlayer.getColor());

					LoadingCore.this.multiplayer.send(data);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		this.sender.setName("LoadingCore/sender");
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
		if (this.requestor != null) {
			this.requestor.stop();
		}
		if (this.sender != null) {
			this.sender.interrupt();
		}
	}
}