package fr.state.menu;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.state.menu.page.MenuMain;

public class Menu {

	private static final double FADE_RATE = 1D / 500; // X / Y : (X alpha tous les Y ms)

	private static final float NEW_ALPHA_START = 0.2f, OLD_ALPHA_START = 0.8f;

	private static final float STARTING_VALUE = 0.2f;

	private MenuState menuState;

	private MenuPage page, oldPage;

	private long lastCall;

	private float pageAlpha, oldPageAlpha;

	public Menu(MenuState ms) {
		this.menuState = ms;
		this.oldPage = null;
		this.lastCall = 0;
		this.oldPageAlpha = OLD_ALPHA_START;
		this.pageAlpha = NEW_ALPHA_START;
	}

	public void applyDefautPage() {
		this.applyPage(new MenuMain(this));
	}

	public void applyPage(MenuPage mp) {
		this.oldPage = this.page;
		this.page = mp;
		this.oldPageAlpha = OLD_ALPHA_START;
		this.pageAlpha = NEW_ALPHA_START;
		this.page.load();
	}

	public void draw(Graphics2D g) {

		float dAlpha = this.getDAlpha();

		if (this.pageAlpha < 1) {
			if (this.oldPage != null) {

				if (this.oldPageAlpha > 0f) {

					this.oldPageAlpha = this.oldPageAlpha - dAlpha;

					if (this.oldPageAlpha <= 0f) {
						this.oldPage = null;
						this.oldPageAlpha = 0f;
					} else {
						g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.oldPageAlpha));
						this.oldPage.draw(g);
					}
				}
			}

			if (this.page.isLoaded() && this.pageAlpha < 1f
					&& (this.oldPage == null || this.oldPageAlpha <= STARTING_VALUE)) {
				this.pageAlpha = this.pageAlpha + dAlpha >= 1f ? 1f : this.pageAlpha + dAlpha;

				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, this.pageAlpha));
				this.page.draw(g);
			}
		} else {
			this.page.draw(g);
		}
	}

	private float getDAlpha() {
		if (this.lastCall == 0) {
			this.lastCall = System.currentTimeMillis();
		}

		long time = System.currentTimeMillis();

		double elapsed = time - this.lastCall;

		this.lastCall = time;

		return (float) (elapsed * FADE_RATE);
	}

	public MenuState getMenuState() {
		return this.menuState;
	}

	public void setMenuState(MenuState menuState) {
		this.menuState = menuState;
	}

	public void update(Input input) {
		if (this.page.isLoaded()) {
			this.page.update(input);
		}
	}
}
