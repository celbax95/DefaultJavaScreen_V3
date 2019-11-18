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

	public void applyForces(double dt) {
		this.pos.add(this.forces.clone().mult(dt));
	}

	private Point getMoveForce(Point aim) {
		if (this.pos != null && aim != null && !this.pos.equals(aim)) {
			double min = 20, max = 300;

			double dist = this.pos.distanceTo(aim);

			if (dist > min) {
				// Speed
				double i = (Util.clamp(dist, min, max) - min) / (max - min);

				// smootherstep
				i = i * i * i * (i * (i * 6 - 15) + 10);

				double speed = this.MAX_SPEED * i;

				// Direction
				Point dir = this.pos.vectTo(aim).trigNorm();

				return dir.mult(speed);

			} else {

			}
		}
		return new Point();
	}

	public AffineTransform getTransform(AffineTransform origin) {
		AffineTransform af = origin;
		if (af == null) {
			af = new AffineTransform();
		}

		af.translate(-this.pos.x, -this.pos.y);

		return af;
	}

	public AffineTransform getTransform(AffineTransform origin, Point interpolatedAimPos, double dt) {
		AffineTransform af = origin;
		if (af == null) {
			af = new AffineTransform();
		}

		interpolatedAimPos = interpolatedAimPos.clone().sub(this.winData.getDefaultWindowSize().clone().div(2));

		Point interpolatedPos = this.pos.clone().add(this.getMoveForce(interpolatedAimPos).mult(dt));

		af.translate(-interpolatedPos.ix(), -interpolatedPos.iy());

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

	public void setPos(Point p) {
		this.pos.set(p);
	}

	public void update() {
		this.addForce(this.getMoveForce(this.aimedPos));
	}
}
