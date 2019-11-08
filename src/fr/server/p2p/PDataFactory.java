package fr.server.p2p;

import java.awt.Color;

import fr.util.point.Point;

public class PDataFactory {

	private static final int PDATA_ID = 100000;

	private int pDataId;

	public PDataFactory() {
		this.pDataId = 0;
	}

	public PData createMove(int playerId, Point pos) {
		return new PData(this.getPDataId(), PData.OP.MOVE, playerId, false, new Object[] { pos.ix(), pos.iy() });
	}

	public PData createPlayerState(int playerId, String username, Point pos, Color color) {
		return new PData(this.getPDataId(), PData.OP.PLAYER_STATE, playerId, false,
				new Object[] { username, pos, color });
	}

	private int getPDataId() {
		return this.pDataId++ % PDATA_ID;
	}
}
