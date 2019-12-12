package fr.state.game.elements.utilities;

import java.awt.geom.AffineTransform;

import fr.inputs.Input;
import fr.util.Util;
import fr.util.point.Point;
import fr.window.WinData;

public class Camera {

	private static final double WEEK_RATE = 0.8;

	private final double DIST_TO_MOVE = 0.1, DIST_TO_FULLSPEED = 1.5;

	private final double MAX_SCALE = 2, MIN_SCALE = 0.1;

	private final double ZOOM_STEP = 1.5;

	private final double MAX_SPEED = 12.5;

	private final double DEFAULT_SCALE = 0.8;

	private WinData winData;

	private Point pos;

	private Point aimedPos;

	private Point forces;

	private double scale;

	private Point scalePosAdjust;

	private double sizeUnit;

	public Camera(WinData winData, double sizeUnit) {
		this.sizeUnit = sizeUnit;
		this.pos = new Point();
		this.aimedPos = this.pos.clone();
		this.forces = new Point();
		this.scalePosAdjust = new Point();
		this.winData = winData;
		this.scale = 1;
		this.setScale(this.DEFAULT_SCALE);
	}

	public void addForce(Point f) {
		this.forces.add(f);
	}

	public void applyForces(double dt) {
		this.pos.add(this.forces.clone().mult(dt));
	}

	public void applyWeakForces(double dt) {
		this.applyForces(dt * WEEK_RATE);
	}

	private Point getMoveForce(Point aim) {
		if (this.pos != null && aim != null && !this.pos.equals(aim)) {

			double dist = this.pos.distanceTo(aim);

			double dtm = this.DIST_TO_MOVE * this.sizeUnit / this.scale;
			double dtf = this.DIST_TO_FULLSPEED * this.sizeUnit / this.scale;

			if (dist > this.DIST_TO_MOVE * this.sizeUnit) {
				// Speed
				double i = (Util.clamp(dist, dtm, dtf) - dtm) / (dtf - dtm);

				// smootherstep
				i = i * i * i * (i * (i * 6 - 15) + 10);

				double speed = this.MAX_SPEED * this.sizeUnit * i;

				// Direction
				Point dir = this.pos.vectTo(aim).trigNorm();

				return dir.mult(speed);

			} else {

			}
		}
		return new Point();
	}

	public Point getPos() {
		return this.pos.clone();
	}

	public AffineTransform getTransform(AffineTransform origin) {
		AffineTransform af = origin;
		if (af == null) {
			af = new AffineTransform();
		}

		af.scale(this.scale, this.scale);

		af.translate(this.scalePosAdjust.x, this.scalePosAdjust.y);

		af.translate(-this.pos.x, -this.pos.y);

		return af;
	}

	public void resetForces() {
		this.forces.set(0, 0);
	}

	public void setAimedCenterPos(Point p) {
		this.setAimedPos(p.clone().sub(this.winData.getDefaultWindowSize().clone().div(2)));
	}

	public void setAimedPos(Point ap) {
		this.aimedPos = ap.clone();
	}

	public void setMoveToAimForce() {
		this.addForce(this.getMoveForce(this.aimedPos));
	}

	public void setPos(Point p) {
		this.pos.set(p);
	}

	public void setScale(double scale) {
		if (scale < this.MIN_SCALE || scale > this.MAX_SCALE)
			return;

		this.scale = scale;

		Point newScalePosAdjust = this.winData.getDefaultWindowSize().clone();
		newScalePosAdjust = newScalePosAdjust.clone().div(this.scale).sub(newScalePosAdjust).div(2);
		this.scalePosAdjust.set(newScalePosAdjust);
	}

	public void update(Input input, double dt) {

		boolean unzoom = input.keyboardKeys.get(109), zoom = input.keyboardKeys.get(107);

		// OU exclusif
		if (!(unzoom && zoom) && (unzoom || zoom)) {
			double newScale = this.scale;

			if (unzoom) {
				// * scale pour que le changement soit lin�aire
				newScale -= this.ZOOM_STEP * this.scale * dt;
			} else {
				// * scale pour que le changement soit lin�aire
				newScale += this.ZOOM_STEP * this.scale * dt;
			}

			this.setScale(newScale);
		}
	}
}
