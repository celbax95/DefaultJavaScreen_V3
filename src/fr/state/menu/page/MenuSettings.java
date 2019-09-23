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

	private static final String[] RES_NAMES = { "title", "graphStd", "graphPressed", "controlsStd",
			"controlsPressed", "profileStd", "profilePressed", "gameoptStd", "gameoptPressed", "backStd",
			"backPressed", };

	private static final String[] RES_PATHS = { "title", "graphStd", "graphPressed", "controlsStd",
			"controlsPressed", "profileStd", "profilePressed", "gameoptStd", "gameoptPressed", "backStd",
			"backPressed", };

	private static final String RES_FOLDER = "/resources/menu/menuSettings/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuSettings";

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private List<Widget> widgets;

	private Menu m;

	public MenuSettings(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		this.loadResources();

		this.wTitle();
		this.wBack();
		this.wGraph();
		this.wControls();
		this.wProfile();
		this.wGameopt();
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
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

		btn.setPos(new Point(30, 30));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wControls() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
			}
		};

		btn.setPos(new Point(394, 321));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/controlsStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/controlsPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wGameopt() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
			}
		};

		btn.setPos(new Point(1042, 321));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/gameoptStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/gameoptPressed"));

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

		btn.setPos(new Point(70, 321));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/graphStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/graphPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wProfile() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
			}
		};

		btn.setPos(new Point(718, 321));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/profileStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/profilePressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(392, 54));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/title"));

		title.setDrawElement(i);

		this.widgets.add(title);
	}
}
