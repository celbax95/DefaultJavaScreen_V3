package fr.util.point;

public class Point {

	/* VARIABLES */

	private double x;
	private double y;

	/* CONSTRUCTORS */

	// Empty
	public Point() {
		this.x = 0;
		this.y = 0;
	}

	// Full
	public Point(double x, double y) {
		this();
		this.x = x;
		this.y = y;
	}

	public Point(Point p) {
		this();
		this.x = p.x;
		this.y = p.y;
	}

	/* METHODS */

	/**
	 * Ajoute respectivement les coordonees de p au Point courant
	 */
	public Point add(Point p) {
		this.x += p.x;
		this.y += p.y;
		return this;
	}

	/**
	 * Calcul de la distance entre le point courant et le point p
	 */
	public double distance(Point p) {
		final Point o = new Point(p).sub(this);
		return Math.sqrt(o.x * o.x + o.y * o.y);
	}

	/**
	 * Divise les coordonnées du Point courant par factor
	 */
	public Point div(double factor) {
		this.x /= factor;
		this.y /= factor;
		return this;
	}

	/**
	 * Divise respectivement les coordonees de p au Point courant
	 */
	public Point div(Point p) {
		this.x /= p.x;
		this.y /= p.y;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Point other = (Point) obj;
		if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	/**
	 * Calcule l'angle du vecteur forme par les coordonees du Point
	 */
	public double getAngle() {
		return Math.acos(this.x) + (this.y < 0 ? Math.PI : 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(this.x);
		result = prime * result + (int) (temp ^ temp >>> 32);
		temp = Double.doubleToLongBits(this.y);
		result = prime * result + (int) (temp ^ temp >>> 32);
		return result;
	}

	/* METHODS */

	/**
	 * Getter pour x avec un cast vers int
	 */
	public int ix() {
		return (int) Math.round(this.x);
	}

	/**
	 * Getter pour y avec un cast vers int
	 */
	public int iy() {
		return (int) Math.round(this.y);
	}

	/**
	 * Multiplie les coordonnees du Point courant par factor
	 */
	public Point mult(double factor) {
		this.x *= factor;
		this.y *= factor;
		return this;
	}

	/**
	 * Multiplie respectivement les coordonees de p au Point courant
	 */
	public Point mult(Point p) {
		this.x *= p.x;
		this.y *= p.y;
		return this;
	}

	/**
	 * Pivote le vecteur de x degres en radian (sens horaire)
	 */
	public void rotate(double angle) {

		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);

		this.x = this.x * cos + this.y * -sin;
		this.y = this.x * sin + this.y * cos;
	}

	/**
	 * Calcul du produit scalaire
	 */
	public double scalar(Point p) {
		return this.x * p.x + this.y * p.y;
	}

	/**
	 * Calque les coordonnees de p sur le Point courant
	 */
	public void set(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	/**
	 * Soustrait respectivement les coordonees de p au Point courant
	 */
	public Point sub(Point p) {
		this.x -= p.x;
		this.y -= p.y;
		return this;
	}

	@Override
	public String toString() {
		return "Point [x=" + this.x + ", y=" + this.y + "]";
	}

	/**
	 * Modifie les coodonnees du Point pour que le vecteur forme par ses coordonees
	 * soit egal a 1
	 */
	public Point trigNorm() {
		final double angle = this.getAngle();
		this.x = Math.cos(angle);
		this.y = Math.sin(angle);
		return this;
	}

	public double x() {
		return this.x;
	}

	public void x(double x) {
		this.x = x;
	}

	public double y() {
		return this.y;
	}

	public void y(double y) {
		this.y = y;
	}
}