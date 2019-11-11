package fr.state.loading;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.util.point.Point;

public class ProgressBar {

	private double fillingSpeed; // (px / secondes);

	private long lastCall;

	private Point pos;
	private Point size;

	private int lastSeparatorX;
	private int aimedSeparatorX;
	private double realSeparatorX;

	private int current;
	private int max;

	private Color backgroundColor;

	private Color fillColor;
	private int borderSize;

	private Color borderColor;

	public ProgressBar(Point pos, Point size, int max, Color backgroundColor, Color fillColor, int borderSize,
			Color borderColor) {
		super();
		this.lastCall = 0;
		this.pos = pos;
		this.size = size;
		this.max = max;
		this.backgroundColor = backgroundColor;
		this.fillColor = fillColor;
		this.borderSize = borderSize;
		this.borderColor = borderColor;
		this.current = 0;

		this.lastSeparatorX = 0;
		this.aimedSeparatorX = 0;
		this.realSeparatorX = 0;

		this.fillingSpeed = size.x / 2; // recalc
	}

	private double calcSpeed() {
		// clamping
		double x = Math.abs(this.realSeparatorX - this.lastSeparatorX)
				/ Math.abs(this.aimedSeparatorX - this.lastSeparatorX) * (Math.PI * 2);

		// function
		double ratio = this.f(x);

		return ratio * this.fillingSpeed;
	}

	public void draw(Graphics2D g) {
		this.moveSeparator();

		// Border
		g.setColor(this.borderColor);
		g.fillRect(this.pos.ix() - this.borderSize, this.pos.iy() - this.borderSize,
				this.size.ix() + this.borderSize * 2, this.size.iy() + this.borderSize * 2);

		// background
		g.setColor(this.backgroundColor);
		g.fillRect(this.pos.ix() + (int) this.realSeparatorX, this.pos.iy(), this.size.ix() - (int) this.realSeparatorX,
				this.size.iy());

		// fill
		g.setColor(this.fillColor);
		g.fillRect(this.pos.ix(), this.pos.iy(), (int) this.realSeparatorX, this.size.iy());
	}

	private double f(double x) {
		return Math.cos(x + Math.PI) / 2 + 1 + 0.001;
	}

	public int getCurrent() {
		return this.current;
	}

	private void moveSeparator() {
		if (this.aimedSeparatorX == this.realSeparatorX)
			return;

		long call = System.currentTimeMillis();
		long elapsed = call - this.lastCall;

		double curFillingSpeed = this.calcSpeed();

		int toMove = (int) (elapsed / 1000D * curFillingSpeed);

		this.realSeparatorX = this.realSeparatorX + toMove > this.aimedSeparatorX ? this.aimedSeparatorX
				: this.realSeparatorX + toMove;

		this.lastCall = call;
	}

	private void setAimedSeparatorX() {
		this.lastSeparatorX = this.aimedSeparatorX;
		this.aimedSeparatorX = (int) (this.size.x * (this.current / (double) this.max));
		this.lastCall = System.currentTimeMillis();
		this.fillingSpeed = Math.abs(this.aimedSeparatorX - this.lastSeparatorX) * 2;
	}

	public void setCurrent(int x) {
		if (x > this.max) {
			x = this.max;
		}
		if (x < 0) {
			x = 0;
		}
		this.current = x;
		this.setAimedSeparatorX();
	}

	/**
	 * @param d : en pourcentage par seconde [0:1]
	 */
	public void setFillingSpeed(double d) {
		if (d > 1) {
			d = 1;
		}
		if (d < 0) {
			d = 0;
		}

		this.fillingSpeed = this.size.x * d;
	}

	public void setMaxSteps(int x) {
		this.lastSeparatorX = 0;
		this.aimedSeparatorX = 0;
		this.realSeparatorX = 0;
		this.fillingSpeed = this.size.x / 2; // recalc
		this.lastCall = System.currentTimeMillis();
		this.current = 0;
		this.max = x;
	}
}
