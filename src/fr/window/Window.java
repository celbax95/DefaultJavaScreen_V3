package fr.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

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

	public Window() {
		this.mainPanel = null;
		this.initialized = false;
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
	public void init(JPanel mainJpanel, int marginRight, int marginBottom, int marginTotal) {

		if (mainJpanel == null)
			throw new IllegalArgumentException("L'argument 1 : mainJpanel ne doit pas etre null");

		(this.mainPanel = mainJpanel).setLocation(marginTotal, marginTotal);

		// Quand on ferme la fenetre
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Proprietes de la fenetres
		this.setResizable(false);
		this.setSize(marginRight + mainJpanel.getWidth() + marginTotal * 2,
				marginBottom + mainJpanel.getHeight() + marginTotal * 2);
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

		this.setVisible(true);
	}
}
