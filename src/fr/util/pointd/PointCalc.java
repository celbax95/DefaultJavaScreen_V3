package fr.util.pointd;

public class PointCalc {
	private PointCalc() {
	};

	public double getAngle(PointD p) {
		double a = Math.acos(p.x);
		if (p.y < 0)
			a *= -1;
		return (a + Math.PI * 2) % (Math.PI * 2);
	}

	public static PointD add(PointD p1, double p2) {
		return new PointD(p1.x + p2, p1.y + p2);
	}

	public static PointD add(PointD p1, PointD p2) {
		return new PointD(p1.x + p2.x, p1.y + p2.y);
	}

	public static PointD compare(PointD p1, PointD p2) {
		return new PointD(p2.x - p1.x, p2.y - p1.y);
	}

	public static PointD dip(PointD p1, double p2) {
		return new PointD(p1.x / p2, p1.y / p2);
	}

	public static double distance(PointD p1, PointD p2) {
		PointD o = compare(p1, p2);
		return Math.sqrt(Math.pow(o.x, 2) + Math.pow(o.y, 2));
	}

	public static PointD div(PointD p1, PointD p2) {
		return new PointD(p1.x / p2.x, p1.y / p2.y);
	}

	public static PointD getNormalized(PointD p) {
		double tmp = Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
		return new PointD(p.x / tmp, p.y / tmp);
	}

	public static PointD mod(PointD p1, double p2) {
		return new PointD(p1.x % p2, p1.y % p2);
	}

	public static PointD mod(PointD p1, PointD p2) {
		return new PointD(p1.x % p2.x, p1.y % p2.y);
	}

	public static PointD mult(PointD p1, double p2) {
		return new PointD(p1.x * p2, p1.y * p2);
	}

	public static PointD mult(PointD p1, PointD p2) {
		return new PointD(p1.x * p2.x, p1.y * p2.y);
	}

	public static PointD rot(PointD p, double angle) {
		angle = (angle + Math.PI * 2) % (Math.PI * 2);
		return rot(p, Math.cos(angle), Math.sin(angle));
	}

	public static PointD rot(PointD p, double cos, double sin) {
		return new PointD(p.x * cos + p.y * -sin, p.x * sin + p.y * cos);
	}

	public static double scalar(PointD p1, PointD p2) {
		return p1.x * p2.x + p1.y * p2.y;
	}

	public static PointD sub(PointD p1, double p2) {
		return new PointD(p1.x - p2, p1.y - p2);
	}

	public static PointD sub(PointD p1, PointD p2) {
		return new PointD(p1.x - p2.x, p1.y - p2.y);
	}
}
