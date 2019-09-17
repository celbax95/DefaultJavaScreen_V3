package fr.window;

import java.awt.Dimension;
import java.awt.Toolkit;

import fr.util.point.Point;

public class WinData {

	enum Aliasing {
		DISABLE, BICUBIC, BILINEAR, NEAREST_NEIGHBOR,
	}

	enum Rendering {
		FAST, QUALITY, DEFAULT,
	}

	private static final Aliasing[] aliasingValues;

	private static final Rendering[] renderingValues;

	static {
		aliasingValues = Aliasing.values();
		renderingValues = Rendering.values();
	}

	/**
	 * @return the aliasingValues
	 */
	public static Aliasing[] getAliasingValues() {
		return aliasingValues;
	}

	/**
	 * @return the renderingValues
	 */
	public static Rendering[] getRenderingValues() {
		return renderingValues;
	}

	private Point windowSize;

	private Point halfWindowSize;

	private Point screenSize;
	private int margin, marginLeft, marginRight, marginTop, marginBottom;

	private boolean fullScreen;

	private boolean borderless;

	private Aliasing antialiasing;

	private Rendering rendering;

	private Rendering colorRendering;

	private Rendering alphaInterpolation;

	public WinData() {
		super();
		this.setWindowSize(new Point());
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenSize = new Point(d.width, d.height);
		this.margin = 0;
		this.marginTop = 0;
		this.marginBottom = 0;
		this.marginLeft = 0;
		this.marginRight = 0;
		this.fullScreen = false;
		this.borderless = false;
		this.antialiasing = Aliasing.BILINEAR;
		this.rendering = Rendering.DEFAULT;
		this.colorRendering = Rendering.DEFAULT;
		this.alphaInterpolation = Rendering.DEFAULT;
	}

	/**
	 * @return the alphaInterpolation
	 */
	public Rendering getAlphaInterpolation() {
		return this.alphaInterpolation;
	}

	/**
	 * @return the antialiasing
	 */
	public Aliasing getAntialiasing() {
		return this.antialiasing;
	}

	/**
	 * @return the colorRendering
	 */
	public Rendering getColorRendering() {
		return this.colorRendering;
	}

	/**
	 * @return the halfWindowSize
	 */
	public Point getHalfWindowSize() {
		return this.halfWindowSize;
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
		return this.marginBottom;
	}

	/**
	 * @return the marginLeft
	 */
	public int getMarginLeft() {
		return this.marginLeft;
	}

	/**
	 * @return the marginRight
	 */
	public int getMarginRight() {
		return this.marginRight;
	}

	/**
	 * @return the marginTop
	 */
	public int getMarginTop() {
		return this.marginTop;
	}

	/**
	 * @return the rendering
	 */
	public Rendering getRendering() {
		return this.rendering;
	}

	/**
	 * @return the screenSize
	 */
	public Point getScreenSize() {
		return this.screenSize;
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
	public boolean isFullScreen() {
		return this.fullScreen;
	}

	public void setAlphaInterpolation(int rendering) {
		assert rendering < renderingValues.length;

		this.alphaInterpolation = renderingValues[rendering];
	}

	/**
	 * @param alphaInterpolation the alphaInterpolation to set
	 */
	public void setAlphaInterpolation(Rendering alphaInterpolation) {
		this.alphaInterpolation = alphaInterpolation;
	}

	/**
	 * @param antialiasing the antialiasing to set
	 */
	public void setAntialiasing(Aliasing antialiasing) {
		this.antialiasing = antialiasing;
	}

	public void setAntialiasing(int aliasing) {
		assert aliasing < aliasingValues.length;

		this.antialiasing = aliasingValues[aliasing];
	}

	/**
	 * @param borderless the borderless to set
	 */
	public void setBorderless(boolean borderless) {
		this.borderless = borderless;
	}

	public void setColorRendering(int rendering) {
		assert rendering < renderingValues.length;

		this.colorRendering = renderingValues[rendering];
	}

	/**
	 * @param colorRendering the colorRendering to set
	 */
	public void setColorRendering(Rendering colorRendering) {
		this.colorRendering = colorRendering;
	}

	/**
	 * @param fullScreen the fullScreen to set
	 */
	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
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

	public void setRendering(int rendering) {
		assert rendering < renderingValues.length;

		this.rendering = renderingValues[rendering];
	}

	/**
	 * @param rendering the rendering to set
	 */
	public void setRendering(Rendering rendering) {
		this.rendering = rendering;
	}

	/**
	 * @param windowSize the windowSize to set
	 */
	public void setWindowSize(Point windowSize) {
		this.windowSize.set(windowSize);
		this.halfWindowSize = windowSize.clone().div(2);
	}
}
