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
import fr.state.menu.widget.BorderData;
import fr.state.menu.widget.TextData;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.WUserKeyInput;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuControls implements MenuPage {

	private static final String[] RES_NAMES = { "title" };

	private static final String[] RES_PATHS = { "title" };

	private static final String RES_FOLDER = "/resources/menu/menuControls/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuControls";

	private static final String PARAM_NAME_LEFT = "leftMovement";
	private static final String PARAM_NAME_RIGHT = "rightMovement";
	private static final String PARAM_NAME_JUMP = "jump";
	private static final String PARAM_NAME_USE = "use";

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private static final Color COLOR = Color.black;

	private static final Color INVALID_COLOR = Color.black;

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
		int useKey = (int) this.manager.getParam(this.controlsConf, PARAM_NAME_USE, 0);

		this.wTitle();
		this.wBack();

		this.controls = new WUserKeyInput[4];

		this.controls[0] = this.wLeftMouvementInput();
		this.controls[0].setData(leftMouvementKey);

		this.controls[1] = this.wRightMouvementInput();
		this.controls[1].setData(rightMouvementKey);

		this.controls[2] = this.wJumpInput();
		this.controls[2].setData(jumpKey);

		this.controls[3] = this.wUseInput();
		this.controls[3].setData(useKey);
	}

	private void changeColorOnSame() {
		for (int i = 0; i < this.controls.length; i++) {
			for (int j = 0; j < this.controls.length; j++) {
				if (i == j) {
					continue;
				}
//				if (this.controls[i].getData() == this.controls[j].getData()) {
//					this.controls[j].getTextData().setColor(INVALID_COLOR);
//				} else {
//					this.controls[j].getTextData().setColor(COLOR);
//				}
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

		btn.setPos(new Point(30, 30));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
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

		BorderData border = new BorderData(2, Color.WHITE, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(110, 70));
		rect.setBorder(border);
		u.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(0, 50, 50));
		u.setSelectedDrawElement(rect);

		u.setPos(new Point(420, 300));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 28), "",
				Color.WHITE, 3);

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

		BorderData border = new BorderData(2, Color.WHITE, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(110, 70));
		rect.setBorder(border);
		u.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(0, 50, 50));
		u.setSelectedDrawElement(rect);

		u.setPos(new Point(200, 300));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 28), "",
				Color.WHITE, 3);

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

		BorderData border = new BorderData(2, Color.WHITE, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(110, 70));
		rect.setBorder(border);
		u.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(0, 50, 50));
		u.setSelectedDrawElement(rect);

		u.setPos(new Point(310, 300));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 28), "",
				Color.WHITE, 3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(392, 54));

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

		BorderData border = new BorderData(2, Color.WHITE, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(110, 70));
		rect.setBorder(border);
		u.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(0, 50, 50));
		u.setSelectedDrawElement(rect);

		u.setPos(new Point(530, 300));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 28), "",
				Color.WHITE, 3);

		u.setTextData(td);

		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}
}
