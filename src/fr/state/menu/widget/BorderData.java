package fr.state.menu.widget;

import java.awt.Color;

public class BorderData {
	public int size;
	public Color color;
	public Color secondColor;

	public BorderData() {
		this.size = 0;
		this.color = Color.BLACK;
		this.secondColor = Color.BLACK;
	}

	public BorderData(int size, Color color, Color secondColor) {
		super();
		this.size = size;
		this.color = color;
		this.secondColor = secondColor;
	}
}
