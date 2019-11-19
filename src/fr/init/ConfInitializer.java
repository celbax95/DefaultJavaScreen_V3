package fr.init;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.state.game.GameState;
import fr.state.loading.LoadingState;
import fr.state.menu.MenuState;
import fr.statepanel.AppStateManager;
import fr.statepanel.IAppState;
import fr.statepanel.StatePanel;
import fr.util.point.Point;
import fr.window.WinData;
import fr.window.Window;
import fr.xmlmanager.XMLManagerDOM;

public class ConfInitializer {

	// Fichiers de configuration
	private DatafilesManager dfm;

	public ConfInitializer() {
		this.dfm = DatafilesManager.getInstance();
	}

	/**
	 * Recuperation de la racine du fichier xml de configuration de la fenetre
	 */
	private Object getWinConf() {
		Object winConf = null;
		winConf = this.dfm.getFile("winConf");

		return winConf;
	}

	private WinData getWinData(Object winConf) {
		XMLManager manager = this.dfm.getXmlManager();

		WinData w = new WinData();

		int width = (int) manager.getParam(winConf, "width", 1366);

		int height = (int) manager.getParam(winConf, "height", 768);

		w.setWindowSize(new Point(width, height));

		w.setMarginTop((int) manager.getParam(winConf, "marginTop", 0));
		w.setMarginBottom((int) manager.getParam(winConf, "marginBottom", 0));
		w.setMarginLeft((int) manager.getParam(winConf, "marginLeft", 0));
		w.setMarginRight((int) manager.getParam(winConf, "marginRight", 0));
		w.setMargin((int) manager.getParam(winConf, "margin", 0));

		w.setFullscreen((boolean) manager.getParam(winConf, "fullscreen", false));
		w.setBorderless((boolean) manager.getParam(winConf, "borderless", false));

		w.setAntialiasing((int) manager.getParam(winConf, "antialiasing", 0));

		w.setRendering((int) manager.getParam(winConf, "rendering", 0));
		w.setColorRendering((int) manager.getParam(winConf, "colorRendering", 0));
		w.setAlphaInterpolation((int) manager.getParam(winConf, "alphaInterpolation", 0));

		return w;
	}

	/**
	 * Ajout des fichiers de configuration
	 */
	private void initConfFiles() {
		this.dfm = DatafilesManager.getInstance();
		this.dfm.init(new XMLManagerDOM());

		ConfFiles.initConfFiles(this.dfm);

	}

	private void initStates(AppStateManager asm) {
		asm.addState(new MenuState());
		asm.addState(new LoadingState());
		asm.addState(new GameState());
	}

	private void loadFonts() {
		try {
			Font customFont = Font.createFont(Font.TRUETYPE_FONT,
					this.getClass().getResourceAsStream("/resources/font/ITCKRIST.ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
			customFont = Font.createFont(Font.TRUETYPE_FONT,
					this.getClass().getResourceAsStream("/resources/font/Copperplate Gothic.ttf"));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creation des elements a partir de la conf
	 */
	public void start() {
		this.loadFonts();

		this.initConfFiles();

		Window screen = this.windowInitializers(this.getWinConf());

		this.windowStart(screen);
	}

	/**
	 * Creation de l'ecran
	 *
	 * @param winConf : racine du fichier de configuration
	 */
	private Window windowInitializers(Object winConf) {
		final Window screen = new Window();
		final StatePanel mainPanel = new StatePanel(screen);
		final WinData winData = this.getWinData(winConf);
		final AppStateManager asm = new AppStateManager();
		this.initStates(asm);

		mainPanel.init(winData);
		mainPanel.setAppStateManager(asm);

		/*
		 * id
		 *
		 * players Data
		 */

		int nb = 2;

		int myId = 0;

		Map<Integer, Map<String, Object>> playersData = new HashMap<>();

		for (int i = 0; i < nb; i++) {
			Map<String, Object> p = new HashMap<>();
			p.put("pos", new Point(200 + 250 * i, 300));
			p.put("color", Color.red);

			playersData.put(i, p);
		}

		Map<String, Object> initData = new HashMap<>();
		initData.put("myId", myId);
		initData.put("playersData", playersData);

		IAppState state = asm.getState("game");

		state.setInitData(initData);

		mainPanel.setState(state);

		screen.init(mainPanel, winData);

		return screen;
	}

	/**
	 * Lance l'affichage de la fenetre
	 *
	 * @param configRoot : racine du fichier de configuration
	 */
	private void windowStart(Window screen) {
		screen.start();
	}
}
