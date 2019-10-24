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

	private Object profileConf;

	private XMLManager manager;

	private WElement pad1, pad2;

	private WElement[] pads;

	private WElement[] ready;

	private PlayerData[] players;

	private int maxPlayer = 4;

	private int idServer;

	private HubJoiner hub;

	private Searcher searcher;

	private Color defaultPadColor = new Color(0, 0, 0, 0);

	public MenuJoin(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		this.loadResources();

		DatafilesManager dfm = DatafilesManager.getInstance();
		this.profileConf = dfm.getFile("profile");
		this.manager = dfm.getXmlManager();

		String myUsername = (String) this.manager.getParam(this.profileConf, PARAM_NAME_USERNAME, "user");
		Color myColor = Color.decode((String) this.manager.getParam(this.profileConf, PARAM_NAME_COLOR, "#000000"));

		this.wReady();
		this.wTitle();
		this.wBack();

		this.pads = new WElement[this.maxPlayer];
		this.ready = new WElement[this.maxPlayer];
		this.players = new PlayerData[this.maxPlayer];

		int size = 300;

		for (int i = 0; i < this.maxPlayer; i++) {
			this.pads[i] = this.wPad(new Point(300 + size * i, 450));
			this.ready[i] = this.wReady(new Point(410 + size * i, 720));
		}

		for (int i = 0; i < this.maxPlayer; i++) {
			this.players[0] = null;
		}

		this.putPlayerOnPad(new PlayerData(-1, myUsername, myColor), 0);

		new Thread(new Runnable() {
			@Override
			public void run() {
				MenuJoin.this.hub = new HubJoiner(myUsername, myColor, ServerData.getGroup(MenuJoin.this.idServer),
						ServerData.getPort(MenuJoin.this.idServer)) {

					@Override
					public void idAssigned(int id) {
						MenuJoin.this.players[0].id = id;
					}

					@Override
					public void noMorePlayer() {
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
				MenuJoin.this.searcher = new Searcher(MenuJoin.this.hub, ServerData.getGroup(MenuJoin.this.idServer),
						ServerData.getPort(MenuJoin.this.idServer));

				MenuJoin.this.hub.start();
				MenuJoin.this.searcher.start();
			}
		}).start();
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

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
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

		this.players[padId] = null;

	}

	private void setPlayerReady(int playerPad, boolean ready) {
		if (playerPad == -1)
			return;

		((DERectangle) this.ready[playerPad].getDrawElement()).setColor(ready ? READY : NOT_READY);
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

	private WElement wColorBlock() {
		BorderData border = new BorderData(4, Color.BLACK, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(52, 380));
		rect.setBorder(border);

		WElement w = new WElement(this);
		w.setDrawElement(rect);
		w.setPos(new Point(1014, 528));

		this.widgets.add(w);

		return w;
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

	private void wReady() {
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

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(550, 76));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/title"));

		title.setDrawElement(i);

		this.widgets.add(title);
	}
}
