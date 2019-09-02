package fr.state.menu;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.state.menu.page.MenuMain;

public class Menu {

	private MenuState menuState;

	private MenuPage page;

	public Menu(MenuState ms) {
		this.menuState = ms;
	}

	public void applyDefautPage() {
		this.applyPage(new MenuMain(this));
	}

	public void applyPage(MenuPage mp) {
		this.page = mp;
	}

	public void draw(Graphics2D g) {
		this.page.draw(g);
	}

	public MenuState getMenuState() {
		return this.menuState;
	}

	public void setMenuState(MenuState menuState) {
		this.menuState = menuState;
	}

	public void update(Input input) {
		this.page.update(input);
	}
}
