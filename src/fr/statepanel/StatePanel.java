package fr.statepanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.inputs.keyboard.Keyboard;
import fr.inputs.mouse.Mouse;
import fr.window.WinData;
import fr.window.Window;

/**
 * Panel principal pouvant changer d'etat
 *
 * @author Loic.MACE
 *
 */
@SuppressWarnings("serial")
public class StatePanel extends JPanel implements Statable {

	private Window window;

	private WinData winData;

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

		this.setSize(this.winData.getScreenSize().ix(), this.winData.getScreenSize().iy());
	}

	@Override
	public void paintComponent(Graphics g2) {
		final Graphics2D g = (Graphics2D) g2;

		super.paintComponent(g);

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

	@Override
	public void setState(IAppState state) {
		if (this.state != null) {
			this.state.stop();
		}

		this.state = state;
		this.state.start(this);
	}
}
