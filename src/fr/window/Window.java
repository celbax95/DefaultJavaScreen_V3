package fr.window;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

@SuppressWarnings("serial")
public class Window extends JFrame {

	private static Window instance;

	static {
		instance = new Window();
	}

	private boolean initialized;

	private JPanel mainPanel;

	private Window() {
		this.mainPanel = null;
		this.initialized = false;
	}

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

		// Clavier
//		this.setFocusable(true);
//		this.requestFocusInWindow();
//		this.addKeyListener(new KeyBoardHolder());

		// Interieur de la fenetre
		final JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.setBackground(Color.black);
		jp.add(this.mainPanel);
		this.setContentPane(jp);

		this.initialized = true;
	}

	public void start() throws IllegalStateException {

		if (!this.initialized)
			throw new IllegalStateException("Utilisez la methode init() pour initialiser l'instance Screen.");

		this.setVisible(true);
	}

	public static Window getInstance() {
		return instance;
	}
}
