package fr.state.game.elements.utilities;

import java.awt.geom.AffineTransform;

import fr.util.point.Point;
import fr.window.WinData;

public class Camera {

	WinData winData;

	Point pos;

	Point aimedPos;

	double speed = 500;

	double acceleration = 3;

	int pixelsTolerence = 2;

	public Camera(WinData winData) {
		this.pos = new Point();
		this.aimedPos = this.pos.clone();
		this.winData = winData;
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
		Point diff = this.aimedPos.sub(this.pos).trigNorm();

		return diff.mult(this.speed);
	}

	private void move(double dt) {
		if (this.pos != null && this.aimedPos != null && !this.pos.equals(this.aimedPos)) {

			Point vectMove = this.getVectMove().mult(dt);

			// Deplacement
			this.pos.add(vectMove);

			// AimedPos depasse
			Point diff = this.pos.compareTo(this.aimedPos);

			if (diff.x <= this.pixelsTolerence) {
				this.pos.x = this.aimedPos.x;
			}
			if (diff.y <= this.pixelsTolerence) {
				this.pos.y = this.aimedPos.y;
			}
		}
	}

	public void setAimedCenterPos(Point p) {
		this.setAimedPos(p.clone().sub(this.winData.getDefaultWindowSize().clone().div(2)));
	}

	public void setAimedPos(Point ap) {
		this.aimedPos = ap.clone();
	}

	public void update(double dt) {
		this.move(dt);
	}
}
