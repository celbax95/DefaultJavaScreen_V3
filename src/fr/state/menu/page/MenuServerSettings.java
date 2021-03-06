package fr.state.menu.page;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.datafilesmanager.DatafilesManager;
import fr.datafilesmanager.XMLManager;
import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.server.GlobalServerData;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WScroller;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuServerSettings implements MenuPage {

	private static final String[] RES_NAMES = {
			"title",
			"backStd",
			"backPressed",
			"border",
			"elementStd",
			"elementSel" };

	private static final String[] RES_PATHS = {
			"title",
			"backStd",
			"backPressed",
			"border",
			"elementStd",
			"elementSel" };

	private static final String RES_FOLDER = "/resources/menu/menuServerSettings/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menu/severSettings";

	private static final int NB_EXCLUSIVE_BTN = GlobalServerData.getIPAmount();

	private static final int ELEMENT_HEIGHT = 128;

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private boolean loaded;
	private Object serverConf;

	private XMLManager manager;

	private WButton[] exclusiveBtn;

	private int btnIndex;

	private List<Widget> widgets;

	private Menu m;

	private int returnPage;

	/**
	 * @param returnPage </br>
	 *                   0 : menuHost </br>
	 *                   1 : menuJoin
	 */
	public MenuServerSettings(Menu m, int returnPage) {
		this.loaded = false;
		this.m = m;
		this.returnPage = returnPage;
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
	}

	@Override
	public boolean isLoaded() {
		return this.loaded;
	}

	@Override
	public void load() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				MenuServerSettings.this.returnPage = MenuServerSettings.this.returnPage;

				MenuServerSettings.this.loadResources();

				MenuServerSettings.this.widgets = new Vector<>();

				MenuServerSettings.this.exclusiveBtn = new WButton[NB_EXCLUSIVE_BTN];
				MenuServerSettings.this.btnIndex = -1;

				DatafilesManager dfm = DatafilesManager.getInstance();
				MenuServerSettings.this.serverConf = dfm.getFile("serverConf");
				MenuServerSettings.this.manager = dfm.getXmlManager();

				MenuServerSettings.this.wTitle();
				MenuServerSettings.this.wBack();
				MenuServerSettings.this.wScrollList();
				MenuServerSettings.this.wBorder();

				MenuServerSettings.this.setBtnIndex(
						(int) MenuServerSettings.this.manager.getParam(MenuServerSettings.this.serverConf, "id", 0));

				MenuServerSettings.this.loaded = true;
			}
		});
		thread.setName("MenuServerSettings/load");
		thread.start();
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
	}

	private WButton serverIdBtn(Point pos, String text, int id) {
		WButton w = new WButton(this) {

			@Override
			public void action() {
				MenuServerSettings.this.manager.setParam(MenuServerSettings.this.serverConf, "id", id);
				MenuServerSettings.this.manager.saveFile(MenuServerSettings.this.serverConf);

				MenuServerSettings.this.setBtnIndex(id);
			}
		};

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 30), text, Color.black,
				3);

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/elementStd"));

		i.setLabel(td);

		w.setStdDrawElement(i);

		i.setImage(im.get(PAGE_NAME + "/elementSel"));
		w.setPressedDrawElement(i);

		w.setCanPressed(true);
		w.setPos(pos.clone());
		w.setHitboxFromDrawElement();

		return w;
	}

	private void setBtnIndex(int index) {
		if (index != this.btnIndex) {
			int oldIndex = this.btnIndex;
			this.btnIndex = index;
			this.switchSelBtn(oldIndex);
			this.m.getMenuState().getStatePanel().getWinData().setNeedRestart(true);
		}
	}

	private void switchSelBtn(int oldIndex) {
		ImageManager im = ImageManager.getInstance();

		if (oldIndex >= 0 && oldIndex < this.exclusiveBtn.length) {
			WButton w = this.exclusiveBtn[oldIndex];
			DEImage i = (DEImage) w.getStdDrawElement().clone();
			i.setImage(im.get(PAGE_NAME + "/elementStd"));
			w.setStdDrawElement(i);
			w.setCanPressed(true);
		}

		WButton w = this.exclusiveBtn[this.btnIndex];
		DEImage i = (DEImage) w.getStdDrawElement().clone();
		i.setImage(im.get(PAGE_NAME + "/elementSel"));

		w.setStdDrawElement(i);

		w.setCanPressed(false);
	}

	@Override
	public void update(Input input) {
		for (Widget w : this.widgets) {
			w.update(input);
		}
	}

	private void wBack() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				if (MenuServerSettings.this.returnPage == 0) {
					MenuServerSettings.this.m.applyPage(new MenuHost(MenuServerSettings.this.m));
				} else if (MenuServerSettings.this.returnPage == 1) {
					MenuServerSettings.this.m.applyPage(new MenuJoin(MenuServerSettings.this.m));
				}
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setPos(new Point(42, 42));
		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wBorder() {
		WElement w = new WElement(this);

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get(PAGE_NAME + "/border"));

		w.setDrawElement(i);
		w.setPos(new Point(618, 392));

		this.widgets.add(w);
	}

	private WScroller wScrollList() {
		WScroller w = new WScroller(this);

		w.setPos(new Point(624, 398));
		w.setSize(new Point(673, 509));
		w.setMaxScroll(ELEMENT_HEIGHT * NB_EXCLUSIVE_BTN - 509);

		DERectangle r = new DERectangle();
		r.setSize(new Point(10, 70));
		r.setColor(new Color(60, 60, 60));
		r.setRoundBorder(5);

		w.setSlider(r);
		w.setPaddingSide(1);

		for (int i = 0; i < NB_EXCLUSIVE_BTN; i++) {
			MenuServerSettings.this.exclusiveBtn[i] = MenuServerSettings.this
					.serverIdBtn(new Point(0, i * ELEMENT_HEIGHT), "Server n� " + (i + 1), i);

			w.add(this.exclusiveBtn[i]);
		}

		this.widgets.add(w);

		return w;
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(550, 76));

		DEImage img = new DEImage();
		img.setImage(ImageManager.getInstance().get(PAGE_NAME + "/title"));

		title.setDrawElement(img);

		this.widgets.add(title);
	}
}
