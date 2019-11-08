package fr.state.loading;

import java.awt.Color;

import fr.util.point.Point;

public class PlayerData {

	private int id;

	private Point pos;

	private String username;

	private Color color;

	/**
	 * @param id
	 * @param username
	 * @param color
	 * @param ready
	 */
	public PlayerData(int id, String username, Point pos, Color color) {
		super();
		this.id = id;
		this.username = username;
		this.pos = new Point(pos);
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

	/**
	 * @return the pos
	 */
	public Point getPos() {
		return this.pos;
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

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		this.pos = pos;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return this.username;
	}
}
