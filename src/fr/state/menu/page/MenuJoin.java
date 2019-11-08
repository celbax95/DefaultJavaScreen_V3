package fr.state.menu.page;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.server.GlobalServerData;
import fr.server.serverlink.data.HubPlayerData;
import fr.server.serverlink.data.ServerDelays;
import fr.server.serverlink.hub.HubJoiner;
import fr.server.serverlink.link.Searcher;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WSwitch;
import fr.state.menu.widget.drawelements.DEImage;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;
import fr.util.point.Point;

public class MenuJoin implements MenuPage {

	private static final String[] RES_NAMES = {
			"title",
			"backStd",
			"backPressed",
			"notReadyPressed",
			"notReadyStd",
			"readyPressed",
			"readyStd" };
	private static final String[] RES_PATHS = {
			"title",
			"backStd",
			"backPressed",
			"notReadyPressed",
			"notReadyStd",
			"readyPressed",
			"readyStd" };
	private static final String RES_FOLDER = "/resources/menu/menuJoin/";

	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuJoin";

	private static final String PARAM_NAME_USERNAME = "username";
	private static final String PARAM_NAME_COLOR = "color";

	/**
	 * PADS_POS.length = nombre max de joueurs
	 */
	private static final Point[] PADS_POS = {
			new Point(604, 340),
			new Point(1056, 340),
			new Point(604, 684),
			new Point(1056, 684) };

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private List<Widget> widgets;

	private Lobby lobby;

	private Menu m;

	private Object profileConf, serverConf;

	private XMLManager manager;

	private int idServer;

	private WSwitch wReady;

	private HubJoiner hub;

	private Searcher searcher;

	private boolean loaded;

	private boolean gameStart;

	public MenuJoin(Menu m) {
		this.loaded = false;
		this.m = m;
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
		this.lobby.draw(g);
	}

	@Override
	public boolean isLoaded() {
		return this.loaded;
	}

	@Override
	public void load() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				MenuJoin.this.widgets = new Vector<>();

				MenuJoin.this.loadResources();

				DatafilesManager dfm = DatafilesManager.getInstance();
				MenuJoin.this.profileConf = dfm.getFile("profile");
				MenuJoin.this.serverConf = dfm.getFile("serverConf");
				MenuJoin.this.manager = dfm.getXmlManager();

				MenuJoin.this.idServer = (int) MenuJoin.this.manager.getParam(MenuJoin.this.serverConf, "id", 0);

				String myUsername = (String) MenuJoin.this.manager.getParam(MenuJoin.this.profileConf,
						PARAM_NAME_USERNAME, "user");
				Color myColor = Color.decode((String) MenuJoin.this.manager.getParam(MenuJoin.this.profileConf,
						PARAM_NAME_COLOR, "#000000"));

				MenuJoin.this.lobby = new Lobby(MenuJoin.this, PADS_POS);

				MenuJoin.this.gameStart = false;

				MenuJoin.this.wReady = MenuJoin.this.wReady();
				MenuJoin.this.wTitle();
				MenuJoin.this.wBack();
				MenuJoin.this.wServerSettings();
				MenuJoin.this.wRefresh();

				MenuJoin.this.lobby.setMainPlayer(new HubPlayerData(-1, myUsername, myColor, false));

				MenuJoin.this.start();

				MenuJoin.this.loaded = true;
			}
		});
		thread.setName("MenuJoin/load");
		thread.start();
	}

	private void loadingState() {
		Map<String, Object> initData = new HashMap<>();

		List<Integer> ids = MenuJoin.this.lobby.getPlayersId();

		initData.put("ids", ids);
		initData.put("myId", MenuJoin.this.lobby.getMainPlayer().getId());

		StatePanel sp = MenuJoin.this.m.getMenuState().getStatePanel();

		IAppState nextState = sp.getAppStateManager().getState("loading");
		nextState.setInitData(initData);

		MenuJoin.this.stop();
		this.hub.closeSockets();
		this.searcher.closeSockets();

		sp.setState(nextState);
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
	}

	private void start() {
		if (this.hub != null) {
			MenuJoin.this.hub.stop();
			this.hub.closeSockets();
		}

		HubPlayerData p = this.lobby.getMainPlayer();

		if (p == null)
			return;

		MenuJoin.this.hub = new HubJoiner(p.getUsername(), p.getColor(),
				GlobalServerData.getGroup(MenuJoin.this.idServer),
				GlobalServerData.getHubPort(MenuJoin.this.idServer)) {

			@Override
			public void gameStarted() {
				MenuJoin.this.gameStart = true;
			}

			@Override
			public void idAssigned(int id) {
				MenuJoin.this.wReady.setActive(false);
				MenuJoin.this.wReady.setVisible(true);
				MenuJoin.this.searcher.stop();
				MenuJoin.this.searcher = null;
				HubPlayerData p = MenuJoin.this.lobby.getMainPlayer();
				p.setId(id);
				MenuJoin.this.lobby.setMainPlayer(p);
			}

			@Override
			public void noMorePlayer() {
				MenuJoin.this.wReady.setActive(false);
				MenuJoin.this.wReady.setVisible(false);
				MenuJoin.this.stop();
				MenuJoin.this.lobby.removeAllPlayers();
				MenuJoin.this.start();
			}

			@Override
			public void playerAdded(int id, String username, Color color) {
				MenuJoin.this.lobby.addPlayer(new HubPlayerData(id, username, color, false));
			}

			@Override
			public void playerRemoved(int id) {
				if (MenuJoin.this.lobby.getMainPlayer().getId() == id)
					return;

				MenuJoin.this.lobby.removePlayer(id);
			}

			@Override
			public void readyChanged(int id, boolean ready) {
				MenuJoin.this.lobby.setPlayerReady(id, ready);
			}
		};

		if (this.searcher != null) {
			MenuJoin.this.searcher.stop();
			this.searcher.closeSockets();
		}
		MenuJoin.this.searcher = new Searcher(MenuJoin.this.hub, GlobalServerData.getGroup(MenuJoin.this.idServer),
				GlobalServerData.getHubPort(MenuJoin.this.idServer));

		MenuJoin.this.hub.start();
		MenuJoin.this.searcher.start();
	}

	private void stop() {
		if (this.searcher != null) {
			MenuJoin.this.searcher.stop();
		}

		if (this.hub != null) {
			MenuJoin.this.hub.stop();
		}

		HubPlayerData p = this.lobby.getMainPlayer();
		this.lobby.setMainPlayer(new HubPlayerData(-1, p.getUsername(), p.getColor(), false));
	}

	@Override
	public void update(Input input) {
		for (Widget w : this.widgets) {
			w.update(input);
		}
		if (this.gameStart) {
			this.loadingState();
		}
	}

	private void wBack() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				MenuJoin.this.stop();
				MenuJoin.this.hub.closeSockets();
				MenuJoin.this.searcher.closeSockets();
				MenuJoin.this.m.applyPage(new MenuMain(MenuJoin.this.m));
			}
		};

		btn.setPos(new Point(42, 42));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private WSwitch wReady() {
		WSwitch w = new WSwitch(this) {
			@Override
			public void actionOff() {
				MenuJoin.this.hub.setReady(false);
			}

			@Override
			public void actionOn() {
				MenuJoin.this.hub.setReady(true);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/notReadyStd"));
		w.setOffDrawElement(i);

		i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/notReadyPressed"));
		w.setPressedOffDrawElement(i);

		i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/readyStd"));
		w.setOnDrawElement(i);

		i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/readyPressed"));
		w.setPressedOnDrawElement(i);

		w.setPos(new Point(1499, 548));
		w.setHitboxFromDrawElement();

		this.widgets.add(w);

		return w;
	}

	public void wRefresh() {
		WButton w = new WButton(this) {
			@Override
			public void action() {
				MenuJoin.this.stop();
				MenuJoin.this.lobby.removeAllPlayers();

				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(ServerDelays.UPDATE_TEST_RATE);
						} catch (InterruptedException e) {
						}
						MenuJoin.this.start();
					}
				}).start();
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get(Lobby.getPageName() + "/refreshStd"));

		w.setStdDrawElement(i.clone());

		i.setImage(im.get(Lobby.getPageName() + "/refreshPressed"));
		w.setPressedDrawElement(i);

		w.setPos(new Point(168, 958));
		w.setHitboxFromDrawElement();

		this.widgets.add(w);
	}

	private void wServerSettings() {
		WButton w = new WButton(this) {
			@Override
			public void action() {
				MenuJoin.this.stop();
				MenuJoin.this.hub.closeSockets();
				MenuJoin.this.searcher.closeSockets();
				MenuJoin.this.m.applyPage(new MenuServerSettings(MenuJoin.this.m, 1));
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get(Lobby.getPageName() + "/serverSettingsStd"));

		w.setStdDrawElement(i.clone());

		i.setImage(im.get(Lobby.getPageName() + "/serverSettingsPressed"));
		w.setPressedDrawElement(i);

		w.setPos(new Point(42, 938));
		w.setHitboxFromDrawElement();

		this.widgets.add(w);
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(550, 76));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/title"));

		title.setDrawElement(i);

		this.widgets.add(title);
	}
}
