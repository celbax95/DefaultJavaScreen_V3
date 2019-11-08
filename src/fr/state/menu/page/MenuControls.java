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
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WScroller;
import fr.state.menu.widget.WUserKeyInput;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuControls implements MenuPage {

	private static final String[] RES_NAMES = {
			"title",
			"backStd",
			"backPressed",
			"border",
			"controlsList",
			"inputStd",
			"inputSel", };

	private static final String[] RES_PATHS = {
			"title",
			"backStd",
			"backPressed",
			"border",
			"controlsList",
			"inputStd",
			"inputSel", };

	private static final String RES_FOLDER = "/resources/menu/menuControls/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menu/controls";

	private static final String PARAM_NAME_LEFT = "leftMovement";
	private static final String PARAM_NAME_RIGHT = "rightMovement";
	private static final String PARAM_NAME_JUMP = "jump";
	private static final String PARAM_NAME_SHOOT = "shoot";
	private static final String PARAM_NAME_USE = "use";

	private static final String[] PARAM_NAMES = {
			PARAM_NAME_LEFT,
			PARAM_NAME_RIGHT,
			PARAM_NAME_JUMP,
			PARAM_NAME_SHOOT,
			PARAM_NAME_USE, };

	private static final int SPACE_BETWEEN_INPUTS = 128;
	private static final int INPUT_POS_X = 874;
	private static final int INPUT_START_POS_Y = 9;

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private static final Color COLOR = Color.BLACK;

	private static final Color INVALID_COLOR = Color.RED;

	private List<Widget> widgets;

	private Menu m;

	private Object controlsConf;

	private XMLManager manager;

	private WUserKeyInput[] controls;

	private boolean loaded;

	public MenuControls(Menu m) {
		this.loaded = false;
		this.m = m;
	}

	private void changeColorOnSame() {
		for (WUserKeyInput w : this.controls) {
			w.getTextData().setColor(COLOR);
		}
		for (int i = 0; i < this.controls.length; i++) {
			for (int j = 0; j < this.controls.length; j++) {
				if (i >= j || this.controls[j].getData() == 0) {
					continue;
				}
				if (this.controls[i].getData() == this.controls[j].getData()) {
					this.controls[i].getTextData().setColor(INVALID_COLOR);
					this.controls[j].getTextData().setColor(INVALID_COLOR);
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
	}

	private WElement getWControlsList() {
		WElement w = new WElement(this);

		DEImage img = new DEImage();

		img.setImage(ImageManager.getInstance().get(PAGE_NAME + "/controlsList"));

		w.setDrawElement(img);

		w.setPos(new Point(0, 0));

		return w;
	}

	private WUserKeyInput getWUserKeyInput(int index, String paramName) {
		WUserKeyInput u = new WUserKeyInput(this) {
			@Override
			public void dataChanged(int data) {
				MenuControls.this.changeColorOnSame();
				MenuControls.this.manager.setParam(MenuControls.this.controlsConf, paramName, data);
				MenuControls.this.manager.saveFile(MenuControls.this.controlsConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get(PAGE_NAME + "/inputStd"));
		DEImage sel = new DEImage();
		sel.setImage(im.get(PAGE_NAME + "/inputSel"));

		u.setStdDrawElement(std);

		u.setSelectedDrawElement(sel);

		u.setPos(new Point(INPUT_POS_X, INPUT_START_POS_Y + SPACE_BETWEEN_INPUTS * index));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "", COLOR, 3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		return u;
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
				MenuControls.this.widgets = new Vector<>();

				MenuControls.this.loadResources();

				DatafilesManager dfm = DatafilesManager.getInstance();
				MenuControls.this.controlsConf = dfm.getFile("controls");
				MenuControls.this.manager = dfm.getXmlManager();

				MenuControls.this.wTitle();
				MenuControls.this.wBack();

				WScroller scroller = MenuControls.this.wScroller();

				scroller.add(MenuControls.this.getWControlsList());

				MenuControls.this.wBorder();

				MenuControls.this.controls = new WUserKeyInput[PARAM_NAMES.length];

				for (int i = 0; i < PARAM_NAMES.length; i++) {
					MenuControls.this.controls[i] = MenuControls.this.getWUserKeyInput(i, PARAM_NAMES[i]);
					MenuControls.this.controls[i].setData((int) MenuControls.this.manager
							.getParam(MenuControls.this.controlsConf, PARAM_NAMES[i], 0));
					scroller.add(MenuControls.this.controls[i]);
				}

				MenuControls.this.changeColorOnSame();

				MenuControls.this.loaded = true;
			}
		});
		thread.setName("MenuControls/load");
		thread.start();
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
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
				MenuControls.this.m.applyPage(new MenuSettings(MenuControls.this.m));
			}
		};

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setPos(new Point(42, 42));
		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wBorder() {
		WElement w = new WElement(this);

		DEImage img = new DEImage();

		img.setImage(ImageManager.getInstance().get(PAGE_NAME + "/border"));

		w.setDrawElement(img);

		w.setPos(new Point(333, 337));

		this.widgets.add(w);
	}

	private WScroller wScroller() {
		WScroller sc = new WScroller(this);

		sc.setPos(new Point(339, 343));
		sc.setSize(new Point(1242, 637));
		sc.setMaxScroll(0);
		sc.setScrollStep(30);
		sc.setDrawAdvanced(true);
		sc.setPaddingBottom(0);
		sc.setPaddingTop(0);
		sc.setPaddingSide(0);
		sc.setDisplayScrollBar(false);
		sc.setSliderLeftSide(true);
		sc.setScrollBarColor(new Color(150, 150, 150));
		DERectangle rect = sc.getDefaultSlider();
		rect.setColor(new Color(130, 130, 130));
		sc.setSlider(rect);

		this.widgets.add(sc);

		return sc;
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(550, 76));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/title"));

		title.setDrawElement(i);

		this.widgets.add(title);
	}
}
