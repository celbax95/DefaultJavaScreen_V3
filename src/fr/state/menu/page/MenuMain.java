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

	private List<Widget> widgets;

	private Menu m;

	public MenuMain(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		ImageLoader im = new ImageLoader();

		im.loadAll();

		this.wBackground();
		this.wTitle();
		this.wJoinButton();
		this.wHostButton();
		this.wExitButton();
		this.wOptionButton();
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
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

	private void wOptionButton() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuMain.this.m.applyPage(new MenuOption(MenuMain.this.m));
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
