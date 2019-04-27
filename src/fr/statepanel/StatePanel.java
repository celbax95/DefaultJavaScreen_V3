package fr.statepanel;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.repainter.DefautRepainter;

@SuppressWarnings("serial")
public class StatePanel extends JPanel implements Statable {

	private static StatePanel instance;

	static {
		instance = new StatePanel();
	}

	private int WIDTH, HEIGHT;

	private IAppState state;

	private DefautRepainter repainter;

	private int repaintRequesting;

	private StatePanel() {
		super();
		this.WIDTH = 0;
		this.HEIGHT = 0;

		this.setSize(this.WIDTH, this.HEIGHT);
	}

	public void init(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;

		this.setSize(this.WIDTH, this.HEIGHT);
	}

	/**
	 * Methode appellee par le repainter, pour actualiser l'affichage dans la
	 * fenetre
	 */
	@Override
	public void paintComponent(Graphics g2) {
		super.paintComponent(g2);

		this.setBackground(this.state.getBackgroundColor());

		try {
			this.state.draw((Graphics2D) g2);
		} catch (StateRequest stateRequest) {
			AppStateManager.getInstance().applyState(stateRequest.getState());
		}
	}

	@Override
	public void setState(IAppState state) {
		if (this.state != null) {
			this.state.setActive(false);
		}

		this.repainter = new DefautRepainter(this);
		state.setRepainter(this.repainter);
		this.state = state;
		this.state.setActive(true);
	}

	public static StatePanel getInstance() {
		return instance;
	}
}
