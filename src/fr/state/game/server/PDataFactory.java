package fr.state.game.server;

import fr.util.point.Point;

public class PDataFactory {

	private static final int PDATA_ID = 100000;

	private int pDataId;

	public PDataFactory() {
		this.pDataId = 0;
	}

	public PData createMove(int playerId, Point pos) {
		return new PData(this.getPDataId(), PData.OP.MOVE, playerId, false, null);
	}

	private int getPDataId() {
		return this.pDataId++ % PDATA_ID;
	}
}
