package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.inputs.Input;
import fr.logger.Logger;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WScroller;
import fr.state.menu.widget.WSlider;
import fr.state.menu.widget.WSwitch;
import fr.state.menu.widget.WUserInput;
import fr.state.menu.widget.WUserKeyInput;
import fr.state.menu.widget.WVSlider;
import fr.state.menu.widget.data.BorderData;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DELabel;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuTemoin implements MenuPage {

	private List<Widget> widgets;

	private Menu m;

	public MenuTemoin(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		this.wButtonToMenu();
		this.wTitle();
		WSwitch sw = this.wSwitch();
		this.wSlide();
		this.wVSlide();
		this.wUserInput();
		this.wUserKeyInput();
		WScroller scroller = this.wScroller();
		scroller.add(sw);
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

	private void wButtonToMenu() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuTemoin.this.m.applyPage(new MenuMain(MenuTemoin.this.m));
			}
		};

		DERectangle rect = new DERectangle();

		TextData label = new TextData(new Point(), new Font("Arial", Font.BOLD, 22), "MENU", Color.BLUE, 3);
		label.lock();

		rect.setLabel(label);
		rect.setColor(Color.RED);
		rect.setSize(new Point(200, 200));
		rect.lock();
		b.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();

		rect.setColor(Color.PINK);
		rect.lock();
		b.setPressedDrawElement(rect);

		b.setPos(new Point(200, 200));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);
	}

	private WScroller wScroller() {
		WScroller sc = new WScroller(this);

		sc.setPos(new Point(950, 300));
		sc.setSize(new Point(300, 200));
		sc.setMaxScroll(500);
		sc.setScrollStep(30);
		sc.setDrawAdvanced(true);
		sc.setPaddingBottom(0);
		sc.setPaddingTop(0);
		sc.setPaddingSide(0);
		sc.setSliderLeftSide(false);
		sc.setScrollBarColor(Color.DARK_GRAY);
		DERectangle rect = sc.getDefaultSlider();
		rect.setColor(Color.gray);
		sc.setSlider(rect);

		this.widgets.add(sc);

		return sc;
	}

	private void wSlide() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
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

		s.setPos(new Point(150, 650));
		s.setScope(10);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);
	}

	private WSwitch wSwitch() {
		WSwitch s = new WSwitch(this) {
			@Override
			public void actionOff() {
				Logger.obs("SwitchButton : non active");
			}

			@Override
			public void actionOn() {
				Logger.obs("SwitchButton : active");
			}
		};
		TextData label = new TextData(new Point(), new Font("Arial", Font.PLAIN, 20), "Active moi", Color.WHITE, 3);
		label.lock();

		BorderData border = new BorderData(3, Color.white, 1);
		border.lock();

		DERectangle rect = new DERectangle();

		rect.setLabel(label);
		rect.setColor(Color.RED);
		rect.setSize(new Point(200, 100));
		rect.setBorder(border);
		s.setOffDrawElement(rect);

		rect = (DERectangle) rect.clone();
		label = new TextData(label);
		label.setColor(Color.BLACK);
		rect.setLabel(label);
		rect.setColor(new Color(200, 30, 30));
		s.setPressedOffDrawElement(rect);

		rect = (DERectangle) rect.clone();
		label = new TextData(label);
		label.setColor(Color.BLACK);
		label.setText("Je suis activé :)");
		rect.setLabel(label);
		rect.setColor(Color.GREEN);
		s.setOnDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(30, 200, 30));
		s.setPressedOnDrawElement(rect);

		s.setPos(new Point(50, 300));

		s.setHitboxFromDrawElement();

		return s;
	}

	private void wTitle() {
		WElement title = new WElement(this);

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "OPTIONS",
				Color.WHITE, 0);
		td.lock();

		DELabel de = new DELabel(new Point(), td);

		int width = this.m.getMenuState().getStatePanel().getWidth();
		title.setPos(new Point(width / 2 - td.getSize().ix() / 2, 80));

		title.setDrawElement(de);

		this.widgets.add(title);
	}

	private void wUserInput() {
		WUserInput u = new WUserInput(this) {
			@Override
			public void dataChanged(String data) {
				System.out.println(data);
			}
		};

		BorderData border = new BorderData(2, Color.WHITE, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(300, 60));
		rect.setBorder(border);
		u.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(0, 50, 50));
		u.setSelectedDrawElement(rect);

		u.setPos(new Point(150, 470));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 35), "", Color.WHITE,
				3);

		u.setTextData(td);

		u.setDataLength(200);
		u.setHitboxFromDrawElement();

		this.widgets.add(u);
	}

	private void wUserKeyInput() {
		WUserKeyInput u = new WUserKeyInput(this) {
			@Override
			public void dataChanged(int data) {
				System.out.println(data);
			}
		};

		BorderData border = new BorderData(2, Color.WHITE, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(150, 70));
		rect.setBorder(border);
		u.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(0, 50, 50));
		u.setSelectedDrawElement(rect);

		u.setPos(new Point(680, 280));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 35), "", Color.WHITE,
				3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		this.widgets.add(u);
	}

	private void wVSlide() {
		WVSlider s = new WVSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				Logger.obs("Valeur du VSlider : " + value);
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(20, 300));
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.RED);
		slider.setSize(new Point(30, 120));
		s.setSlider(slider);

		s.setPos(new Point(150, 800));
		s.setScope(10);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);
	}
}
