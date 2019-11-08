package fr.state.menu.page;

import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.drawelements.DEImage;
import fr.util.point.Point;

public class MenuSettings implements MenuPage {

	private static final String[] RES_NAMES = {
			"title",
			"graphStd",
			"graphPressed",
			"controlsStd",
			"controlsPressed",
			"profileStd",
			"profilePressed",
			"gameSettingsStd",
			"gameSettingsPressed",
			"backStd",
			"backPressed", };

	private static final String[] RES_PATHS = {
			"title",
			"graphStd",
			"graphPressed",
			"controlsStd",
			"controlsPressed",
			"profileStd",
			"profilePressed",
			"gameSettingsStd",
			"gameSettingsPressed",
			"backStd",
			"backPressed", };

	private static final String RES_FOLDER = "/resources/menu/menuSettings/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menu/settings";

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private boolean loaded;

	private List<Widget> widgets;
	private Menu m;

	public MenuSettings(Menu m) {
		this.loaded = false;
		this.m = m;
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
	}

	@Override
	public boolean isLoaded() {
		return this.loaded;
	}

	@Override
	public void load() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				MenuSettings.this.widgets = new Vector<>();

				MenuSettings.this.loadResources();

				MenuSettings.this.wTitle();
				MenuSettings.this.wBack();
				MenuSettings.this.wGraph();
				MenuSettings.this.wControls();
				MenuSettings.this.wProfile();
				MenuSettings.this.wGameSettings();

				MenuSettings.this.loaded = true;
			}
		});
		thread.setName("MenuSettings/load");
		thread.start();
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
	}

	@Override
	public void update(Input input) {
		for (Widget w : this.widgets) {
			w.update(input);
		}
	}

	private void wBack() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				MenuSettings.this.m.applyPage(new MenuMain(MenuSettings.this.m));
			}
		};

		btn.setPos(new Point(42, 42));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wControls() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				MenuSettings.this.m.applyPage(new MenuControls(MenuSettings.this.m));
			}
		};

		btn.setPos(new Point(554, 451));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/controlsStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/controlsPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wGameSettings() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				MenuSettings.this.m.applyPage(new MenuGameSettings(MenuSettings.this.m));
			}
		};

		btn.setPos(new Point(1465, 451));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/gameSettingsStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/gameSettingsPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wGraph() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				MenuSettings.this.m.applyPage(new MenuGraphics(MenuSettings.this.m));
			}
		};

		btn.setPos(new Point(98, 451));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/graphStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/graphPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wProfile() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				MenuSettings.this.m.applyPage(new MenuProfile(MenuSettings.this.m));
			}
		};

		btn.setPos(new Point(1009, 451));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/profileStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/profilePressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(550, 76));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/title"));

		title.setDrawElement(i);

		this.widgets.add(title);
	}
}
