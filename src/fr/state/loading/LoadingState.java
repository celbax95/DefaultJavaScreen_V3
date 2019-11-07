package fr.state.loading;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.logger.Logger;
import fr.server.GlobalServerData;
import fr.server.p2p.Multiplayer;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;

public class LoadingState implements IAppState {

	private StatePanel sp;

	private Input input;

	private LoadingLoop loop;

	private final int GRAY = 40;

	private Map<String, Object> initData;

	private final Color BACKGROUND = new Color(this.GRAY, this.GRAY, this.GRAY);

	public LoadingState() {
		this.initData = null;
	}

	@Override
	public void draw(Graphics2D g) {
	}

	public void getInput() {
		this.input.getInput();
	}

	@Override
	public String getName() {
		return "loading";
	}

	public StatePanel getStatePanel() {
		return this.sp;
	}

	@Override
	public void setInitData(Map<String, Object> data) {
		this.initData = data;
	}

	@Override
	public void start(StatePanel panel) {

		// A utiliser pour faire passer les infos de la map choisie
		if (this.initData == null) {
			Logger.err("Les donnees d'initialisation n'ont pas ete affectees");
			System.exit(0);
		}

		@SuppressWarnings("unchecked")
		Map<Integer, Integer> linkIDPorts = (Map<Integer, Integer>) this.initData.get("linkIDPorts");

		DatafilesManager dfm = DatafilesManager.getInstance();

		Object serverConf = dfm.getFile("serverConf");
		// Object profileConf = dfm.getFile("profile");

		XMLManager xml = dfm.getXmlManager();

		int serverID = (int) xml.getParam(serverConf, "id", 0);

		String groupIP = GlobalServerData.getGroup(serverID);

		List<Integer> portsCli = new ArrayList<>();
		for (Integer port : linkIDPorts.values()) {
			portsCli.add(port);
		}

		List<Integer> idsCli = new ArrayList<>();
		for (Integer id : linkIDPorts.keySet()) {
			idsCli.add(id);
		}

		LoadingRequestor lr = new LoadingRequestor(groupIP, portsCli);

		Multiplayer m = new Multiplayer(groupIP, GlobalServerData.getP2PPort(serverID), idsCli);

		LoadingCore lc = new LoadingCore(m, linkIDPorts, lr);

		this.sp = panel;

		ImageManager.getInstance().removeAll();

		this.sp.setBackground(/* this.BACKGROUND */Color.pink);

		this.input = new Input(this.sp.getWinData());

		this.loop = new LoadingLoop(this);
		this.loop.start();

		lc.start();
	}

	@Override
	public void stop() {
		this.sp.removeKeyboardListener(this.input.getKeyboardEventListener());
		this.sp.removeKeyboardListener(this.input.getKeyboardMirrorListener());
		this.sp.removeMouseListener(this.input.getMouseEventListener());
		this.sp.removeMouseListener(this.input.getMouseMirrorListener());
		this.sp = null;
		this.input = null;
		this.loop.stop();
		this.loop = null;
	}

	@Override
	public void update() {
	}
}
