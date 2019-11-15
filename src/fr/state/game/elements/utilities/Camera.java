package fr.state.game.elements.utilities;

import java.awt.geom.AffineTransform;

import fr.util.point.Point;

public class Camera {

	Point pos;

	Point aimedPos;

	double acceleration = 2; // 0.5 is slow / 2 is mid / 10 is really fast

	public Camera() {
		this.pos = new Point();
		this.aimedPos = this.pos.clone();
	}

	public AffineTransform getTransform(AffineTransform origin) {
		AffineTransform af = origin;
		if (af == null) {
			af = new AffineTransform();
		}

		af.translate(-this.pos.x, -this.pos.y);

		return af;
	}

	public Point getVectMove() {
		Point diff = this.pos.compareTo(this.aimedPos);

		return new Point(Math.signum(this.aimedPos.x - this.pos.x) * diff.x * this.acceleration,
				Math.signum(this.aimedPos.y - this.pos.y) * diff.y * this.acceleration);
	}

	private void move(double dt) {
		if (this.pos != null && this.aimedPos != null && !this.pos.equals(this.aimedPos)) {

			boolean posXInfToAim = this.pos.x < this.aimedPos.x;
			boolean posYInfToAim = this.pos.y < this.aimedPos.y;

			Point vectMove = this.getVectMove().mult(dt);

			// Deplacement
			this.pos.add(vectMove);

			// AimedPos depassee
			if (posXInfToAim != this.pos.x < this.aimedPos.x) {
				this.pos.x = this.aimedPos.x;
			}
			if (posYInfToAim != this.pos.y < this.aimedPos.y) {
				this.pos.y = this.aimedPos.y;
			}
		}
	}

	public void setAimedPos(Point ap) {
		this.aimedPos = ap.clone();
	}

	public void update(double dt) {
		this.move(dt);
	}
}
