package fr.server;

import java.awt.Color;

public class PlayerData {

	private String username;

	private Color color;

	public PlayerData(String username, Color color) {
		super();
		this.username = username;
		this.color = color;
	}

	public Color getColor() {
		return this.color;
	}

	public String getUsername() {
		return this.username;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
