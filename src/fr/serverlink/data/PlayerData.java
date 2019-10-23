package fr.serverlink.data;

import java.awt.Color;

public class PlayerData {

	private int id;

	private String username;

	private Color color;

	public PlayerData(int id, String username, Color color) {
		super();
		this.id = id;
		this.username = username;
		this.color = color;
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

	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return this.username;
	}
}
