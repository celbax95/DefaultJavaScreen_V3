package fr.util.pointd;

public class PointD {
	public double x;
	public double y;

	public PointD() {
		x = 0;
		y = 0;
	}

	public PointD(double x, double y) {
		this();
		this.x = x;
		this.y = y;
	}

	public PointD(PointD p) {
		this(p.x, p.y);
	}

	public PointD clone() {
		return new PointD(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PointD other = (PointD) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	public int getIntX() {
		return (int) x;
	}

	public int getIntY() {
		return (int) y;
	}

	public PointD getLocation() {
		return this;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setLocation(PointD p) {
		this.x = p.x;
		this.y = p.y;
	}

	@Override
	public String toString() {
		return "PointD [x=" + x + ", y=" + y + "]";
	}

	public void translate(double dx, double dy) {
		x += dx;
		y += dy;
	}
}
