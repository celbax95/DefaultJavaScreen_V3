package fr.state.menu.page;

import java.awt.Color;
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
import fr.state.menu.widget.WSlider;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuGameSettings implements MenuPage {

	private static final String[] RES_NAMES = { "title" };

	private static final String[] RES_PATHS = { "title" };

	private static final String RES_FOLDER = "/resources/menu/menuGameSettings/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuGameSettings";

	private static final String PARAM_NAME_ITEM = "itemFreq";

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

	private Object gameSettingsConf;

	private XMLManager manager;

	public MenuGameSettings(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		this.loadResources();

		DatafilesManager dfm = DatafilesManager.getInstance();
		this.gameSettingsConf = dfm.getFile("gameSettings");
		this.manager = dfm.getXmlManager();

		int itemFreq = (int) this.manager.getParam(this.gameSettingsConf, PARAM_NAME_ITEM, 0);

		this.wTitle();
		this.wBack();
		this.wFreqItem().setValue(itemFreq);

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
				MenuGameSettings.this.m.applyPage(new MenuSettings(MenuGameSettings.this.m));
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

	private WSlider wFreqItem() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				if (!pressed) {
					MenuGameSettings.this.manager.setParam(MenuGameSettings.this.gameSettingsConf,
							PARAM_NAME_ITEM, value);
					MenuGameSettings.this.manager.saveFile(MenuGameSettings.this.gameSettingsConf);
				}
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(400, 6));
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.RED);
		slider.setSize(new Point(12, 50));
		s.setSlider(slider);

		s.setPos(new Point(200, 400));
		s.setScope(100);
		s.setFreeMove(true);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);

		return s;
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(392, 54));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get("menuSettings/title"));

		title.setDrawElement(i);

		this.widgets.add(title);
	}
}