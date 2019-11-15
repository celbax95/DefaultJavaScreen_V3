package fr.state.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.inputs.Input;
import fr.server.p2p.Multiplayer;
import fr.server.p2p.PData;
import fr.server.p2p.PDataProcessor;
import fr.state.game.elements.players.MyPlayer;
import fr.state.game.elements.players.OtherPlayer;
import fr.state.game.elements.players.Player;
import fr.state.game.elements.utilities.Camera;
import fr.util.point.Point;
import fr.window.WinData;

public class Game implements PDataProcessor {

	private final static int MISSING_PDATA = 50;

	private GameState gameState;

	private Map<Integer, Player> players;

	private Multiplayer multiplayer;

	private Queue<PData> qPData;

	private LinkedList<Integer> missingPData;

	private int currentPDataID;

	private int myId;

	private Player myPlayer;

	private Camera camera;

	private WinData winData;

	public Game(GameState gameState, Multiplayer multiplayer, WinData winData, int myId,
			Map<Integer, Map<String, Object>> playersData) {
		this.gameState = gameState;

		this.multiplayer = multiplayer;

		this.winData = winData;

		this.myId = myId;

		this.currentPDataID = -1;

		this.players = new HashMap<>();

		this.myPlayer = new MyPlayer(myId, this.multiplayer);
		this.myPlayer.setPos((Point) playersData.get(myId).get("pos"));
		this.myPlayer.setColor((Color) playersData.get(myId).get("color"));
		this.myPlayer.setSize(new Point(200, 200));

		for (int id : playersData.keySet()) {
			if (id == myId) {
				continue;
			}
			OtherPlayer p = new OtherPlayer(id);
			p.setPos((Point) playersData.get(id).get("pos"));
			p.setColor((Color) playersData.get(id).get("color"));
			p.setSize(new Point(200, 200));
			this.players.put(id, p);
		}

		this.players.put(myId, this.myPlayer);

		this.missingPData = new LinkedList<>();

		this.qPData = new ConcurrentLinkedQueue<>();

		this.camera = new Camera(winData);
	}

	public void draw(Graphics2D g, double dt) {

		AffineTransform af = g.getTransform();

		g.setTransform(this.camera.getTransform(af));

		for (Player player : this.players.values()) {
			player.draw(g, dt);
		}
	}

	public GameState getGameState() {
		return this.gameState;
	}

	@Override
	public void newPData(PData pdata) {
		this.qPData.offer(pdata);
	}

	private void processPData(PData pdata) {
		// On ignore nos propres messages
		if (pdata.getPlayerId() == this.myId)
			return;

		int id = pdata.getId();

		if (this.currentPDataID == -1) {
			this.currentPDataID = 0;
		}

		boolean process = true;

		if (id < this.currentPDataID) {

			// Retire le PData rate qui vient d'etre recu

			if (this.missingPData.contains(id)) {
				this.missingPData.remove((Object) id);
			} else {
				process = false;
			}
		} else if (id > this.currentPDataID) {

			// Ajout des PData rate a la liste missing

			for (int i = this.missingPData.size() + id - this.currentPDataID - MISSING_PDATA; i >= 0; i--) {
				this.missingPData.removeFirst();
			}

			for (int i = this.currentPDataID; i < id; i++) {
				this.missingPData.add(i);
			}

			this.currentPDataID = id + 1;
		} else {
			this.currentPDataID++;
		}

		if (!process)
			return;

		switch (pdata.getOpId()) {
		case CONFIRM:
			break;
		case MOVE:
			Player p = this.players.get(pdata.getPlayerId());
			Object[] data = pdata.getData();
			if (data.length != 2)
				return;
			p.setPos(new Point((int) data[0], (int) data[1]));
		default:
			break;
		}
	}

	public void resetForces() {
		this.myPlayer.resetForces();
	}

	public void setMenuState(GameState gameState) {
		this.gameState = gameState;
	}

	public void setMultiplayer(Multiplayer multiplayer) {
		this.multiplayer = multiplayer;
	}

	public void update(Input input, double dt) {

		this.camera.setAimedCenterPos(this.myPlayer.getPos().clone().add(this.myPlayer.getSize().clone().div(2)));

		this.camera.update(dt);

		while (!this.qPData.isEmpty()) {
			PData data = this.qPData.poll();
			if (data == null) {
				break;
			}
			this.processPData(data);
		}

		this.myPlayer.update(input, dt);
	}
}
