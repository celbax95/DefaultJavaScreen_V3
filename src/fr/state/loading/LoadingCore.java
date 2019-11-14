package fr.state.loading;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.server.p2p.Multiplayer;
import fr.server.p2p.PData;
import fr.server.p2p.PData.OP;
import fr.server.p2p.PDataFactory;
import fr.server.p2p.PDataProcessor;
import fr.statepanel.AppStateManager;
import fr.statepanel.IAppState;
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

	LoadingTemplate template;

	int step, maxSteps;

	LoadingState state;

	private long lastNewPlayerReceived;

	private Thread timeoutTester;

	private final int TIMEOUT_TIME = 5000;

	private boolean timeout;

	public LoadingCore(LoadingState state, Multiplayer multiplayer, int myId, List<Integer> ids,
			LoadingRequestor requestor, LoadingTemplate template) {

		this.state = state;

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

		this.players.put(myId, new PlayerData(myId, username,
				new Point(new Random().nextDouble() * 900, new Random().nextDouble() * 500), color));

		this.myId = myId;

		this.multiplayer = multiplayer;
		this.multiplayer.setPDataProcessor(this);

		this.step = 1;
		this.maxSteps = ids.size();

		this.timeoutTester = null;
		this.lastNewPlayerReceived = System.currentTimeMillis();
		this.timeout = false;

		this.template = template;
		this.template.setMaxSteps(this.maxSteps);
		template.setAction(() -> {
			this.loadGame();
		});
	}

	public void closeSockets() {
		synchronized (this.multiplayer) {
			this.multiplayer.closeSockets();
		}
	}

	private void loadGame() {
		Map<Integer, Map<String, Object>> playersData = new HashMap<>();

		for (Integer id : this.ids) {
			Map<String, Object> data = new HashMap<>();
			data.put("pos", this.players.get(id).getPos());
			data.put("username", this.players.get(id).getUsername());
			data.put("color", this.players.get(id).getColor());

			playersData.put(id, data);
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		AppStateManager asm = this.state.getStatePanel().getAppStateManager();

		Map<String, Object> initData = new HashMap<>();

		initData.put("myId", this.myId);

		initData.put("playersData", playersData);

		IAppState nextState = asm.getState("game");
		nextState.setInitData(initData);

		this.state.getStatePanel().setState(nextState);
	}

	private void loadMenu() {
		AppStateManager asm = this.state.getStatePanel().getAppStateManager();

		this.state.getStatePanel().setState(asm.getState("menu"));
	}

	@Override
	public void newPData(PData pdata) {
		OP op = pdata.getOpId();

		if (pdata.getId() != this.myId) {
			if (op == OP.PLAYER_STATE) {

				Object[] data = pdata.getData();

				int i = 0;

				if (this.ids.contains(pdata.getPlayerId()) && !this.players.containsKey(pdata.getPlayerId())) {
					this.lastNewPlayerReceived = System.currentTimeMillis();
					this.step++;
					this.template.setStep(this.step);
				}

				// On met a jour le joueur
				this.players.put(pdata.getPlayerId(),
						new PlayerData(pdata.getPlayerId(), (String) data[i++], (Point) data[i++], (Color) data[i++]));

				// Le host verifie s'il y a encore besoin du requestor
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
				synchronized (LoadingCore.this.multiplayer) {
					while (Thread.currentThread().isInterrupted() == false) {
						PData data = factory.createPlayerState(LoadingCore.this.myId, myPlayer.getUsername(),
								myPlayer.getPos(), myPlayer.getColor());

						LoadingCore.this.multiplayer.send(data);

						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
						}
					}
				}
			}
		});
		this.sender.setName("LoadingCore/sender");
	}

	private void setTimeoutTester() {
		this.timeoutTester = new Thread(new Runnable() {
			@Override
			public void run() {
				while (Thread.currentThread().isInterrupted() == false) {
					if (LoadingCore.this.players.size() < LoadingCore.this.ids.size()) {
						if (System.currentTimeMillis() > LoadingCore.this.lastNewPlayerReceived
								+ LoadingCore.this.TIMEOUT_TIME) {
							LoadingCore.this.timeout = true;
						}
					}

					try {
						Thread.sleep(1500);
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

		this.setTimeoutTester();
		this.timeoutTester.start();
	}

	public void stop() {
		this.multiplayer.stop();
		if (this.requestor != null) {
			this.requestor.stop();
		}
		if (this.sender != null) {
			this.sender.interrupt();
		}
		if (this.timeoutTester != null) {
			this.timeoutTester.interrupt();
		}
	}

	public void update() {
		if (this.timeout) {
			this.loadMenu();
		}
	}
}