package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.logger.Logger;
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
		this.wSlide();
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

		DERectangle rect = new DERectangle();

		TextData label = new TextData(new Point(), new Font("Arial", Font.BOLD, 22), "OPTIONS", Color.RED, 3);

		rect.setLabel(label);
		rect.setColor(Color.BLUE);
		rect.setSize(new Point(200, 200));
		b.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(Color.CYAN);
		b.setPressedDrawElement(rect);

		b.setPos(new Point(200, 200));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);
	}

	private void wSlide() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value) {
				Logger.obs("Valeur du slider : " + value);
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(300, 20));
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.RED);
		slider.setSize(new Point(120, 30));
		s.setSlider(slider);

		s.setPos(new Point(300, 500));
		s.setScope(10);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);
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
