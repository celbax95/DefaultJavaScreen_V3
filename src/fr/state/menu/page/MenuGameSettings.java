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

	private static final String[] RES_NAMES = { "title", "backStd", "backPressed", "frame", "itemFreq", };

	private static final String[] RES_PATHS = { "title", "backStd", "backPressed", "frame", "itemFreq", };

	private static final String RES_FOLDER = "/resources/menu/menuGameSettings/";

	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menu/gameSettings";
	private static final String PARAM_NAME_ITEM = "itemFreq";

	static {
		for (int i = 0; i < RES_NAMES.length; i++) {
			RES_NAMES[i] = PAGE_NAME + "/" + RES_NAMES[i];
		}

		for (int i = 0; i < RES_PATHS.length; i++) {
			RES_PATHS[i] = RES_FOLDER + RES_PATHS[i] + RES_EXTENSION;
		}
	}

	private boolean loaded;

	private List<Widget> widgets;

	private Menu m;

	private Object gameSettingsConf;

	private XMLManager manager;

	public MenuGameSettings(Menu m) {
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
				MenuGameSettings.this.widgets = new Vector<>();

				MenuGameSettings.this.loadResources();

				DatafilesManager dfm = DatafilesManager.getInstance();
				MenuGameSettings.this.gameSettingsConf = dfm.getFile("gameSettings");
				MenuGameSettings.this.manager = dfm.getXmlManager();

				int itemFreq = (int) MenuGameSettings.this.manager.getParam(MenuGameSettings.this.gameSettingsConf,
						PARAM_NAME_ITEM, 0);

				MenuGameSettings.this.wTitle();
				MenuGameSettings.this.wBack();
				MenuGameSettings.this.wFrame();
				MenuGameSettings.this.wItemFreq();
				MenuGameSettings.this.wItemFreqSlider().setValue(itemFreq);

				MenuGameSettings.this.loaded = true;
			}
		}).start();
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

	private void wFrame() {
		WElement w = new WElement(this);

		DEImage img = new DEImage();

		img.setImage(ImageManager.getInstance().get(PAGE_NAME + "/frame"));

		w.setDrawElement(img);
		w.setPos(new Point(333, 337));

		this.widgets.add(w);
	}

	private void wItemFreq() {
		WElement w = new WElement(this);

		DEImage img = new DEImage();

		img.setImage(ImageManager.getInstance().get(PAGE_NAME + "/itemFreq"));

		w.setDrawElement(img);
		w.setPos(new Point(738, 400));

		this.widgets.add(w);
	}

	private WSlider wItemFreqSlider() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				if (!pressed) {
					MenuGameSettings.this.manager.setParam(MenuGameSettings.this.gameSettingsConf, PARAM_NAME_ITEM,
							value);
					MenuGameSettings.this.manager.saveFile(MenuGameSettings.this.gameSettingsConf);
				}
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(460, 8));
		bar.setRoundBorder(3);
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.RED);
		slider.setSize(new Point(16, 60));
		slider.setRoundBorder(8);
		s.setSlider(slider);

		s.setPos(new Point(730, 550));
		s.setScope(100);
		s.setFreeMove(true);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);

		return s;
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
