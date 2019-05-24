package fr.statepanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.keyboard.KeyBoardHolder;
import fr.mouse.MouseHolder;

/**
 * Panel principal pouvant changer d'etat
 *
 * @author Loic.MACE
 *
 */
@SuppressWarnings("serial")
public class StatePanel extends JPanel implements Statable, HardwareListner {

	// Instance unique de StatePanel
	private static StatePanel instance;

	static {
		instance = new StatePanel();
	}

	// Taille de la fenetre
	private int WIDTH, HEIGHT;

	// Etat
	private IAppState state;

	// Repainter servant a repaint
	private Repainter repainter;

	private StatePanel() {
		super();
		this.WIDTH = 0;
		this.HEIGHT = 0;

		this.setSize(this.WIDTH, this.HEIGHT);
		this.setBackground(Color.black);
	}

	/**
	 * Initialisation du StatePanel
	 *
	 * @param width     : largeur de la fenetre
	 * @param height    : hauteur de la fenetre
	 * @param repainter : repainter a utiliser
	 */
	public void init(int width, int height, Repainter repainter) {
		this.WIDTH = width;
		this.HEIGHT = height;

		this.repainter = repainter;
		this.repainter.setPanel(this);

		// Pour les listners
		this.setFocusable(true);
		this.requestFocusInWindow();

		this.setAllListnersEnabeled(true);

		this.setSize(this.WIDTH, this.HEIGHT);
	}

	/**
	 * Methode appellee par le repainter, pour actualiser l'affichage dans la
	 * fenetre
	 */
	@Override
	public void paintComponent(Graphics g2) {
		final Graphics2D g = (Graphics2D) g2;
		super.paintComponent(g);

		g.setColor(this.state.getBackgroundColor());
		g.fillRect(0, 0, this.WIDTH, this.HEIGHT);
		g.setColor(Color.black);

		try {
			this.state.draw(g);
		} catch (final StateRequest stateRequest) {
			AppStateManager.getInstance().applyState(stateRequest.getState());
		}
	}

	/**
	 * Change l'etat da'ctivation de tous les listners
	 *
	 * @param activation : nouvel etat d'activation
	 */
	@Override
	public void setAllListnersEnabeled(boolean activation) {
		this.setKeyboardEnabeled(activation);
		this.setMouseClicksEnabeled(activation);
		this.setMouseMovesEnabeled(activation);
		this.setMouseWheelEnabeled(activation);
	}

	@Override
	public void setKeyboardEnabeled(boolean activation) {
		if (activation) {
			this.addKeyListener(new KeyBoardHolder());
		} else {
			this.addKeyListener(null);
		}
	}

	@Override
	public void setMouseClicksEnabeled(boolean activation) {
		if (activation) {
			this.addMouseListener(new MouseHolder());
		} else {
			this.addMouseListener(null);
		}
	}

	@Override
	public void setMouseMovesEnabeled(boolean activation) {
		if (activation) {
			this.addMouseMotionListener(new MouseHolder());
		} else {
			this.addMouseMotionListener(null);
		}
	}

	@Override
	public void setMouseWheelEnabeled(boolean activation) {
		if (activation) {
			this.addMouseWheelListener(new MouseHolder());
		} else {
			this.addMouseWheelListener(null);
		}
	}

	@Override
	public void setState(IAppState state) {
		if (this.state != null) {
			this.state.setActive(false);
		}

		this.state = state;
		this.state.setRepainter(this.repainter);
		this.state.setActive(true);
	}

	/**
	 * Recupere l'instance unique de StatePanel
	 *
	 * @return l'instance unique de StatePanel
	 */
	public static StatePanel getInstance() {
		return instance;
	}
}
