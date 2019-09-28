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

public class MenuMain implements MenuPage {

	private static final String[] RES_NAMES = { "background", "title", "exitStd", "exitPressed", "hostStd",
			"hostPressed", "joinStd", "joinPressed", "settingsStd", "settingsPressed" };

	private static final String[] RES_PATHS = { "background", "title", "exitStd", "exitPressed", "hostStd",
			"hostPressed", "joinStd", "joinPressed", "settingsStd", "settingsPressed" };

	private static final String RES_FOLDER = "/resources/menu/menuMain/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuMain";

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

	public MenuMain(Menu m) {
		this.m = m;

		this.widgets = new Vector<>();

		this.loadResources();

		this.wBackground();
		this.wTitle();
		this.wJoinButton();
		this.wHostButton();
		this.wExitButton();
		this.wSettingsButton();
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

	private void wBackground() {
		WElement i = new WElement(this);

		DEImage image = new DEImage();

		image.setImage(ImageManager.getInstance().get("menuMain/background"));

		i.setPos(new Point(416, 0));
		i.setDrawElement(image);

		this.widgets.add(i);
	}

	private void wExitButton() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuMain.this.m.getMenuState().getStatePanel().getWindow().close();
			}
		};

		DEImage i = new DEImage();

		ImageManager im = ImageManager.getInstance();

		i.setImage(im.get("menuMain/exitStd"));
		b.setStdDrawElement(i);

		i = (DEImage) i.clone();
		i.setImage(im.get("menuMain/exitPressed"));
		b.setPressedDrawElement(i);

		b.setPos(new Point(631, 631));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);
	}

	private void wHostButton() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				// MenuMain.this.m.applyPage(new MenuOption(MenuMain.this.m));
			}
		};

		DEImage i = new DEImage();

		ImageManager im = ImageManager.getInstance();

		i.setImage(im.get("menuMain/hostStd"));
		b.setStdDrawElement(i);

		i = (DEImage) i.clone();
		i.setImage(im.get("menuMain/hostPressed"));
		b.setPressedDrawElement(i);

		b.setPos(new Point(568, 464));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);
	}

	private void wJoinButton() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				// MenuMain.this.m.applyPage(new MenuOption(MenuMain.this.m));
			}
		};

		DEImage i = new DEImage();

		ImageManager im = ImageManager.getInstance();

		i.setImage(im.get("menuMain/joinStd"));
		b.setStdDrawElement(i);

		i = (DEImage) i.clone();
		i.setImage(im.get("menuMain/joinPressed"));
		b.setPressedDrawElement(i);

		b.setPos(new Point(538, 298));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);
	}

	private void wSettingsButton() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuMain.this.m.applyPage(new MenuSettings(MenuMain.this.m));
			}
		};

		DEImage i = new DEImage();

		ImageManager im = ImageManager.getInstance();

		i.setImage(im.get("menuMain/settingsStd"));
		b.setStdDrawElement(i);

		i = (DEImage) i.clone();
		i.setImage(im.get("menuMain/settingsPressed"));
		b.setPressedDrawElement(i);

		b.setPos(new Point(49, 648));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);
	}

	private void wTitle() {
		WElement title = new WElement(this);

		DEImage i = new DEImage();

		i.setImage(ImageManager.getInstance().get("menuMain/title"));

		title.setDrawElement(i);
		title.setPos(new Point(392, 54));

		this.widgets.add(title);
	}
}
