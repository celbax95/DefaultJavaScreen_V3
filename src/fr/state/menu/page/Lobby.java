package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.serverlink.data.PlayerData;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DELabel;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class Lobby {

	private static final String NAME2 = "lobby";
	private static final String RES_FOLDER2 = "/resources/menu/lobby/";
	private static final String RES_EXTENSION = ".png";

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
			RES_NAMES2[i] = NAME2 + "/" + RES_NAMES2[i];
		}

		for (int i = 0; i < RES_PATHS2.length; i++) {
			RES_PATHS2[i] = RES_FOLDER2 + RES_PATHS2[i] + RES_EXTENSION;
		}
	}

	private static final int maxPlayer = 4;

	private List<Widget> widgets;

	private WElement[] pads;

	private WElement[] ready;

	private WElement[] usernames;

	private PlayerData[] players;

	private MenuPage parent;

	public Lobby(MenuPage parent, Point[] padsPos) {
		this.parent = parent;

		this.widgets = new Vector<>();

		this.loadResources();

		this.pads = new WElement[maxPlayer];
		this.usernames = new WElement[maxPlayer];
		this.ready = new WElement[maxPlayer];
		this.players = new PlayerData[maxPlayer];

		for (int i = 0; i < maxPlayer; i++) {
			this.wPadFrame(padsPos[i]);
			this.usernames[i] = this.wUsername(padsPos[i].clone().add(new Point(0, 180)));
			this.pads[i] = this.wPad(padsPos[i].clone().add(new Point(58, 3)));
			this.ready[i] = this.wReady(padsPos[i].clone().add(new Point(180, 124)));
		}
		for (int i = 0; i < Lobby.maxPlayer; i++) {
			this.players[i] = null;
		}
	}

	public void addPlayer(PlayerData p) {
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

	public PlayerData getMainPlayer() {
		return this.players[0];
	}

	public int getNbPlayer() {
		int nb = 0;
		for (PlayerData player : this.players) {
			if (player != null) {
				nb++;
			}
		}
		return nb;
	}

	public int getNbReady() {
		int nb = 0;
		for (PlayerData player : this.players) {
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

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES2, RES_PATHS2);
	}

	private void putPlayerOnPad(PlayerData p, int padId) {
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
		for (int i = 1; i < Lobby.maxPlayer; i++) {
			this.removePlayerFromPad(i);
		}
	}

	public void setMainPlayer(PlayerData player) {
		this.putPlayerOnPad(player, 0);
	}

	public void setPadLabel(int padId, String text) {
		text = text.trim();
		if (text == null || text.equals("")) {
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

			i.setImage(ready ? im.get("lobby/readyCell") : im.get("lobby/notReadyCell"));

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

		i.setImage(im.get("lobby/playerPadEmpty"));

		w.setDrawElement(i);
		w.setPos(pos.clone());

		this.widgets.add(w);

		return w;
	}

	private WElement wReady(Point pos) {

		ImageManager im = ImageManager.getInstance();

		WElement w = new WElement(this.parent);

		DEImage i = new DEImage();

		i.setImage(im.get("lobby/notReadyCell"));

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
				1);

		l.setLabel(td);
		l.setSupBound(new Point(270, 0));

		w.setPos(pos);
		w.setDrawElement(l);

		this.widgets.add(w);

		return w;
	}
}
