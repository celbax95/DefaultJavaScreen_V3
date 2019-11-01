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
import fr.state.menu.widget.data.BorderData;
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

	private static final String[] RES_NAMES = { "title", "backStd", "backPressed" };

	private static final String[] RES_PATHS = { "title", "backStd", "backPressed" };

	private static final String RES_FOLDER = "/resources/menu/menuHost/";

	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuHost";

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

	private static final int ID_HOST = 0;
	private static final int TIME_TO_PLAY = 3000;

	private static final Color PLAY_ACTIVATED_COLOR = new Color(0, 180, 0);

	private static final Color PLAY_UNACTIVATED_COLOR = new Color(130, 60, 60);

	private boolean loaded;

	private WSwitch wPlay;

	private Color defaultPadColor = new Color(0, 0, 0, 0);

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
			this.wPlay.setEnabled(true);
			((DERectangle) this.wPlay.getOffDrawElement()).setColor(PLAY_ACTIVATED_COLOR);
			((DERectangle) this.wPlay.getOnDrawElement()).setColor(PLAY_ACTIVATED_COLOR);
			((DERectangle) this.wPlay.getPressedOnDrawElement()).setColor(PLAY_ACTIVATED_COLOR);
			((DERectangle) this.wPlay.getPressedOffDrawElement()).setColor(PLAY_ACTIVATED_COLOR);
		} else {
			this.wPlay.setEnabled(false);
			this.wPlay.setActive(false);
			((DERectangle) this.wPlay.getOffDrawElement()).setColor(PLAY_UNACTIVATED_COLOR);
			((DERectangle) this.wPlay.getOnDrawElement()).setColor(PLAY_UNACTIVATED_COLOR);
			((DERectangle) this.wPlay.getPressedOnDrawElement()).setColor(PLAY_UNACTIVATED_COLOR);
			((DERectangle) this.wPlay.getPressedOffDrawElement()).setColor(PLAY_UNACTIVATED_COLOR);
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
				MenuHost.this.wServerSettings();
				MenuHost.this.wRefresh();

				MenuHost.this.wPlay = MenuHost.this.wPlay();

				MenuHost.this.pads = new WElement[MenuHost.this.maxPlayer];
				MenuHost.this.ready = new WElement[MenuHost.this.maxPlayer];
				MenuHost.this.players = new PlayerData[MenuHost.this.maxPlayer];

				int size = 300;

				for (int i = 0; i < MenuHost.this.maxPlayer; i++) {
					MenuHost.this.pads[i] = MenuHost.this.wPad(new Point(300 + size * i, 450));
					MenuHost.this.ready[i] = MenuHost.this.wReady(new Point(410 + size * i, 720));
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

		il.load(RES_NAMES, RES_PATHS);
	}

	private void putPlayerOnPad(PlayerData p, int padId) {
		DERectangle r = (DERectangle) this.pads[padId].getDrawElement();

		r.setColor(p.color);

		TextData td = r.getLabel().clone();
		td.setText(p.username);

		r.setLabel(td);

		this.players[padId] = p;

		this.setPlayerReady(padId, p.ready);

		this.changeWPlayState();
	}

	private void removePlayerFromPad(int padId) {
		if (padId < 0)
			return;

		DERectangle r = (DERectangle) this.pads[padId].getDrawElement();

		r.setColor(this.defaultPadColor);

		TextData td = r.getLabel().clone();
		td.setText("");

		r.setLabel(td);

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

		if (this.players[playerPad] == null) {
			((DERectangle) this.ready[playerPad].getDrawElement()).setColor(new Color(0, 0, 0, 0));
		} else {
			this.players[playerPad].ready = ready;

			((DERectangle) this.ready[playerPad].getDrawElement()).setColor(ready ? READY : NOT_READY);
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

		de.setBorder(new BorderData(5, Color.BLACK, 1));
		de.setLabel(new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 30), "", Color.black, 3));
		de.setColor(this.defaultPadColor);
		de.setSize(new Point(250, 250));

		w.setDrawElement(de);
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
						String playMsg = "Starts in X";

						int countdown = TIME_TO_PLAY / 1000;

						for (int i = countdown; i > 0; i--) {
							TextData td = ((DERectangle) MenuHost.this.wPlay.getOnDrawElement()).getLabel().clone();
							td.setText(playMsg.replace("X", String.valueOf(i)));
							((DERectangle) MenuHost.this.wPlay.getOnDrawElement()).setLabel(td.clone());
							((DERectangle) MenuHost.this.wPlay.getPressedOnDrawElement()).setLabel(td.clone());
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								return;
							}
						}
						TextData td = ((DERectangle) MenuHost.this.wPlay.getOnDrawElement()).getLabel().clone();
						td.setText("Starting...");
						((DERectangle) MenuHost.this.wPlay.getOnDrawElement()).setLabel(td.clone());
						((DERectangle) MenuHost.this.wPlay.getPressedOnDrawElement()).setLabel(td.clone());

						// TODO
						System.out.println("LET'S PLAY !");
					}
				})).start();
			}
		};

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 30), "PLAY",
				Color.black, 3);

		DERectangle r = new DERectangle();
		r.setSize(new Point(250, 100));
		r.setLabel(td.clone());
		r.setBorder(new BorderData(3, Color.black, 1));

		w.setOffDrawElement(r.clone());

		w.setPressedOffDrawElement(r.clone());

		td.setText("");
		r.setLabel(td.clone());
		w.setOnDrawElement(r.clone());

		w.setPressedOnDrawElement(r.clone());

		w.setPos(new Point(400, 850));
		w.setHitboxFromDrawElement();

		w.setActive(false);

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
				MenuHost.this.stop();

				MenuHost.this.resetPads();

				MenuHost.this.start();
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
				MenuHost.this.stop();
				MenuHost.this.m.applyPage(new MenuServerSettings(MenuHost.this.m, 0));
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
