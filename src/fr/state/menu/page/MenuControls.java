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
import fr.state.menu.widget.TextData;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WUserKeyInput;
import fr.state.menu.widget.drawelements.DEImage;
import fr.util.point.Point;

public class MenuControls implements MenuPage {

	private static final String[] RES_NAMES = { "title", "backStd", "backPressed", "frame", "inputStd", "inputSel", };

	private static final String[] RES_PATHS = { "title", "backStd", "backPressed", "frame", "inputStd", "inputSel", };

	private static final String RES_FOLDER = "/resources/menu/menuControls/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuControls";

	private static final String PARAM_NAME_LEFT = "leftMovement";
	private static final String PARAM_NAME_RIGHT = "rightMovement";
	private static final String PARAM_NAME_JUMP = "jump";
	private static final String PARAM_NAME_SHOOT = "shoot";
	private static final String PARAM_NAME_USE = "use";

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

	public MenuControls(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		this.loadResources();

		DatafilesManager dfm = DatafilesManager.getInstance();
		this.controlsConf = dfm.getFile("controls");
		this.manager = dfm.getXmlManager();

		int leftMouvementKey = (int) this.manager.getParam(this.controlsConf, PARAM_NAME_LEFT, 0);
		int rightMouvementKey = (int) this.manager.getParam(this.controlsConf, PARAM_NAME_RIGHT, 0);
		int jumpKey = (int) this.manager.getParam(this.controlsConf, PARAM_NAME_JUMP, 0);
		int shootKey = (int) this.manager.getParam(this.controlsConf, PARAM_NAME_SHOOT, 0);
		int useKey = (int) this.manager.getParam(this.controlsConf, PARAM_NAME_USE, 0);

		this.wTitle();
		this.wBack();

		this.wframe();

		this.controls = new WUserKeyInput[5];

		int i = 0;

		this.controls[i] = this.wLeftMouvementInput();
		this.controls[i++].setData(leftMouvementKey);

		this.controls[i] = this.wRightMouvementInput();
		this.controls[i++].setData(rightMouvementKey);

		this.controls[i] = this.wShootInput();
		this.controls[i++].setData(shootKey);

		this.controls[i] = this.wJumpInput();
		this.controls[i++].setData(jumpKey);

		this.controls[i] = this.wUseInput();
		this.controls[i++].setData(useKey);

		this.changeColorOnSame();
	}

	private void changeColorOnSame() {
		for (WUserKeyInput w : this.controls) {
			w.getTextData().setColor(COLOR);
		}
		for (int i = 0; i < this.controls.length; i++) {
			for (int j = 0; j < this.controls.length; j++) {
				if (i >= j) {
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
		i.setImage(ImageManager.getInstance().get("menuSettings/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setPos(new Point(42, 42));
		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private void wframe() {
		WElement w = new WElement(this);

		DEImage img = new DEImage();

		img.setImage(ImageManager.getInstance().get("menuControls/frame"));

		w.setDrawElement(img);

		w.setPos(new Point(333, 337));

		this.widgets.add(w);
	}

	private WUserKeyInput wJumpInput() {
		WUserKeyInput u = new WUserKeyInput(this) {
			@Override
			public void dataChanged(int data) {
				MenuControls.this.changeColorOnSame();
				MenuControls.this.manager.setParam(MenuControls.this.controlsConf, PARAM_NAME_JUMP, data);
				MenuControls.this.manager.saveFile(MenuControls.this.controlsConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuControls/inputStd"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuControls/inputSel"));

		u.setStdDrawElement(std);

		u.setSelectedDrawElement(sel);

		u.setPos(new Point(1213, 609));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "", COLOR, 3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}

	private WUserKeyInput wLeftMouvementInput() {
		WUserKeyInput u = new WUserKeyInput(this) {
			@Override
			public void dataChanged(int data) {
				MenuControls.this.changeColorOnSame();
				MenuControls.this.manager.setParam(MenuControls.this.controlsConf, PARAM_NAME_LEFT, data);
				MenuControls.this.manager.saveFile(MenuControls.this.controlsConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuControls/inputStd"));

		DEImage sel = new DEImage();
		sel.setImage(im.get("menuControls/inputSel"));

		u.setStdDrawElement(std);

		u.setSelectedDrawElement(sel);

		u.setPos(new Point(1213, 352));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "", COLOR, 3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}

	private WUserKeyInput wRightMouvementInput() {
		WUserKeyInput u = new WUserKeyInput(this) {
			@Override
			public void dataChanged(int data) {
				MenuControls.this.changeColorOnSame();
				MenuControls.this.manager.setParam(MenuControls.this.controlsConf, PARAM_NAME_RIGHT, data);
				MenuControls.this.manager.saveFile(MenuControls.this.controlsConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuControls/inputStd"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuControls/inputSel"));

		u.setStdDrawElement(std);

		u.setSelectedDrawElement(sel);

		u.setPos(new Point(1213, 481));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "", COLOR, 3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}

	private WUserKeyInput wShootInput() {
		WUserKeyInput u = new WUserKeyInput(this) {
			@Override
			public void dataChanged(int data) {
				MenuControls.this.changeColorOnSame();
				MenuControls.this.manager.setParam(MenuControls.this.controlsConf, PARAM_NAME_SHOOT, data);
				MenuControls.this.manager.saveFile(MenuControls.this.controlsConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuControls/inputStd"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuControls/inputSel"));

		u.setStdDrawElement(std);

		u.setSelectedDrawElement(sel);

		u.setPos(new Point(1213, 737));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "", COLOR, 3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(550, 76));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/title"));

		title.setDrawElement(i);

		this.widgets.add(title);
	}

	private WUserKeyInput wUseInput() {
		WUserKeyInput u = new WUserKeyInput(this) {
			@Override
			public void dataChanged(int data) {
				MenuControls.this.changeColorOnSame();
				MenuControls.this.manager.setParam(MenuControls.this.controlsConf, PARAM_NAME_USE, data);
				MenuControls.this.manager.saveFile(MenuControls.this.controlsConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get("menuControls/inputStd"));
		DEImage sel = new DEImage();
		sel.setImage(im.get("menuControls/inputSel"));

		u.setStdDrawElement(std);

		u.setSelectedDrawElement(sel);

		u.setPos(new Point(1213, 865));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "", COLOR, 3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}
}
