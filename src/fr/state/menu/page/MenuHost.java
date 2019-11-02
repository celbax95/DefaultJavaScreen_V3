package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.serverlink.data.ServerData;
import fr.serverlink.hub.HubHoster;
import fr.serverlink.link.Linker;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WSwitch;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuHost implements MenuPage {

	private class PlayerData {
		public int id;
		public String username;
		public Color color;
		public boolean ready;

		public PlayerData(int id, String username, Color color, boolean ready) {
			super();
			this.id = id;
			this.username = username;
			this.color = color;
			this.ready = ready;
		}
	}

	private static final String[] RES_NAMES1 = {
			"title",
			"backStd",
			"backPressed",
			"cancelStd",
			"cancelPressed",
			"playBlocked",
			"playStd",
			"playPressed" };
	private static final String[] RES_PATHS1 = {
			"title",
			"backStd",
			"backPressed",
			"cancelStd",
			"cancelPressed",
			"playBlocked",
			"playStd",
			"playPressed" };
	private static final String RES_FOLDER1 = "/resources/menu/menuHost/";

	private static final String[] RES_NAMES2 = {
			"notReadyCell",
			"readyCell",
			"playerPadEmpty",
			"refreshStd",
			"refreshPressed",
			"serverSettingsStd",
			"serverSettingsPressed",
			"playerPadEmpty" };
	private static final String[] RES_PATHS2 = {
			"notReadyCell",
			"readyCell",
			"playerPadEmpty",
			"refreshStd",
			"refreshPressed",
			"serverSettingsStd",
			"serverSettingsPressed",
			"playerPadEmpty" };
	private static final String RES_FOLDER2 = "/resources/menu/lobby/";

	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuHost";
	private static final String NAME2 = "lobby";

	private static final String PARAM_NAME_USERNAME = "username";

	private static final String PARAM_NAME_COLOR = "color";
	static {
		for (int i = 0; i < RES_NAMES1.length; i++) {
			RES_NAMES1[i] = PAGE_NAME + "/" + RES_NAMES1[i];
		}

		for (int i = 0; i < RES_PATHS1.length; i++) {
			RES_PATHS1[i] = RES_FOLDER1 + RES_PATHS1[i] + RES_EXTENSION;
		}

		for (int i = 0; i < RES_NAMES2.length; i++) {
			RES_NAMES2[i] = NAME2 + "/" + RES_NAMES2[i];
		}

		for (int i = 0; i < RES_PATHS2.length; i++) {
			RES_PATHS2[i] = RES_FOLDER2 + RES_PATHS2[i] + RES_EXTENSION;
		}
	}

	private static final int ID_HOST = 0;
	private static final int TIME_TO_PLAY = 3000;

	private static final Point[] PADS_POS = {
			new Point(604, 340),
			new Point(1050, 340),
			new Point(604, 600),
			new Point(1050, 600) };

	private boolean loaded;

	private WSwitch wPlay;

	private List<Widget> widgets;

	private Menu m;

	private Object profileConf, serverConf;

	private XMLManager manager;

	private WElement[] pads;

	private WElement[] ready;

	private PlayerData[] players;

	private int maxPlayer = 4;

	private int idServer;

	private HubHoster hub;

	private Linker linker;

	private Thread playCountdown;

	private WElement wWaiting;

	public MenuHost(Menu m) {
		this.loaded = false;
		this.m = m;
	}

	private void changeWPlayState() {
		List<Integer> pads = new ArrayList<>(); // pad occupe
		int ready = 0;
		for (int i = 0; i < this.maxPlayer; i++) {
			if (this.players[i] != null) {
				pads.add(i);
				if (this.players[i].ready) {
					ready++;
				}
			}
		}

		if (pads.size() >= 2 && pads.size() == ready) {
			this.wPlay.setVisible(true);
			this.wWaiting.setVisible(false);
		} else {
			this.wPlay.setActive(false);
			this.wPlay.setVisible(false);
			this.wWaiting.setVisible(true);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
	}

	private int getEmptyPad() {
		for (int i = 0; i < this.players.length; i++) {
			if (this.players[i] == null)
				return i;
		}
		return -1;
	}

	private int getPlayerPad(int playerId) {
		for (int i = 0; i < this.players.length; i++) {
			if (this.players[i] != null && this.players[i].id == playerId)
				return i;
		}
		return -1;
	}

	@Override
	public boolean isLoaded() {
		return this.loaded;
	}

	@Override
	public void load() {
		new Thread(new Runnable() {
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

				MenuHost.this.wTitle();
				MenuHost.this.wBack();
				MenuHost.this.wWaiting = MenuHost.this.wWaiting();
				MenuHost.this.wServerSettings();
				MenuHost.this.wRefresh();

				MenuHost.this.wPlay = MenuHost.this.wPlay();

				MenuHost.this.pads = new WElement[MenuHost.this.maxPlayer];
				MenuHost.this.ready = new WElement[MenuHost.this.maxPlayer];
				MenuHost.this.players = new PlayerData[MenuHost.this.maxPlayer];

				for (int i = 0; i < MenuHost.this.maxPlayer; i++) {
					MenuHost.this.wPadFrame(PADS_POS[i]);
					MenuHost.this.pads[i] = MenuHost.this.wPad(PADS_POS[i].clone().add(new Point(58, 3)));
					MenuHost.this.ready[i] = MenuHost.this.wReady(PADS_POS[i].clone().add(new Point(180, 124)));
				}

				for (int i = 0; i < MenuHost.this.maxPlayer; i++) {
					MenuHost.this.players[i] = null;
				}

				MenuHost.this.putPlayerOnPad(new PlayerData(ID_HOST, myUsername, myColor, true), 0);
				MenuHost.this.setPlayerReady(0, true);

				MenuHost.this.changeWPlayState();

				MenuHost.this.start();

				MenuHost.this.loaded = true;
			}
		}).start();
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		for (int i = 0; i < RES_NAMES1.length; i++) {
			System.out.println(RES_NAMES1[i] + " : " + RES_PATHS1[i]);
		}

		il.load(RES_NAMES1, RES_PATHS1);
		il.load(RES_NAMES2, RES_PATHS2);
	}

	private void putPlayerOnPad(PlayerData p, int padId) {
		DERectangle r = (DERectangle) this.pads[padId].getDrawElement();

		r.setColor(p.color);

		TextData td = r.getLabel().clone();
		td.setText(p.username);

		r.setLabel(td);

		this.pads[padId].setVisible(true);

		this.players[padId] = p;

		this.setPlayerReady(padId, p.ready);

		this.changeWPlayState();
	}

	private void removePlayerFromPad(int padId) {
		if (padId < 0)
			return;

		this.pads[padId].setVisible(false);

		this.setPlayerReady(padId, false);

		this.players[padId] = null;

		this.changeWPlayState();
	}

	private void resetPads() {
		for (int i = 1; i < this.maxPlayer; i++) {
			this.removePlayerFromPad(i);
		}
	}

	private void setPlayerReady(int playerPad, boolean ready) {
		if (playerPad == -1)
			return;

		ImageManager im = ImageManager.getInstance();

		if (this.players[playerPad] == null) {
			this.ready[playerPad].setVisible(false);
		} else {
			this.players[playerPad].ready = ready;

			this.ready[playerPad].setVisible(true);
			DEImage i = (DEImage) this.ready[playerPad].getDrawElement().clone();

			i.setImage(ready ? im.get("lobby/readyCell") : im.get("lobby/notReadyCell"));

			this.ready[playerPad].setDrawElement(i);

		}
		this.changeWPlayState();
	}

	private boolean stackPlayers() {
		boolean r = false;

		for (int i = 1, size = this.players.length - 1; i < size; i++) {
			if (this.players[i] == null && this.players[i + 1] != null) {
				this.players[i] = this.players[i + 1];
				this.players[i + 1] = null;
				r = true;
			}
		}

		return r;
	}

	public void start() {

		// hub
		if (this.hub != null) {
			MenuHost.this.hub.stop();
		}
		MenuHost.this.hub = new HubHoster(ID_HOST, this.players[0].username, this.players[0].color,
				MenuHost.this.maxPlayer, ServerData.getGroup(MenuHost.this.idServer),
				ServerData.getPort(MenuHost.this.idServer)) {

			@Override
			public void gameStarting(boolean state) {

			}

			@Override
			public void noMorePlayer() {
				MenuHost.this.resetPads();
			}

			@Override
			public void playerAdded(int id, String username, Color color) {
				int i = MenuHost.this.getEmptyPad();

				if (i == -1)
					return;

				MenuHost.this.putPlayerOnPad(new PlayerData(id, username, color, false), i);
				MenuHost.this.updatePads();
			}

			@Override
			public void playerRemoved(int id) {
				MenuHost.this.removePlayerFromPad(MenuHost.this.getPlayerPad(id));
				MenuHost.this.updatePads();
			}

			@Override
			public void readyChanged(int id, boolean ready) {
				MenuHost.this.setPlayerReady(MenuHost.this.getPlayerPad(id), ready);
			}
		};

		// Linker
		if (this.linker != null) {
			MenuHost.this.linker.stop();
		}
		MenuHost.this.linker = new Linker(ID_HOST, ServerData.getGroup(MenuHost.this.idServer),
				ServerData.getPort(MenuHost.this.idServer));

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

	private void updatePads() {
		if (this.stackPlayers() == false)
			return;

		for (int i = 1; i < this.players.length; i++) {
			if (this.players[i] == null) {
				this.removePlayerFromPad(i);
			} else {
				this.putPlayerOnPad(this.players[i], i);
			}
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

	private WElement wPad(Point pos) {

		WElement w = new WElement(this);

		DERectangle de = new DERectangle();

		de.setLabel(new TextData(new Point(new Point(0, 178)), new Font("Copperplate Gothic Bold", Font.PLAIN, 30), "",
				Color.black, 1));
		de.setColor(Color.black);
		de.setSize(new Point(143, 143));

		w.setDrawElement(de);
		w.setPos(pos.clone());

		w.setVisible(false);

		this.widgets.add(w);

		return w;
	}

	private WElement wPadFrame(Point pos) {

		ImageManager im = ImageManager.getInstance();

		WElement w = new WElement(this);

		DEImage i = new DEImage();

		i.setImage(im.get("lobby/playerPadEmpty"));

		w.setDrawElement(i);
		w.setPos(pos.clone());

		this.widgets.add(w);

		return w;
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

						// TODO
						System.out.println("LET'S PLAY !");
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

	private WElement wReady(Point pos) {

		ImageManager im = ImageManager.getInstance();

		WElement w = new WElement(this);

		DEImage i = new DEImage();

		i.setImage(im.get("lobby/notReadyCell"));

		w.setDrawElement(i);
		w.setPos(pos.clone());
		w.setVisible(false);

		this.widgets.add(w);

		return w;
	}

	public void wRefresh() {
		WButton w = new WButton(this) {
			@Override
			public void action() {
				MenuHost.this.stop();

				MenuHost.this.resetPads();

				MenuHost.this.start();
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get("lobby/refreshStd"));

		w.setStdDrawElement(i.clone());

		i.setImage(im.get("lobby/refreshPressed"));
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
		i.setImage(im.get("lobby/serverSettingsStd"));

		w.setStdDrawElement(i.clone());

		i.setImage(im.get("lobby/serverSettingsPressed"));
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
