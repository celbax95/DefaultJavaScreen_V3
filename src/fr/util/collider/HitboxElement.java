package fr.util.collider;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class HitboxElement {

	public final Color COLOR = new Color(30, 255, 30, 80);

	public abstract void draw(Graphics2D g);

	public abstract int getType();
}