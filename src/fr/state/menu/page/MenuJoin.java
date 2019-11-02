package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.serverlink.data.ServerData;
import fr.serverlink.data.ServerDelays;
import fr.serverlink.hub.HubJoiner;
import fr.serverlink.link.Searcher;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WSwitch;
import fr.state.menu.widget.data.BorderData;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuJoin implements MenuPage {

	private class PlayerData {
		public int id;
		public String username;
		public Color color;

		public PlayerData(int id, String username, Color color) {
			super();
			this.id = id;
			this.username = username;
			this.color = color;
		}
	}

	private static final String[] RES_NAMES1 = {
			"title",
			"backStd",
			"backPressed",
			"notReadyPressed",
			"notReadyStd",
			"eadyPressed",
			"readyStd" };
	private static final String[] RES_PATHS1 = {
			"title",
			"backStd",
			"backPressed",
			"notReadyPressed",
			"notReadyStd",
			"readyPressed",
			"readyStd" };
	private static final String RES_FOLDER1 = "/resources/menu/menuJoin/";

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

	private static final String PAGE_NAME = "menuJoin";
	private static final String NAME2 = "lobby";

	private static final Color READY = Color.GREEN;
	private static final Color NOT_READY = Color.RED;

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

	private static final int[][] PADS_POS = { { 604, 340 }, { 900, 340 }, { 604, 540 }, { 900, 540 } };

	private List<Widget> widgets;

	private Menu m;

	private Object profileConf, serverConf;

	private XMLManager manager;

	private WElement[] pads;

	private WElement[] ready;

	private PlayerData[] players;

	private int maxPlayer = 4;

	private int idServer;

	private WSwitch wReady;

	private HubJoiner hub;

	private Searcher searcher;

	private Color defaultPadColor = new Color(0, 0, 0, 0);

	private boolean loaded;

	public MenuJoin(Menu m) {
		this.loaded = false;
		this.m = m;
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
	}

	public int getEmptyPad() {
		for (int i = 0; i < this.players.length; i++) {
			if (this.players[i] == null)
				return i;
		}
		return -1;
	}

	public int getPlayerPad(int playerId) {
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

				MenuJoin.this.wReady = MenuJoin.this.wReady();
				MenuJoin.this.wTitle();
				MenuJoin.this.wBack();
				MenuJoin.this.wServerSettings();
				MenuJoin.this.wRefresh();

				MenuJoin.this.pads = new WElement[MenuJoin.this.maxPlayer];
				MenuJoin.this.ready = new WElement[MenuJoin.this.maxPlayer];
				MenuJoin.this.players = new PlayerData[MenuJoin.this.maxPlayer];

				int size = 300;

				for (int i = 0; i < MenuJoin.this.maxPlayer; i++) {
					MenuJoin.this.pads[i] = MenuJoin.this.wPad(new Point(300 + size * i, 450));
					MenuJoin.this.ready[i] = MenuJoin.this.wReady(new Point(410 + size * i, 720));
				}

				for (int i = 0; i < MenuJoin.this.maxPlayer; i++) {
					MenuJoin.this.players[i] = null;
				}

				MenuJoin.this.putPlayerOnPad(new PlayerData(-1, myUsername, myColor), 0);

				MenuJoin.this.start();

				MenuJoin.this.loaded = true;
			}
		}).start();
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES1, RES_PATHS1);
		il.load(RES_NAMES2, RES_PATHS2);
	}

	public void putPlayerOnPad(PlayerData p, int padId) {
		if (padId == -1)
			return;

		DERectangle r = (DERectangle) this.pads[padId].getDrawElement();

		r.setColor(p.color);

		TextData td = r.getLabel().clone();
		td.setText(p.username);

		r.setLabel(td);

		this.players[padId] = p;

		((DERectangle) this.ready[padId].getDrawElement()).setColor(NOT_READY);
	}

	public void removePlayerFromPad(int padId) {
		if (padId < 0)
			return;

		DERectangle r = (DERectangle) this.pads[padId].getDrawElement();

		r.setColor(this.defaultPadColor);

		TextData td = r.getLabel().clone();
		td.setText("");

		r.setLabel(td);

		((DERectangle) this.ready[padId].getDrawElement()).setColor(new Color(0, 0, 0, 0));

		this.players[padId] = null;
	}

	private void resetPads() {
		for (int i = 1; i < this.maxPlayer; i++) {
			this.removePlayerFromPad(i);
		}
	}

	private void setPlayerReady(int playerPad, boolean ready) {
		if (playerPad == -1)
			return;

		((DERectangle) this.ready[playerPad].getDrawElement()).setColor(ready ? READY : NOT_READY);

		if (playerPad == 0 && ready == false && this.wReady.isActive()) {
			this.wReady.setActive(false);
		}
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

	private void start() {
		if (this.hub != null) {
			MenuJoin.this.hub.stop();
		}
		MenuJoin.this.hub = new HubJoiner(this.players[0].username, this.players[0].color,
				ServerData.getGroup(MenuJoin.this.idServer), ServerData.getPort(MenuJoin.this.idServer)) {

			@Override
			public void idAssigned(int id) {
				MenuJoin.this.searcher.stop();
				MenuJoin.this.searcher = null;
				MenuJoin.this.players[0].id = id;
			}

			@Override
			public void noMorePlayer() {
				this.stop();
				MenuJoin.this.resetPads();
				this.start();
			}

			@Override
			public void playerAdded(int id, String username, Color color) {
				int i = MenuJoin.this.getEmptyPad();

				if (i == -1)
					return;

				MenuJoin.this.putPlayerOnPad(new PlayerData(id, username, color), i);

				MenuJoin.this.updatePads();
			}

			@Override
			public void playerRemoved(int id) {
				MenuJoin.this.removePlayerFromPad(MenuJoin.this.getPlayerPad(id));
				MenuJoin.this.updatePads();
			}

			@Override
			public void readyChanged(int id, boolean ready) {
				MenuJoin.this.setPlayerReady(MenuJoin.this.getPlayerPad(id), ready);
			}
		};

		if (this.searcher != null) {
			MenuJoin.this.searcher.stop();
		}
		MenuJoin.this.searcher = new Searcher(MenuJoin.this.hub, ServerData.getGroup(MenuJoin.this.idServer),
				ServerData.getPort(MenuJoin.this.idServer));

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

		MenuJoin.this.setPlayerReady(0, false);
		this.players[0].id = -1;
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
				MenuJoin.this.stop();
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

	private WElement wPad(Point pos) {

		WElement w = new WElement(this);

		DERectangle de = new DERectangle();

		de.setBorder(new BorderData(5, Color.BLACK, 1));
		de.setLabel(new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 30), "", Color.black, 3));
		de.setColor(this.defaultPadColor);
		de.setSize(new Point(250, 250));

		w.setDrawElement(de);
		w.setPos(pos.clone());

		this.widgets.add(w);

		return w;
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

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 30), "Not ready",
				Color.black, 3);

		DERectangle r = new DERectangle();
		r.setSize(new Point(250, 100));
		r.setColor(new Color(200, 0, 0));
		r.setLabel(td.clone());
		r.setBorder(new BorderData(3, Color.black, 1));

		w.setOffDrawElement(r.clone());

		r.setColor(new Color(255, 0, 0));
		w.setPressedOffDrawElement(r.clone());

		td.setText("Ready");
		r.setLabel(td.clone());
		r.setColor(new Color(0, 200, 0));
		w.setOnDrawElement(r.clone());

		r.setColor(new Color(0, 255, 0));
		w.setPressedOnDrawElement(r.clone());

		w.setPos(new Point(400, 850));
		w.setHitboxFromDrawElement();

		this.widgets.add(w);

		return w;
	}

	private WElement wReady(Point pos) {

		WElement w = new WElement(this);

		DERectangle de = new DERectangle();

		de.setBorder(new BorderData(3, Color.BLACK, 1));
		de.setColor(this.defaultPadColor);
		de.setSize(new Point(30, 30));

		w.setDrawElement(de);
		w.setPos(pos.clone());

		this.widgets.add(w);

		return w;
	}

	public void wRefresh() {
		WButton w = new WButton(this) {
			@Override
			public void action() {
				MenuJoin.this.stop();
				MenuJoin.this.resetPads();
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
				MenuJoin.this.stop();
				MenuJoin.this.m.applyPage(new MenuServerSettings(MenuJoin.this.m, 1));
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
}
