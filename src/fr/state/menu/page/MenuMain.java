package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.TextData;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WSlider;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
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
		this.wButtonToOption();
		this.wTitle();
//		this.wSlide();
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

	private void wButtonToOption() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuMain.this.m.applyPage(new MenuOption(MenuMain.this.m));
			}
		};

		DERectangle rect1 = new DERectangle();

		TextData label = new TextData(new Point(), new Font("Arial", Font.BOLD, 22), "OPTIONS", Color.RED, 3);

		label.lock();

		rect1.setLabel(label);
		rect1.setColor(Color.BLUE);
		rect1.setSize(new Point(200, 200));
		rect1.lock();

		DERectangle rect2 = new DERectangle();

		rect2.setLabel(label);
		rect2.setColor(Color.CYAN);
		rect2.setSize(new Point(200, 200));
		rect2.lock();

		b.setPos(new Point(200, 200));
		b.setStdDrawElement(rect1);
		b.setPressedDrawElement(rect2);
		b.setHitboxFromDrawElement();

		this.widgets.add(b);
	}

	private void wSlide() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value) {
				System.out.println(value);
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(300, 20));
		bar.lock();

		DERectangle slider = new DERectangle();

		slider.setColor(Color.WHITE);
		slider.setSize(new Point(20, 40));
		slider.lock();

		s.setPos(new Point(300, 500));
		s.setBar(bar);
		s.setSlider(slider);
		s.setScope(1);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);
	}

	private void wTitle() {
		WElement title = new WElement(this);

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "MENU",
				Color.WHITE, 0);
		td.lock();

		DEImage i = new DEImage();

		i.setImage(ImageManager.getInstance().get("menuMain/title"));
		i.lock();

		title.setDrawElement(i);
		title.setPos(new Point(392, 54));

		this.widgets.add(title);
	}
}
