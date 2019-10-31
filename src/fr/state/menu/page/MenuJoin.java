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

	private static final String[] RES_NAMES = { "title", "backStd", "backPressed" };

	private static final String[] RES_PATHS = { "title", "backStd", "backPressed" };

	private static final String RES_FOLDER = "/resources/menu/menuJoin/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuJoin";

	private static final Color READY = Color.GREEN;
	private static final Color NOT_READY = Color.RED;

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

		il.load(RES_NAMES, RES_PATHS);
	}

	public void putPlayerOnPad(PlayerData p, int padId) {
		if (padId == -1)
			return;

		if (this.wReady.isEnabled() == false) {
			this.wReady.setEnabled(true);
		}

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

		this.players[padId] = null;

		((DERectangle) this.ready[padId].getDrawElement()).setColor(new Color(0, 0, 0, 0));
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

	private void start() {
		if (this.hub != null) {
			MenuJoin.this.hub.stop();
		}
		MenuJoin.this.hub = new HubJoiner(this.players[0].username, this.players[0].color,
				ServerData.getGroup(MenuJoin.this.idServer), ServerData.getPort(MenuJoin.this.idServer)) {

			@Override
			public void idAssigned(int id) {
				MenuJoin.this.searcher.stop();
				MenuJoin.this.players[0].id = id;
			}

			@Override
			public void noMorePlayer() {
				MenuJoin.this.wReady.setEnabled(false);
				MenuJoin.this.resetPads();
				MenuJoin.this.setPlayerReady(0, false);
				MenuJoin.this.searcher.stop();
				MenuJoin.this.hub.stop();
				MenuJoin.this.players[0].id = -1;
				MenuJoin.this.hub.start();
				MenuJoin.this.searcher.start();
			}

			@Override
			public void playerAdded(int id, String username, Color color) {
				int i = MenuJoin.this.getEmptyPad();

				if (i == -1)
					return;

				MenuJoin.this.putPlayerOnPad(new PlayerData(id, username, color), i);
			}

			@Override
			public void playerRemoved(int id) {
				MenuJoin.this.removePlayerFromPad(MenuJoin.this.getPlayerPad(id));
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
		w.setEnabled(false);
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

				MenuJoin.this.start();
			}
		};

		DERectangle r = new DERectangle();
		r.setSize(new Point(100, 100));
		r.setLabel(new TextData(new Point(), new Font("Arial", Font.BOLD, 25), "Refresh", Color.BLACK, 3));
		r.setBorder(new BorderData(3, Color.black, 2));
		r.setColor(Color.GRAY);
		w.setStdDrawElement(r);

		r.setColor(Color.LIGHT_GRAY);
		w.setPressedDrawElement(r);

		w.setPos(new Point(1100, 850));
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

		DERectangle r = new DERectangle();
		r.setSize(new Point(200, 100));
		r.setLabel(new TextData(new Point(), new Font("Arial", Font.BOLD, 30), "Settings", Color.BLACK, 3));
		r.setBorder(new BorderData(3, Color.black, 2));
		r.setColor(Color.GRAY);
		w.setStdDrawElement(r);

		r.setColor(Color.LIGHT_GRAY);
		w.setPressedDrawElement(r);

		w.setPos(new Point(800, 850));
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
