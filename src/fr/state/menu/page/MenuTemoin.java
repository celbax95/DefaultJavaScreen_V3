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
import fr.state.menu.widget.BorderData;
import fr.state.menu.widget.TextData;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WSlider;
import fr.state.menu.widget.WSwitch;
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
		this.wSwitch();
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

	private void wButtonToMenu() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuTemoin.this.m.applyPage(new MenuMain(MenuTemoin.this.m));
			}
		};

		DERectangle rect = new DERectangle();

		TextData label = new TextData(new Point(), new Font("Arial", Font.BOLD, 22), "OPTIONS", Color.BLUE,
				3);
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

	private void wSwitch() {
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
		TextData label = new TextData(new Point(), new Font("Arial", Font.PLAIN, 20), "Active moiiiii",
				Color.WHITE, 3);
		label.lock();

		BorderData border = new BorderData(3, Color.white, 1);
		border.lock();

		DERectangle rect = new DERectangle();

		rect.setPos(new Point(800, 400));
		rect.setLabel(label);
		rect.setColor(Color.RED);
		rect.setSize(new Point(200, 100));
		rect.setBorder(border);
		rect.lock();
		s.setOffDrawElement(rect);

		rect = (DERectangle) rect.clone();
		label = new TextData(label);
		label.setColor(Color.BLACK);
		label.lock();
		rect.setLabel(label);
		rect.setColor(new Color(200, 30, 30));
		rect.lock();
		s.setPressedOffDrawElement(rect);

		rect = (DERectangle) rect.clone();
		label = new TextData(label);
		label.setColor(Color.BLACK);
		label.setText("Je suis activé :)");
		label.lock();
		rect.setLabel(label);
		rect.setColor(Color.GREEN);
		rect.lock();
		s.setOnDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(30, 200, 30));
		rect.lock();
		s.setPressedOnDrawElement(rect);

		s.setHitboxFromDrawElement();

		this.widgets.add(s);
	}

	private void wTitle() {
		WElement title = new WElement(this);

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45),
				"OPTIONS", Color.WHITE, 0);
		td.lock();

		DELabel de = new DELabel(new Point(), td);

		int width = this.m.getMenuState().getStatePanel().getWidth();
		title.setPos(new Point(width / 2 - td.getSize().ix() / 2, 80));

		title.setDrawElement(de);

		this.widgets.add(title);
	}

}
