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
import fr.state.game.elements.onscreen.GOTag;
import fr.state.game.elements.onscreen.GameObject;
import fr.state.game.elements.players.DummyPlayer;
import fr.state.game.elements.players.InputReactivePlayer;
import fr.state.game.elements.players.Player;
import fr.state.game.elements.utilities.Camera;
import fr.util.point.Point;
import fr.window.WinData;

public class Game implements PDataProcessor {

	private final static int MISSING_PDATA = 50;

	private static final int SIZE = 1;

	private GameState gameState;

	private Map<Integer, Player> players;

	private Map<Integer, GameObject> gameObjects;

	private Multiplayer multiplayer;

	private Queue<PData> qPData;

	private LinkedList<Integer> missingPData;

	private int currentPDataID;

	private int myId;

	private InputReactivePlayer myPlayer;

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
		this.gameObjects = new HashMap<>();

		this.myPlayer = new InputReactivePlayer(myId, this.multiplayer, Constants.SIZE_UNIT);
		this.myPlayer.setScale(1);
		this.myPlayer.setPos((Point) playersData.get(myId).get("pos"));
		this.myPlayer.setColor((Color) playersData.get(myId).get("color"));
		this.myPlayer.setSize(new Point(SIZE, SIZE));
		this.myPlayer.addTags(GOTag.PLAYER);

		for (int id : playersData.keySet()) {
			if (id == myId) {
				continue;
			}
			DummyPlayer p = new DummyPlayer(id, Constants.SIZE_UNIT);
			p.setPos((Point) playersData.get(id).get("pos"));
			p.setColor((Color) playersData.get(id).get("color"));
			p.setSize(new Point(SIZE, SIZE));
			p.addTags(GOTag.PLAYER);
			this.players.put(id, p);
			this.addGameObjects(p);
		}

		this.players.put(myId, this.myPlayer);
		this.addGameObjects(this.myPlayer);

		this.missingPData = new LinkedList<>();

		this.qPData = new ConcurrentLinkedQueue<>();

		this.camera = new Camera(winData, Constants.SIZE_UNIT);
	}

	public void addGameObjects(GameObject... gameObjects) {
		for (GameObject go : gameObjects) {
			if (this.gameObjects.containsKey(go.getId()) == false) {
				this.gameObjects.put(go.getId(), go);
			}
		}
	}

	public boolean containsGameObject(GameObject go) {
		return this.containsGameObject(go.getId());
	}

	public boolean containsGameObject(int id) {
		return this.gameObjects.containsKey(id);
	}

	public void draw(Graphics2D g, double dt) {

		AffineTransform origin = g.getTransform();

		Point interpolatedPlayerPos = this.myPlayer.getInterpolatedPos(dt).add(this.myPlayer.getSize().clone().div(2));

		this.camera.setAimedCenterPos(interpolatedPlayerPos);
		this.camera.setMoveToAimForce();
		this.camera.applyForces(dt);

		g.setTransform(this.camera.getTransform(origin));

		for (GameObject go : this.gameObjects.values()) {
			go.draw(g, dt);
		}

		g.setTransform(origin);
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

	public void removeGameObject(GameObject gameObject) {
		this.gameObjects.remove(gameObject.getId());
	}

	public void removeGameObject(int id) {
		this.gameObjects.remove(id);
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

		// Reset des forces du tour de boucle precedent
		for (Player p : this.players.values()) {
			p.resetForces();
		}
		this.camera.resetForces();

		// Traite des paquets recus entre deux tour de boucle
		while (!this.qPData.isEmpty()) {
			PData data = this.qPData.poll();
			if (data == null) {
				break;
			}
			this.processPData(data);
		}

		// Application des forces relatives aux actions utilisateur au joueur
		this.myPlayer.update(input, dt);

		// Exec Forces

		for (GameObject go : this.gameObjects.values()) {
			go.applyForces(dt);
		}

		this.camera.setAimedCenterPos(this.myPlayer.getPos().clone().add(this.myPlayer.getSize().clone().div(2)));
		this.camera.setMoveToAimForce();

		this.camera.update(input, dt);

		this.camera.applyForces(dt);
	}
}
