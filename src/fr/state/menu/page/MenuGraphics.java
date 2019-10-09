package fr.state.menu.page;

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
import fr.state.menu.widget.drawelements.DEImage;
import fr.util.point.Point;
import fr.window.WinData;

public class MenuGraphics implements MenuPage {

	private static final String[] RES_NAMES = {
			"title",
			"backStd",
			"backPressed",
			"frame",
			"1280Std",
			"1280Sel",
			"1366Std",
			"1366Sel",
			"1600Std",
			"1600Sel",
			"1920Std",
			"1920Sel",
			"fullscreenStd",
			"fullscreenSel",
			"warning", };

	private static final String[] RES_PATHS = {
			"title",
			"backStd",
			"backPressed",
			"frame",
			"1280Std",
			"1280Sel",
			"1366Std",
			"1366Sel",
			"1600Std",
			"1600Sel",
			"1920Std",
			"1920Sel",
			"fullscreenStd",
			"fullscreenSel",
			"warning", };

	private static final String RES_FOLDER = "/resources/menu/menuGraphics/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuGraphics";

	private static final Point res1280 = new Point(1280, 720);
	private static final Point res1366 = new Point(1366, 768);
	private static final Point res1600 = new Point(1600, 900);
	private static final Point res1920 = new Point(1920, 1080);

	private static final int NB_RES_EXCLUSIVE_BTN = 5;

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private Object winConf;

	private XMLManager manager;

	private WButton[] exclusiveResBtn;

	private int resBtnIndex;

	private WinData winData;

	private List<Widget> widgets;

	private Widget needRestart;

	private Menu m;

	public MenuGraphics(Menu m) {

		this.m = m;

		this.loadResources();

		this.winData = m.getMenuState().getStatePanel().getWinData();

		this.widgets = new Vector<>();

		this.exclusiveResBtn = new WButton[NB_RES_EXCLUSIVE_BTN];
		this.resBtnIndex = 0;

		DatafilesManager dfm = DatafilesManager.getInstance();
		this.winConf = dfm.getFile("winConf");
		this.manager = dfm.getXmlManager();

		int width = (int) this.manager.getParam(this.winConf, "width", 0);
		int height = (int) this.manager.getParam(this.winConf, "height", 0);
		boolean fullscreen = (boolean) this.manager.getParam(this.winConf, "fullscreen", false);

		if (fullscreen) {
			this.resBtnIndex = NB_RES_EXCLUSIVE_BTN - 1;
		} else if (width == 1280 && height == 720) {
			this.resBtnIndex = 0;
		} else if (width == 1366 && height == 768) {
			this.resBtnIndex = 1;
		} else if (width == 1600 && height == 900) {
			this.resBtnIndex = 2;
		} else if (width == 1920 && height == 1080) {
			this.resBtnIndex = 3;
		}

		this.wTitle();
		this.wBack();
		this.wFrame();
		int i = 0;
		this.exclusiveResBtn[i++] = this.w1280();
		this.exclusiveResBtn[i++] = this.w1366();
		this.exclusiveResBtn[i++] = this.w1600();
		this.exclusiveResBtn[i++] = this.w1920();
		this.exclusiveResBtn[i++] = this.wFullscreen();
		this.needRestart = this.wRestart();
		this.switchResolutionBtn();
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
	}

	private void setResBtnIndex(int index) {
		if (index != this.resBtnIndex) {
			this.resBtnIndex = index;
			this.switchResolutionBtn();
			this.m.getMenuState().getStatePanel().getWinData().setNeedRestart(true);
			this.needRestart.setVisible(true);
		}
	}

	private void switchResolutionBtn() {
		for (WButton w : this.exclusiveResBtn) {
			w.setCanPressed(true);
		}
		this.exclusiveResBtn[this.resBtnIndex].setCanPressed(false);
	}

	@Override
	public void update(Input input) {
		for (Widget w : this.widgets) {
			w.update(input);
		}
	}

	private WButton w1280() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuGraphics.this.setResBtnIndex(0);
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "width", res1280.ix());
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "height", res1280.iy());
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "fullscreen", false);
				MenuGraphics.this.manager.saveFile(MenuGraphics.this.winConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuGraphics/1280Std"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuGraphics/1280Sel"));

		b.setStdDrawElement(std);
		b.setPressedDrawElement(sel);
		b.setCantPressDrawElement(sel);

		b.setPos(new Point(531, 532));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);

		return b;
	}

	private WButton w1366() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuGraphics.this.setResBtnIndex(1);
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "width", res1366.ix());
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "height", res1366.iy());
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "fullscreen", false);
				MenuGraphics.this.manager.saveFile(MenuGraphics.this.winConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuGraphics/1366Std"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuGraphics/1366Sel"));

		b.setStdDrawElement(std);
		b.setPressedDrawElement(sel);
		b.setCantPressDrawElement(sel);

		b.setPos(new Point(746, 532));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);

		return b;
	}

	private WButton w1600() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuGraphics.this.setResBtnIndex(2);
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "width", res1600.ix());
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "height", res1600.iy());
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "fullscreen", false);
				MenuGraphics.this.manager.saveFile(MenuGraphics.this.winConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuGraphics/1600Std"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuGraphics/1600Sel"));

		b.setStdDrawElement(std);
		b.setPressedDrawElement(sel);
		b.setCantPressDrawElement(sel);

		b.setPos(new Point(960, 532));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);

		return b;
	}

	private WButton w1920() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuGraphics.this.setResBtnIndex(3);
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "width", res1920.ix());
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "height", res1920.iy());
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "fullscreen", false);
				MenuGraphics.this.manager.saveFile(MenuGraphics.this.winConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuGraphics/1920Std"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuGraphics/1920Sel"));

		b.setStdDrawElement(std);
		b.setPressedDrawElement(sel);
		b.setCantPressDrawElement(sel);

		b.setPos(new Point(1174, 532));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);

		return b;
	}

	private void wBack() {
		WButton btn = new WButton(this) {
			@Override
			public void action() {
				MenuGraphics.this.m.applyPage(new MenuSettings(MenuGraphics.this.m));
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage i = new DEImage();
		i.setImage(im.get("menuGraphics/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(im.get("menuGraphics/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setPos(new Point(42, 42));
		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wFrame() {
		WElement w = new WElement(this);

		DEImage img = new DEImage();

		img.setImage(ImageManager.getInstance().get("menuGraphics/frame"));

		w.setDrawElement(img);
		w.setPos(new Point(333, 337));

		this.widgets.add(w);
	}

	private WButton wFullscreen() {
		WButton b = new WButton(this) {
			@Override
			public void action() {
				MenuGraphics.this.setResBtnIndex(NB_RES_EXCLUSIVE_BTN - 1);
				MenuGraphics.this.manager.setParam(MenuGraphics.this.winConf, "fullscreen", true);
				MenuGraphics.this.manager.saveFile(MenuGraphics.this.winConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuGraphics/fullscreenStd"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuGraphics/fullscreenSel"));

		b.setStdDrawElement(std);
		b.setPressedDrawElement(sel);
		b.setCantPressDrawElement(sel);

		b.setPos(new Point(745, 675));
		b.setHitboxFromDrawElement();

		this.widgets.add(b);

		return b;
	}

	private Widget wRestart() {
		WElement e = new WElement(this);

		DEImage img = new DEImage();
		img.setImage(ImageManager.getInstance().get("menuGraphics/warning"));

		e.setDrawElement(img);

		e.setPos(new Point(593, 859));

		e.setVisible(this.winData.isNeedRestart());

		this.widgets.add(e);

		return e;
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(550, 76));

		DEImage img = new DEImage();
		img.setImage(ImageManager.getInstance().get("menuGraphics/title"));

		title.setDrawElement(img);

		this.widgets.add(title);
	}
}
