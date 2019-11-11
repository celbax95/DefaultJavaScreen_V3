package fr.window;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import fr.util.point.Point;

public class WinData {

	private static final Object[] antialiasingValues = {
			RenderingHints.VALUE_ANTIALIAS_OFF,
			RenderingHints.VALUE_INTERPOLATION_BICUBIC,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR,
			RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR };

	private static final Object[] renderingValues = {
			RenderingHints.VALUE_RENDER_DEFAULT,
			RenderingHints.VALUE_RENDER_SPEED,
			RenderingHints.VALUE_RENDER_QUALITY };

	private static final Object[] colorRenderingValues = {
			RenderingHints.VALUE_COLOR_RENDER_DEFAULT,
			RenderingHints.VALUE_COLOR_RENDER_SPEED,
			RenderingHints.VALUE_COLOR_RENDER_QUALITY };

	private static final Object[] alphaInterpolationValues = {
			RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT,
			RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED,
			RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY };

	private static final Point DEFAULT_WINDOW_SIZE = new Point(1920, 1080);

	private Point windowRatio;

	private Point invWindowRatio;
	private Point windowSize;

	private Point tmpWindowSize;

	private Point halfWindowSize;

	private Point screenSize;

	private Point fullscreenPadding;
	private int margin, marginLeft, marginRight, marginTop, marginBottom;

	private boolean fullscreen;

	private boolean borderless;
	private int antialiasing;

	private int rendering;

	private int colorRendering;

	private int alphaInterpolation;

	private boolean needRestart;

	public WinData() {
		super();
		this.windowSize = new Point();
		// Si on est en plein ecran, tmpWindowSize sert de zone tampon
		this.tmpWindowSize = new Point();
		this.halfWindowSize = new Point();

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		Point t = new Point(d.width, d.height);
		if (Math.abs(t.x / t.y - DEFAULT_WINDOW_SIZE.x / DEFAULT_WINDOW_SIZE.y) > 0.1) {
			if (DEFAULT_WINDOW_SIZE.x / t.x < DEFAULT_WINDOW_SIZE.y / t.y) {
				t.x = t.y * DEFAULT_WINDOW_SIZE.x / DEFAULT_WINDOW_SIZE.y;
			} else {
				t.y = t.x * DEFAULT_WINDOW_SIZE.y / DEFAULT_WINDOW_SIZE.x;
			}
		}
		this.fullscreenPadding = new Point(d.width - t.ix(), d.getHeight() - t.iy()).div(2);
		this.screenSize = new Point(t);

		this.windowRatio = new Point(1, 1);
		this.invWindowRatio = new Point();
		this.setInvWindowRatio();
		this.margin = 0;
		this.marginTop = 0;
		this.marginBottom = 0;
		this.marginLeft = 0;
		this.marginRight = 0;
		this.fullscreen = false;
		this.borderless = false;
		this.antialiasing = 0;
		this.rendering = 0;
		this.colorRendering = 0;
		this.alphaInterpolation = 0;
	}

	public Object getAlphaInterpolation() {
		return alphaInterpolationValues[this.alphaInterpolation];
	}

	public Object getAntialiasing() {
		return antialiasingValues[this.antialiasing];
	}

	public Object getColorRendering() {
		return colorRenderingValues[this.colorRendering];
	}

	public Point getDefaultWindowSize() {
		return DEFAULT_WINDOW_SIZE;
	}

	/**
	 * @return the halfWindowSize
	 */
	public Point getHalfWindowSize() {
		return this.halfWindowSize;
	}

	public Point getInvWindowRatio() {
		return this.invWindowRatio;
	}

	/**
	 * @return the margin
	 */
	public int getMargin() {
		return this.margin;
	}

	/**
	 * @return the marginBottom
	 */
	public int getMarginBottom() {
		// 36 est la taille ajoutée par la bordure de la fenetre windows
		return this.marginBottom + (this.borderless ? 0 : 36);
	}

	/**
	 * @return the marginLeft
	 */
	public int getMarginLeft() {
		return this.marginLeft + (this.fullscreen ? this.fullscreenPadding.ix() : 0);
	}

	/**
	 * @return the marginRight
	 */
	public int getMarginRight() {
		// 6 est la taille ajoutée par la bordure de la fenetre windows
		return this.marginRight + (this.borderless ? 0 : 6);
	}

	/**
	 * @return the marginTop
	 */
	public int getMarginTop() {
		return this.marginTop + (this.fullscreen ? this.fullscreenPadding.iy() : 0);
	}

	public Object getRendering() {
		return renderingValues[this.rendering];
	}

	/**
	 * @return the screenSize
	 */
	public Point getScreenSize() {
		return this.screenSize;
	}

	/**
	 * @return the windowRatio
	 */
	public Point getWindowRatio() {
		return this.windowRatio;
	}

	/**
	 * @return the windowSize
	 */
	public Point getWindowSize() {
		return this.windowSize;
	}

	/**
	 * @return the borderless
	 */
	public boolean isBorderless() {
		return this.borderless;
	}

	/**
	 * @return the fullScreen
	 */
	public boolean isFullscreen() {
		return this.fullscreen;
	}

	public boolean isNeedRestart() {
		return this.needRestart;
	}

	public void setAlphaInterpolation(int alphaInterpolation) {
		assert alphaInterpolation >= 0 && alphaInterpolation < alphaInterpolationValues.length;

		this.alphaInterpolation = alphaInterpolation;
	}

	public void setAntialiasing(int antialiasing) {
		assert antialiasing >= 0 && antialiasing < antialiasingValues.length;

		this.antialiasing = antialiasing;
	}

	/**
	 * @param borderless the borderless to set
	 */
	public void setBorderless(boolean borderless) {
		this.borderless = borderless;
	}

	public void setColorRendering(int colorRendering) {
		assert colorRendering >= 0 && colorRendering < colorRenderingValues.length;

		this.colorRendering = colorRendering;
	}

	/**
	 * @param fullScreen the fullScreen to set
	 */
	public void setFullscreen(boolean fullScreen) {
		if (fullScreen != this.fullscreen) {
			this.fullscreen = fullScreen;
			if (fullScreen) {
				this.windowRatio = this.screenSize.clone().div(DEFAULT_WINDOW_SIZE);
				this.setInvWindowRatio();
				this.tmpWindowSize.set(this.windowSize);
				this.windowSize.set(this.screenSize);
				this.halfWindowSize.set(this.windowSize.clone().div(2));
			} else {
				this.windowSize.set(this.tmpWindowSize);
				this.windowRatio = this.windowSize.clone().div(DEFAULT_WINDOW_SIZE);
				this.setInvWindowRatio();
			}
		}
	}

	private void setInvWindowRatio() {
		this.invWindowRatio.set(1 / this.windowRatio.x, 1 / this.windowRatio.y);
	}

	/**
	 * @param margin the margin to set
	 */
	public void setMargin(int margin) {
		this.margin = margin;
	}

	/**
	 * @param marginBottom the marginBottom to set
	 */
	public void setMarginBottom(int marginBottom) {
		this.marginBottom = marginBottom;
	}

	/**
	 * @param marginLeft the marginLeft to set
	 */
	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	/**
	 * @param marginRight the marginRight to set
	 */
	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}

	/**
	 * @param marginTop the marginTop to set
	 */
	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	public void setNeedRestart(boolean needRestart) {
		this.needRestart = needRestart;
	}

	public void setRendering(int rendering) {
		assert rendering >= 0 && rendering < renderingValues.length;

		this.rendering = rendering;
	}

	/**
	 * @param windowSize the windowSize to set
	 */
	public void setWindowSize(Point windowSize) {
		this.tmpWindowSize.set(windowSize);
		if (!this.fullscreen) {
			this.windowSize.set(this.tmpWindowSize);
			this.windowRatio.set(this.windowSize.clone().div(DEFAULT_WINDOW_SIZE));
			this.setInvWindowRatio();
			this.halfWindowSize.set(this.windowSize.clone().div(2));
		}
	}
}
