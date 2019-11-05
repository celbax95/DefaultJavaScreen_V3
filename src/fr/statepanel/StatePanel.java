package fr.statepanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import fr.inputs.keyboard.Keyboard;
import fr.inputs.mouse.Mouse;
import fr.logger.Logger;
import fr.util.point.Point;
import fr.window.WinData;
import fr.window.Window;

/**
 * Panel principal pouvant changer d'etat
 *
 * @author Loic.MACE
 *
 */
@SuppressWarnings("serial")
public class StatePanel extends JPanel {

	private Window window;

	private WinData winData;

	private AppStateManager appStateManager;

	// Etat
	private IAppState state;

	public StatePanel(Window w) {
		super();
		assert w != null;
		this.window = w;
		this.winData = w.getWinData();

		this.setSize(this.winData.getScreenSize().ix(), this.winData.getScreenSize().iy());
		this.setBackground(Color.black);
	}

	public void addKeyboardListener(Keyboard listner) {
		super.addKeyListener(listner);
	}

	public void addMouseListener(Mouse listner) {
		super.addMouseListener(listner);
		this.addMouseMotionListener(listner);
		this.addMouseWheelListener(listner);
	}

	public AppStateManager getAppStateManager() {
		return this.appStateManager;
	}

	/**
	 * @return the winData
	 */
	public WinData getWinData() {
		return this.winData;
	}

	/**
	 * @return the window
	 */
	public Window getWindow() {
		return this.window;
	}

	/**
	 * Initialisation du StatePanel
	 *
	 */
	public void init(WinData winData) {
		this.winData = winData;

		// Pour les listners
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.setFocusTraversalKeysEnabled(false);

		this.setSize(this.winData.getWindowSize().ix(), this.winData.getWindowSize().iy());
	}

	@Override
	public void paintComponent(Graphics g2) {
		final Graphics2D g = (Graphics2D) g2;

		super.paintComponent(g);

		Point windowRatio = this.winData.getWindowRatio();

		g.scale(windowRatio.x, windowRatio.y);

		Object antialias = this.winData.getAntialiasing();

		if (antialias.equals(RenderingHints.VALUE_ANTIALIAS_OFF)) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		} else {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, antialias);
		}

		g.setRenderingHint(RenderingHints.KEY_RENDERING, this.winData.getRendering());
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, this.winData.getColorRendering());
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, this.winData.getAlphaInterpolation());

		this.state.draw(g);
	}

	public void removeKeyboardListener(Keyboard listner) {
		super.removeKeyListener(listner);
	}

	public void removeMouseListener(Mouse listner) {
		super.removeMouseListener(listner);
		this.removeMouseMotionListener(listner);
		this.removeMouseWheelListener(listner);
	}

	public void setAppStateManager(AppStateManager appStateManager) {
		this.appStateManager = appStateManager;
	}

	public void setState(IAppState state) {
		if (this.state != null) {
			this.state.stop();
		}

		this.state = state;
		this.state.start(this);

		Logger.inf("Application du state \"" + state.getName() + "\"");
	}
}
