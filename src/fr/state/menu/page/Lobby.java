package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.server.serverlink.data.HubPlayerData;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DELabel;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class Lobby {

	private static final String PAGE_NAME = "menu/lobby";
	private static final String RES_FOLDER = "/resources/menu/lobby/";
	private static final String RES_EXTENSION = ".png";

	private static final Point USERNAME_SIZE = new Point(254, 37);

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

	static {
		for (int i = 0; i < RES_NAMES2.length; i++) {
			RES_NAMES2[i] = PAGE_NAME + "/" + RES_NAMES2[i];
		}

		for (int i = 0; i < RES_PATHS2.length; i++) {
			RES_PATHS2[i] = RES_FOLDER + RES_PATHS2[i] + RES_EXTENSION;
		}
	}

	public static String getPageName() {
		return PAGE_NAME;
	}

	private int maxPlayer;

	private List<Widget> widgets;

	private WElement[] pads;

	private WElement[] ready;

	private WElement[] usernames;

	private HubPlayerData[] players;

	private MenuPage parent;

	public Lobby(MenuPage parent, Point[] padsPos) {
		this.parent = parent;

		this.widgets = new Vector<>();

		this.maxPlayer = padsPos.length;

		this.loadResources();

		this.pads = new WElement[this.maxPlayer];
		this.usernames = new WElement[this.maxPlayer];
		this.ready = new WElement[this.maxPlayer];
		this.players = new HubPlayerData[this.maxPlayer];

		for (int i = 0; i < this.maxPlayer; i++) {
			this.pads[i] = this.wPad(padsPos[i].clone().add(new Point(58, 3)));
			this.ready[i] = this.wReady(padsPos[i].clone().add(new Point(180, 124)));
			this.wPadFrame(padsPos[i]);
			this.usernames[i] = this.wUsername(padsPos[i].clone().add(new Point(3, 172)));
		}
		for (int i = 0; i < this.maxPlayer; i++) {
			this.players[i] = null;
		}
	}

	public void addPlayer(HubPlayerData p) {
		int i = this.getEmptyPad();

		if (i == -1)
			return;

		this.putPlayerOnPad(p, i);
		this.updatePads();
	}

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

	public HubPlayerData getMainPlayer() {
		return this.players[0];
	}

	public int getNbPlayer() {
		int nb = 0;
		for (HubPlayerData player : this.players) {
			if (player != null) {
				nb++;
			}
		}
		return nb;
	}

	public int getNbReady() {
		int nb = 0;
		for (HubPlayerData player : this.players) {
			if (player != null && player.isReady()) {
				nb++;
			}
		}
		return nb;
	}

	private int getPlayerPad(int playerId) {
		for (int i = 0; i < this.players.length; i++) {
			if (this.players[i] != null && this.players[i].getId() == playerId)
				return i;
		}
		return -1;
	}

	/**
	 * @return the players
	 */
	public List<Integer> getPlayersId() {
		ArrayList<Integer> ids = new ArrayList<>();
		for (HubPlayerData p : this.players) {
			if (p != null) {
				ids.add(p.getId());
			}
		}
		return ids;
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES2, RES_PATHS2);
	}

	private void putPlayerOnPad(HubPlayerData p, int padId) {
		this.players[padId] = p;

		DERectangle r = (DERectangle) this.pads[padId].getDrawElement();

		r.setColor(p.getColor());

		this.pads[padId].setVisible(true);

		this.setPadLabel(padId, p.getUsername());

		this.setPadReady(padId, p.isReady());
	}

	public void removeAllPlayers() {
		this.resetPads();
	}

	public void removePlayer(int playerId) {
		this.removePlayerFromPad(this.getPlayerPad(playerId));
		this.updatePads();
	}

	private void removePlayerFromPad(int padId) {
		if (padId < 0)
			return;

		this.players[padId] = null;

		this.pads[padId].setVisible(false);

		this.setPadReady(padId, false);

		this.setPadLabel(padId, null);
	}

	private void resetPads() {
		for (int i = 1; i < this.maxPlayer; i++) {
			this.removePlayerFromPad(i);
		}
	}

	public void setMainPlayer(HubPlayerData player) {
		this.putPlayerOnPad(player, 0);
	}

	private void setPadLabel(int padId, String text) {
		if (text == null || text.trim().equals("")) {
			this.usernames[padId].setVisible(false);
		} else {
			DELabel u = (DELabel) this.usernames[padId].getDrawElement();
			TextData td = u.getLabel().clone();
			td.setText(text);

			while (td.getSize().x > 270 - 40) {
				text = text.substring(0, text.length() - 5);
				text += "...";
				td.setText(text);
			}

			u.setLabel(td);
			this.usernames[padId].setVisible(true);
		}
	}

	private void setPadReady(int playerPad, boolean ready) {
		if (playerPad == -1)
			return;

		ImageManager im = ImageManager.getInstance();

		if (this.players[playerPad] == null) {
			this.ready[playerPad].setVisible(false);
		} else {
			this.players[playerPad].setReady(ready);

			this.ready[playerPad].setVisible(true);
			DEImage i = (DEImage) this.ready[playerPad].getDrawElement().clone();

			i.setImage(ready ? im.get(PAGE_NAME + "/readyCell") : im.get(PAGE_NAME + "/notReadyCell"));

			this.ready[playerPad].setDrawElement(i);

		}
	}

	public void setPlayerReady(int playerId, boolean ready) {
		this.setPadReady(this.getPlayerPad(playerId), ready);
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

	private WElement wPad(Point pos) {
		WElement w = new WElement(this.parent);

		DERectangle de = new DERectangle();

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

		WElement w = new WElement(this.parent);

		DEImage i = new DEImage();

		i.setImage(im.get(PAGE_NAME + "/playerPadEmpty"));

		w.setDrawElement(i);
		w.setPos(pos.clone());

		this.widgets.add(w);

		return w;
	}

	private WElement wReady(Point pos) {

		ImageManager im = ImageManager.getInstance();

		WElement w = new WElement(this.parent);

		DEImage i = new DEImage();

		i.setImage(im.get(PAGE_NAME + "/notReadyCell"));

		w.setDrawElement(i);
		w.setPos(pos.clone());
		w.setVisible(false);

		this.widgets.add(w);

		return w;
	}

	private WElement wUsername(Point pos) {
		WElement w = new WElement(this.parent);

		DELabel l = new DELabel();

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 30), "", Color.BLACK,
				3);

		l.setLabel(td);
		l.setSupBound(USERNAME_SIZE.clone());

		w.setPos(pos);
		w.setDrawElement(l);

		this.widgets.add(w);

		return w;
	}
}
