package fr.screen;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.screen.keyboard.KeyBoard;

@SuppressWarnings("serial")
public class Screen extends JFrame {

	private static Screen single;

	private Screen(ScreenApp scrApp, int w, int h, int mx, int my, int m) {
		if (scrApp != null)
			this.setTitle(scrApp.getScreenTitle());
		else
			this.setTitle("DefaultName");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(mx + w + m * 2, my + h + m * 2);
		this.setLocationRelativeTo(null);

		// Clavier
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(new KeyBoard());

		JPanel jp = new JPanel();
		jp.setLayout(null);
		jp.setBackground(Color.black);
		jp.add(Root.create(scrApp, w, h, m));
		this.setContentPane(jp);
		this.setVisible(true);
	}

	public void addScreenApp(ScreenApp scrApp) {
		Root.addScreenApp(scrApp);
		single.setName(scrApp.getScreenTitle());
	}

	public static Screen create(ScreenApp scrApp, int w, int h, int mx, int my, int m) {
		if (single == null) {
			single = new Screen(scrApp, w, h, mx, my, m);
			return single;
		} else
			return null;
	}

}
