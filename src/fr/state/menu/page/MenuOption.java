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
import fr.state.menu.widget.WLabel;
import fr.state.menu.widget.WSwitch;
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
		b.setLabel(new TextData(new Font("Arial", Font.BOLD, 22), "MENU", Color.BLUE, Color.BLUE));
		b.setColor(Color.RED);
		b.setPressedColor(Color.pink);
		b.setPos(new Point(200, 200));
		b.setSize(new Point(200, 200));

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
		s.setPos(new Point(800, 400));
		s.setSize(new Point(200, 100));

		s.setColorOn(Color.GREEN);
		s.setPressedColorOn(new Color(30, 255, 30));
		s.setLabelOn(new TextData(new Font("Arial", Font.PLAIN, 20), "Je suis activé :)", Color.BLACK,
				Color.BLACK));

		s.setColorOff(Color.RED);
		s.setPressedColorOff(new Color(255, 30, 30));
		s.setLabelOff(
				new TextData(new Font("Arial", Font.PLAIN, 20), "Active moiiiii", Color.WHITE, Color.BLACK));

		s.setBorder(new BorderData(3, Color.WHITE, Color.WHITE));

		this.widgets.add(s);
	}

	private void wTitle() {
		WLabel title = new WLabel(this);
		title.setTextData(
				new TextData(new Font("Arial", Font.PLAIN, 30), "OPTIONS", Color.WHITE, Color.WHITE));
		title.setPos(new Point(550, 60));

		int width = this.m.getMenuState().getStatePanel().getWidth();
		title.setPos(new Point(width / 2 - title.getTextData().getSize().ix() / 2, 60));

		this.widgets.add(title);
	}

}
