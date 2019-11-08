package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
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
import fr.server.serverlink.hub.HubHoster;
import fr.server.serverlink.link.Linker;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WSwitch;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;
import fr.util.point.Point;

public class MenuHost implements MenuPage {

	private static final String[] RES_NAMES = {
			"title",
			"backStd",
			"backPressed",
			"cancelStd",
			"cancelPressed",
			"playBlocked",
			"playStd",
			"playPressed" };
	private static final String[] RES_PATHS = {
			"title",
			"backStd",
			"backPressed",
			"cancelStd",
			"cancelPressed",
			"playBlocked",
			"playStd",
			"playPressed" };
	private static final String RES_FOLDER = "/resources/menu/menuHost/";

	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menu/host";

	private static final String PARAM_NAME_USERNAME = "username";

	private static final String PARAM_NAME_COLOR = "color";
	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private static final int ID_HOST = 0;
	private static final int TIME_TO_PLAY = 3000;

	private static final Point[] PADS_POS = {
			new Point(604, 340),
			new Point(1056, 340),
			new Point(604, 684),
			new Point(1056, 684) };

	private boolean loaded;

	private WSwitch wPlay;

	private List<Widget> widgets;

	private Menu m;

	private Object profileConf, serverConf;

	private XMLManager manager;

	private int idServer;

	private HubHoster hub;

	private Linker linker;

	private Thread playCountdown;

	private WElement wWaiting;

	private Lobby lobby;

	public MenuHost(Menu m) {
		this.loaded = false;
		this.m = m;
	}

	private void changeWPlayState() {
		int players = this.lobby.getNbPlayer();
		int readys = this.lobby.getNbReady();
		boolean playEnabled = players >= 2 && players == readys;

		this.wPlay.setActive(false);

		this.wPlay.setVisible(playEnabled);
		this.wWaiting.setVisible(!playEnabled);
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
				MenuHost.this.widgets = new Vector<>();

				MenuHost.this.loadResources();

				DatafilesManager dfm = DatafilesManager.getInstance();
				MenuHost.this.profileConf = dfm.getFile("profile");
				MenuHost.this.serverConf = dfm.getFile("serverConf");
				MenuHost.this.manager = dfm.getXmlManager();

				MenuHost.this.idServer = (int) MenuHost.this.manager.getParam(MenuHost.this.serverConf, "id", 0);

				String myUsername = (String) MenuHost.this.manager.getParam(MenuHost.this.profileConf,
						PARAM_NAME_USERNAME, "user");
				Color myColor = Color.decode((String) MenuHost.this.manager.getParam(MenuHost.this.profileConf,
						PARAM_NAME_COLOR, "#000000"));

				MenuHost.this.lobby = new Lobby(MenuHost.this, PADS_POS);

				MenuHost.this.wTitle();
				MenuHost.this.wBack();
				MenuHost.this.wWaiting = MenuHost.this.wWaiting();
				MenuHost.this.wServerSettings();
				MenuHost.this.wRefresh();

				MenuHost.this.wPlay = MenuHost.this.wPlay();

				MenuHost.this.lobby.setMainPlayer(new HubPlayerData(ID_HOST, myUsername, myColor, true));
				MenuHost.this.lobby.setPlayerReady(ID_HOST, true);

				MenuHost.this.changeWPlayState();

				MenuHost.this.start();

				MenuHost.this.loaded = true;
			}
		});
		thread.setName("MenuHost/load");
		thread.start();
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
	}

	public void start() {

		// hub
		if (this.hub != null) {
			MenuHost.this.hub.stop();
		}

		HubPlayerData p = this.lobby.getMainPlayer();

		if (p == null)
			return;

		MenuHost.this.hub = new HubHoster(ID_HOST, p.getUsername(), p.getColor(), PADS_POS.length,
				GlobalServerData.getGroup(MenuHost.this.idServer),
				GlobalServerData.getHubPort(MenuHost.this.idServer)) {

			@Override
			public void gameStarting(boolean state) {

			}

			@Override
			public void noMorePlayer() {
				MenuHost.this.lobby.removeAllPlayers();
			}

			@Override
			public void playerAdded(int id, String username, Color color) {
				MenuHost.this.lobby.addPlayer(new HubPlayerData(id, username, color, false));
				MenuHost.this.changeWPlayState();
			}

			@Override
			public void playerRemoved(int id) {
				MenuHost.this.lobby.removePlayer(id);
				MenuHost.this.changeWPlayState();
			}

			@Override
			public void readyChanged(int id, boolean ready) {
				MenuHost.this.lobby.setPlayerReady(id, ready);
				MenuHost.this.changeWPlayState();
			}
		};

		// Linker
		if (this.linker != null) {
			MenuHost.this.linker.stop();
		}
		MenuHost.this.linker = new Linker(ID_HOST, GlobalServerData.getGroup(MenuHost.this.idServer),
				GlobalServerData.getHubPort(MenuHost.this.idServer));

		if (this.playCountdown != null) {
			MenuHost.this.playCountdown.interrupt();
		}

		MenuHost.this.hub.start();
		MenuHost.this.linker.start();
	}

	private void stop() {
		if (this.hub != null) {
			MenuHost.this.hub.stop();
		}

		if (this.linker != null) {
			MenuHost.this.linker.stop();
		}

		if (this.playCountdown != null) {
			MenuHost.this.playCountdown.interrupt();
		}
	}

	@Override
	public void update(Input input) {
		for (Widget w : this.widgets) {
			w.update(input);
		}
	}

	private void wBack() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				MenuHost.this.stop();
				MenuHost.this.m.applyPage(new MenuMain(MenuHost.this.m));
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

	private WSwitch wPlay() {
		WSwitch w = new WSwitch(this) {
			@Override
			public void actionOff() {
				MenuHost.this.playCountdown.interrupt();
			}

			@Override
			public void actionOn() {
				(MenuHost.this.playCountdown = new Thread(new Runnable() {

					@Override
					public void run() {
						int countdown = TIME_TO_PLAY / 1000;

						for (int i = countdown; i >= 0; i--) {
							TextData td = ((DEImage) MenuHost.this.wPlay.getOnDrawElement()).getLabel().clone();
							td.setText(String.valueOf(i));
							((DEImage) MenuHost.this.wPlay.getOnDrawElement()).setLabel(td.clone());
							((DEImage) MenuHost.this.wPlay.getPressedOnDrawElement()).setLabel(td.clone());

							if (i > 0) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									Thread.currentThread().interrupt();
									return;
								}
							}
						}

						Map<String, Object> initData = new HashMap<>();

						List<Integer> ports = MenuHost.this.hub.getListeningPorts();
						List<Integer> ids = MenuHost.this.lobby.getPlayersId();

						initData.put("ports", ports);
						initData.put("ids", ids);
						initData.put("myId", MenuHost.this.lobby.getMainPlayer().getId());

						StatePanel sp = MenuHost.this.m.getMenuState().getStatePanel();

						IAppState nextState = sp.getAppStateManager().getState("loading");
						nextState.setInitData(initData);

						MenuHost.this.stop();

						sp.setState(nextState);
					}
				})).start();
			}
		};

		TextData td = new TextData(new Point(0, 22), new Font("Kristen ITC", Font.PLAIN, 35), "", Color.black, 1);

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/playStd"));
		w.setOffDrawElement(i);

		i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/playPressed"));
		w.setPressedOffDrawElement(i);

		i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/cancelStd"));
		i.setLabel(td);
		w.setOnDrawElement(i);

		i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/cancelPressed"));
		i.setLabel(td);
		w.setPressedOnDrawElement(i);

		w.setPos(new Point(1499, 548));
		w.setHitboxFromDrawElement();

		w.setActive(false);

		this.widgets.add(w);

		return w;
	}

	public void wRefresh() {
		WButton w = new WButton(this) {
			@Override
			public void action() {
				MenuHost.this.stop();

				MenuHost.this.lobby.removeAllPlayers();

				MenuHost.this.start();
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
				MenuHost.this.stop();
				MenuHost.this.m.applyPage(new MenuServerSettings(MenuHost.this.m, 0));
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

	private WElement wWaiting() {
		WElement w = new WElement(this);

		w.setPos(new Point(1499, 548));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/playBlocked"));

		w.setDrawElement(i);

		this.widgets.add(w);

		return w;
	}

}
