package fr.state.game.server;

import java.io.Serializable;

public class PData implements Serializable {

	public enum OP {
		MOVE, CONFIRM,
	}

	private static final long serialVersionUID = 1L;

	private int id;

	private OP opId;

	private int playerId;

	private boolean needConfirm;

	private Object[] data;

	public PData(int id, OP opId, int playerId, boolean needConfirm, Object[] data) {
		super();
		this.id = id;
		this.opId = opId;
		this.playerId = playerId;
		this.needConfirm = needConfirm;
		this.data = data;
	}

	public Object[] getData() {
		return this.data;
	}

	public int getId() {
		return this.id;
	}

	public OP getOpId() {
		return this.opId;
	}

	public int getPlayerId() {
		return this.playerId;
	}

	public boolean isNeedConfirm() {
		return this.needConfirm;
	}

	public void setData(Object[] data) {
		this.data = data;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNeedConfirm(boolean needConfirm) {
		this.needConfirm = needConfirm;
	}

	public void setOpId(OP opId) {
		this.opId = opId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
}
