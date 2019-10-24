package fr.serverlink.data;

import java.awt.Color;

public class PlayerData {

	private int id;

	private String username;

	private Color color;

	private boolean ready;

	/**
	 * @param id
	 * @param username
	 * @param color
	 * @param ready
	 */
	public PlayerData(int id, String username, Color color, boolean ready) {
		super();
		this.id = id;
		this.username = username;
		this.color = color;
		this.ready = ready;
	}

	public Color getColor() {
		return this.color;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
	}

	public String getUsername() {
		return this.username;
	}

	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return this.ready;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param ready the ready to set
	 */
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return this.username;
	}
}
