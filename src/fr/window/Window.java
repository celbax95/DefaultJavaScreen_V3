package fr.window;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import fr.logger.Logger;

/**
 * Classe representant une fenetre
 *
 * @author Loic.MACE
 *
 */
public class Window extends JFrame {

	private static final long serialVersionUID = 1L;

	// Si Window est initialise
	private boolean initialized;

	// Panel principal de la fenetre
	private JPanel mainPanel;

	private WinData winData;

	public Window() {
		this.mainPanel = null;
		this.initialized = false;
		this.winData = new WinData();
	}

	public void close() {
		Logger.inf("Fermeture de l'application.");
		this.setVisible(false);
		super.dispose();
		System.exit(0);
	}

	/**
	 * @return the winData
	 */
	public WinData getWinData() {
		return this.winData;
	}

	/**
	 * Initialisation de la fenetre
	 *
	 * @param mainJpanel   : panel principal de la fenetre
	 * @param marginRight  : marge sur le bord a droite de la fenetre
	 * @param marginBottom : marge sur le bord en bas de la fenetre
	 * @param marginTotal  : marge sur les bord de la fenetre (en plus des marge en
	 *                     bas et a droite)
	 */
	public void init(JPanel mainJpanel, WinData winData) {

		if (mainJpanel == null)
			throw new IllegalArgumentException("L'argument 1 : mainJpanel ne doit pas etre null");

		if (this.winData != null) {
			this.winData = winData;
		}

		this.dispose();

		(this.mainPanel = mainJpanel).setLocation(this.winData.getMargin() + this.winData.getMarginLeft(),
				this.winData.getMargin() + this.winData.getMarginTop());

		// Quand on ferme la fenetre
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Window.this.dispose();
				System.exit(0);
			}
		});

		// Proprietes de la fenetres
		this.setResizable(false);

		if (this.winData.isFullScreen()) {
			this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
			this.setUndecorated(true);
		} else {
			this.setSize(
					this.winData.getMarginLeft() + this.winData.getMarginRight() + mainJpanel.getWidth()
							+ this.winData.getMargin() * 2,
					this.winData.getMarginTop() + this.winData.getMarginBottom() + mainJpanel.getHeight()
							+ this.winData.getMargin() * 2);
			this.setUndecorated(this.winData.isBorderless());
		}

		this.setLocationRelativeTo(null);

		// Interieur de la fenetre
		final JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.setBackground(Color.black);
		jp.add(this.mainPanel);
		this.setContentPane(jp);

		this.initialized = true;
	}

	/**
	 * Demarre et affiche la fenetre
	 *
	 * @throws IllegalStateException
	 *
	 * @see init
	 */
	public void start() throws IllegalStateException {

		if (!this.initialized)
			throw new IllegalStateException("Utilisez la methode init() pour initialiser l'instance Screen.");

		Logger.inf("Lancement de l'application.");

		this.setVisible(true);
	}
}
