package fr.state.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.inputs.Input;
import fr.logger.Logger;
import fr.server.GlobalServerData;
import fr.server.p2p.Multiplayer;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;

public class GameState implements IAppState {

	private Game game;

	private StatePanel sp;

	private Input input;

	private GameLoop loop;

	private final int GRAY = 40;

	private final Color BACKGROUND = new Color(this.GRAY, this.GRAY, this.GRAY);

	private double repaintDt;

	private Map<String, Object> initData;

	private Multiplayer multiplayer;

	public GameState() {
		this.initData = null;
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.game != null) {
			this.game.draw(g, this.repaintDt);
		}
	}

	public void getInput() {
		this.input.getInput();
	}

	@Override
	public String getName() {
		return "game";
	}

	public StatePanel getStatePanel() {
		return this.sp;
	}

	public void setDt(double dt) {
		this.repaintDt = dt;
	}

	@Override
	public void setInitData(Map<String, Object> data) {
		this.initData = data;
	}

	@Override
	public void start(StatePanel panel) {

		if (this.initData == null) {
			Logger.err("Les donnees d'initialisation n'ont pas ete affectees");
			System.exit(0);
		}

		DatafilesManager dfm = DatafilesManager.getInstance();

		Object serverConf = dfm.getFile("serverConf");

		XMLManager xml = dfm.getXmlManager();

		int serverID = (int) xml.getParam(serverConf, "id", 0);

		String groupIP = GlobalServerData.getGroup(serverID);

		int myId = (int) this.initData.get("myId");

		@SuppressWarnings("unchecked")
		Map<Integer, Map<String, Object>> playersData = (Map<Integer, Map<String, Object>>) this.initData
				.get("playersData");

		int port = GlobalServerData.getP2PPort(serverID);

		this.sp = panel;

		List<Integer> ids = new ArrayList<>();

		for (Integer id : playersData.keySet()) {
			ids.add(id);
		}

		this.multiplayer = new Multiplayer(groupIP, port, ids);

		this.loop = new GameLoop(this);

		this.game = new Game(this, this.multiplayer, panel.getWinData(), myId, playersData, this.loop.getDtUpdate());
		this.multiplayer.setPDataProcessor(this.game);

		this.sp.setBackground(this.BACKGROUND);

		this.input = new Input(this.sp.getWinData());

		this.sp.addKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.addKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.addMouseListener(this.input.getMouseEventListener());
		this.sp.addMouseListener(this.input.getMouseMirrorListener());

		this.loop.start();

		this.multiplayer.start();
	}

	@Override
	public void stop() {
		this.sp.removeKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.removeKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.removeMouseListener(this.input.getMouseEventListener());
		this.sp.removeMouseListener(this.input.getMouseMirrorListener());
		this.game = null;
		this.sp = null;
		this.input = null;
		this.loop.stop();
		this.loop = null;
		this.multiplayer.stop();
		this.multiplayer = null;

		System.gc();
	}

	@Override
	public void update() {
	}

	public void update(double dt) {
		this.game.resetForces();
		this.game.update(this.input, dt);
	}
}
