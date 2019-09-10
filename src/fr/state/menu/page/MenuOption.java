package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.inputs.Input;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.BorderData;
import fr.state.menu.widget.TextData;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WSwitch;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuOption implements MenuPage {

	private List<Widget> widgets;

	private Menu m;

	public MenuOption(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		this.wButtonToMenu();
		this.wTitle();
		this.wSwitch();
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
				MenuOption.this.m.applyPage(new MenuMain(MenuOption.this.m));
			}
		};

		DERectangle rect1 = new DERectangle();

		TextData label = new TextData(new Point(), new Font("Arial", Font.BOLD, 22), "OPTIONS", Color.BLUE,
				2);

		label.lock();

		rect1.setLabel(label);
		rect1.setColor(Color.RED);
		rect1.setSize(new Point(200, 200));
		rect1.lock();

		DERectangle rect2 = new DERectangle();

		rect2.setLabel(label);
		rect2.setColor(Color.PINK);
		rect2.setSize(new Point(200, 200));
		rect2.lock();

		b.setPos(new Point(200, 200));
		b.setStdDrawElement(rect1);
		b.setPressedDrawElement(rect2);
		b.setHitboxFromDrawElement();

		this.widgets.add(b);

		this.wTitle();

		this.widgets.add(b);
	}

	private void wSwitch() {
		WSwitch s = new WSwitch(this) {
			@Override
			public void actionOff() {
				System.out.println("inactive...");
			}

			@Override
			public void actionOn() {
				System.out.println("active !");
			}
		};
		TextData label1 = new TextData(new Point(), new Font("Arial", Font.PLAIN, 20), "Active moiiiii",
				Color.WHITE, 2);
		label1.lock();
		TextData label2 = new TextData(new Point(), new Font("Arial", Font.PLAIN, 20), "Active moiiiii",
				Color.BLACK, 2);
		label2.lock();
		TextData label3 = new TextData(new Point(), new Font("Arial", Font.PLAIN, 20), "Je suis activé :)",
				Color.BLACK, 2);
		label3.lock();
		TextData label4 = new TextData(new Point(), new Font("Arial", Font.PLAIN, 20), "Je suis activé :)",
				Color.BLACK, 2);
		label4.lock();

		BorderData border = new BorderData(3, Color.white, 0);
		border.lock();

		DERectangle rect1 = new DERectangle();

		rect1.setLabel(label1);
		rect1.setColor(Color.RED);
		rect1.setSize(new Point(200, 100));
		rect1.setBorder(border);
		rect1.lock();

		DERectangle rect2 = new DERectangle();

		rect2.setLabel(label2);
		rect2.setColor(new Color(255, 30, 30));
		rect2.setSize(new Point(200, 100));
		rect2.setBorder(border);
		rect2.lock();

		DERectangle rect3 = new DERectangle();

		rect3.setLabel(label3);
		rect3.setColor(Color.GREEN);
		rect3.setSize(new Point(200, 100));
		rect3.setBorder(border);
		rect3.lock();

		DERectangle rect4 = new DERectangle();

		rect4.setLabel(label4);
		rect4.setColor(new Color(30, 255, 30));
		rect4.setSize(new Point(200, 100));
		rect4.setBorder(border);
		rect4.lock();

		s.setPos(new Point(800, 400));

		s.setOffDrawElement(rect1);
		s.setPressedOffDrawElement(rect2);
		s.setOnDrawElement(rect3);
		s.setPressedOnDrawElement(rect4);

		s.setHitboxFromDrawElement();

		this.widgets.add(s);
	}

	private void wTitle() {
		WElement title = new WElement(this);

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45),
				"OPTIONS", Color.WHITE, 0);
		td.lock();

		int width = this.m.getMenuState().getStatePanel().getWidth();
		title.setPos(new Point(width / 2 - td.getSize().ix() / 2, 80));

		this.widgets.add(title);
	}

}
