package fr.screen;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Screen extends JFrame {

	private static Screen instance;

	static {
		instance = new Screen();
	}

	private boolean initialized;

	private JPanel mainJpanel;

	private Screen() {
		this.mainJpanel = null;
		this.initialized = false;
	}

	public void init(JPanel mainJpanel, int marginRight, int marginBottom, int marginTotal) {

		if (mainJpanel == null)
			throw new IllegalArgumentException("L'argument 1 : mainJpanel ne doit pas etre null");

		(this.mainJpanel = mainJpanel).setLocation(marginTotal, marginTotal);

		// Quand on ferme la fenetre
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				instance.dispose();
			}
		});

		// Proprietes de la fenetres
		this.setResizable(false);
		this.setSize(marginRight + mainJpanel.getWidth() + marginTotal * 2,
				marginBottom + mainJpanel.getHeight() + marginTotal * 2);
		this.setLocationRelativeTo(null);

		// Clavier
//		this.setFocusable(true);
//		this.requestFocusInWindow();
//		this.addKeyListener(new KeyBoardHolder());

		// Interieur de la fenetre
		JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.setBackground(Color.black);
		jp.add(this.mainJpanel);
		this.setContentPane(jp);

		this.initialized = true;
	}

	public void start() throws IllegalStateException {

		if (!this.initialized)
			throw new IllegalStateException("Utilisez la methode init() pour initialiser l'instance Screen.");

		this.setVisible(true);
	}

	public static Screen getInstance() {
		return instance;
	}
}
