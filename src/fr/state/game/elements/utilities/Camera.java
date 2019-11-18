package fr.state.game.elements.utilities;

import java.awt.geom.AffineTransform;

import fr.util.Util;
import fr.util.point.Point;
import fr.window.WinData;

public class Camera {

	WinData winData;

	Point pos;

	Point aimedPos;

	double MAX_SPEED = 2500;

	double acceleration = 3;

	int pixelsTolerence = 2;

	Point forces;

	public Camera(WinData winData) {
		this.pos = new Point();
		this.aimedPos = this.pos.clone();
		this.forces = new Point();
		this.winData = winData;
	}

	public void addForce(Point f) {
		this.forces.add(f);
	}

	private void addMoveForce() {
		if (this.pos != null && this.aimedPos != null && !this.pos.equals(this.aimedPos)) {
			double min = 20, max = 300;

			double dist = this.pos.distanceTo(this.aimedPos);

			if (dist > min) {
				// Speed
				double i = (Util.clamp(dist, min, max) - min) / (max - min);

				// smootherstep
				i = i * i * i * (i * (i * 6 - 15) + 10);

				double speed = this.MAX_SPEED * i;

				// Direction
				Point dir = this.pos.vectTo(this.aimedPos).trigNorm();

				// Move
				this.addForce(dir.mult(speed));
			} else {
				// Rien
			}
		}
	}

	public void applyForces(double dt) {
		this.pos.add(this.forces.clone().mult(dt));
	}

	public AffineTransform getTransform(AffineTransform origin, double dt) {
		AffineTransform af = origin;
		if (af == null) {
			af = new AffineTransform();
		}

		af.translate(-this.pos.x, -this.pos.y);

		return af;
	}

	public Point getVectMove() {
		return null;
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

	public void setPos(Point p) {
		this.pos.set(p);
	}

	public void update() {
		this.addMoveForce();
	}
}
