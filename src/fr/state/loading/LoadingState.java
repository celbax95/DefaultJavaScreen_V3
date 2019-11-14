package fr.state.loading;

import java.awt.Color;
import java.awt.Graphics2D;
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

public class LoadingState implements IAppState {

	private StatePanel sp;

	private Input input;

	private LoadingLoop loop;

	private final int GRAY = 40;

	private Map<String, Object> initData;

	private final Color BACKGROUND = new Color(this.GRAY, this.GRAY, this.GRAY);

	private LoadingTemplate template;

	private LoadingCore loadingCore;

	public LoadingState() {
		this.initData = null;
	}

	@Override
	public void draw(Graphics2D g) {
		if (this.template != null) {
			this.template.draw(g);
		}
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

		if (this.initData == null) {
			Logger.err("Les donnees d'initialisation n'ont pas ete affectees");
			System.exit(0);
		}

		@SuppressWarnings("unchecked")
		List<Integer> ids = (List<Integer>) this.initData.get("ids");
		@SuppressWarnings("unchecked")
		List<Integer> ports = (List<Integer>) this.initData.get("ports");
		int myId = (int) this.initData.get("myId");

		DatafilesManager dfm = DatafilesManager.getInstance();

		Object serverConf = dfm.getFile("serverConf");
		// Object profileConf = dfm.getFile("profile");

		XMLManager xml = dfm.getXmlManager();

		int serverID = (int) xml.getParam(serverConf, "id", 0);

		String groupIP = GlobalServerData.getGroup(serverID);

		LoadingRequestor lr = null;
		if (ports != null) {
			lr = new LoadingRequestor(groupIP, ports);
		}

		Multiplayer m = new Multiplayer(groupIP, GlobalServerData.getP2PPort(serverID), ids);

		this.template = new LoadingTemplate(panel.getWinData(), 1);

		this.loadingCore = new LoadingCore(this, m, myId, ids, lr, this.template);

		this.sp = panel;

		this.sp.setBackground(this.BACKGROUND);

		this.input = new Input(this.sp.getWinData());

		this.loop = new LoadingLoop(this);
		this.loop.start();

		this.loadingCore.start();
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
		this.loadingCore.stop();
		this.loadingCore.closeSockets();
		this.loadingCore = null;
	}

	@Override
	public void update() {
		this.loadingCore.update();
		this.template.update();
	}
}
