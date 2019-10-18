package fr.state.menu.page;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

import fr.datafilesmanager.XMLManager;
import fr.imagesmanager.ImageLoader;
import fr.imagesmanager.ImageManager;
import fr.inputs.Input;
import fr.state.menu.Menu;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.WButton;
import fr.state.menu.widget.WElement;
import fr.state.menu.widget.data.BorderData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuHost implements MenuPage {

	private static final String[] RES_NAMES = { "title", "backStd", "backPressed" };

	private static final String[] RES_PATHS = { "title", "backStd", "backPressed" };

	private static final String RES_FOLDER = "/resources/menu/menuHost/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuHost";

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private List<Widget> widgets;

	private Menu m;

	private Object serverConf;

	private XMLManager manager;

	public MenuHost(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		this.loadResources();

//		DatafilesManager dfm = DatafilesManager.getInstance();
//		this.serverConf = dfm.getFile("profile");
//		this.manager = dfm.getXmlManager();
//
//		String colorHex = (String) this.manager.getParam(this.profileConf, PARAM_NAME_COLOR, 0);

		this.wTitle();
		this.wBack();
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
				MenuHost.this.m.applyPage(new MenuMain(MenuHost.this.m));
			}
		};

		btn.setPos(new Point(42, 42));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/backStd"));

		btn.setStdDrawElement(i);

		i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/backPressed"));

		btn.setPressedDrawElement(i);

		btn.setHitboxFromDrawElement();

		this.widgets.add(btn);
	}

	private WElement wColorBlock() {
		BorderData border = new BorderData(4, Color.BLACK, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(52, 380));
		rect.setBorder(border);

		WElement w = new WElement(this);
		w.setDrawElement(rect);
		w.setPos(new Point(1014, 528));

		this.widgets.add(w);

		return w;
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
