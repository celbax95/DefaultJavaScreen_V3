package fr.statepanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.inputs.keyboard.Keyboard;
import fr.inputs.mouse.Mouse;
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

	// Taille de la fenetre
	private int WIDTH, HEIGHT;

	// Etat
	private IAppState state;

	public StatePanel(Window w) {
		super();
		assert w != null;
		this.window = w;
		this.WIDTH = 0;
		this.HEIGHT = 0;

		this.setSize(this.WIDTH, this.HEIGHT);
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

	@Override
	public int getHeight() {
		return this.HEIGHT;
	}

	@Override
	public int getWidth() {
		return this.WIDTH;
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
	 * @param width     : largeur de la fenetre
	 * @param height    : hauteur de la fenetre
	 * @param repainter : repainter a utiliser
	 */
	public void init(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;

		// Pour les listners
		this.setFocusable(true);
		this.requestFocusInWindow();

		this.setSize(this.WIDTH, this.HEIGHT);
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
