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
import fr.state.menu.widget.BorderData;
import fr.state.menu.widget.TextData;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WImage;
import fr.state.menu.widget.WLabel;
import fr.state.menu.widget.WSlider;
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
		WImage i = new WImage(this);

		i.setPos(new Point(416, 0));
		i.setImage(ImageManager.getInstance().get("menuMain/background"));

		this.widgets.add(i);
	}

	private void wButtonToOption() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuMain.this.m.applyPage(new MenuOption(MenuMain.this.m));
			}
		};

		b.setLabel(new TextData(new Font("Arial", Font.BOLD, 22), "OPTIONS", Color.RED, Color.RED));
		b.setColor(Color.BLUE);
		b.setPressedColor(Color.CYAN);
		b.setPos(new Point(200, 200));
		b.setSize(new Point(200, 200));

		this.widgets.add(b);

		this.wTitle();
	}

	private void wSlide() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value) {
				System.out.println(value);
			}
		};

		s.setPos(new Point(300, 500));
		s.setSize(new Point(300, 40));
		s.setSlideWidth(14);
		s.setBarHeightScale(0.2);
		s.setClicNumber(1);
		s.setBarColor(Color.white);
		s.setSlideColor(Color.red);
		s.setBorder(new BorderData(2, Color.BLACK, Color.BLACK));

		this.widgets.add(s);
	}

	private void wTitle() {
		WLabel title = new WLabel(this);
		title.setTextData(new TextData(new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "MENU",
				Color.WHITE, Color.WHITE));

		int width = this.m.getMenuState().getStatePanel().getWidth();
		title.setPos(new Point(width / 2 - title.getTextData().getSize().ix() / 2, 80));

		this.widgets.add(title);
	}
}
