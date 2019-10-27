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
import fr.serverlink.data.ServerData;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.data.BorderData;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuServerSettings implements MenuPage {

	private static final String[] RES_NAMES = { "title", "backStd", "backPressed" };

	private static final String[] RES_PATHS = { "title", "backStd", "backPressed" };

	private static final String RES_FOLDER = "/resources/menu/menuGraphics/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuSeverSettings";

	private static final int NB_EXCLUSIVE_BTN = ServerData.getIPAmount();

	private static final Color ENABLE = Color.GRAY, DISABLE = Color.LIGHT_GRAY;

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
		new Thread(new Runnable() {
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

				for (int i = 0; i < NB_EXCLUSIVE_BTN; i++) {
					MenuServerSettings.this.exclusiveBtn[i] = MenuServerSettings.this.wServerIDChoose("" + i,
							new Point(480 + 210 * i, 600), i);
				}

				MenuServerSettings.this.setBtnIndex(
						(int) MenuServerSettings.this.manager.getParam(MenuServerSettings.this.serverConf, "id", 0));

				MenuServerSettings.this.loaded = true;
			}
		}).start();
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
	}

	private void setBtnIndex(int index) {
		if (index != this.btnIndex) {
			this.btnIndex = index;
			this.switchSelBtn();
			this.m.getMenuState().getStatePanel().getWinData().setNeedRestart(true);
		}
	}

	private void switchSelBtn() {
		for (WButton w : this.exclusiveBtn) {
			w.setCanPressed(true);
			((DERectangle) w.getStdDrawElement()).setColor(ENABLE);
		}
		((DERectangle) this.exclusiveBtn[this.btnIndex].getStdDrawElement()).setColor(DISABLE);

		this.exclusiveBtn[this.btnIndex].setCanPressed(false);
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

	private WButton wServerIDChoose(String text, Point pos, int id) {
		WButton w = new WButton(this) {

			@Override
			public void action() {
				MenuServerSettings.this.manager.setParam(MenuServerSettings.this.serverConf, "id", id);
				MenuServerSettings.this.manager.saveFile(MenuServerSettings.this.serverConf);

				MenuServerSettings.this.setBtnIndex(id);
			}
		};

		DERectangle r = new DERectangle();

		r.setSize(new Point(200, 100));
		r.setColor(new Color(130, 130, 130));
		TextData td = new TextData(new Point(), new Font("Arial", Font.BOLD, 30), text, Color.BLACK, 3);
		r.setLabel(td);
		r.setBorder(new BorderData(3, Color.BLACK, 2));

		w.setStdDrawElement(r.clone());

		r.setColor(new Color(160, 160, 160));
		w.setPressedDrawElement(r);

		w.setPos(pos.clone());
		w.setHitboxFromDrawElement();

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
