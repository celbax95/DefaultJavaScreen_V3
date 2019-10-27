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
import fr.state.menu.widget.WSlider;
import fr.state.menu.widget.WUserInput;
import fr.state.menu.widget.data.BorderData;
import fr.state.menu.widget.data.TextData;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuProfile implements MenuPage {

	private static final String[] RES_NAMES = {
			"title",
			"backStd",
			"backPressed",
			"frame",
			"usernameInputStd",
			"usernameInputSel",
			"colorSelect", };

	private static final String[] RES_PATHS = {
			"title",
			"backStd",
			"backPressed",
			"frame",
			"usernameInputStd",
			"usernameInputSel",
			"colorSelect", };

	private static final String RES_FOLDER = "/resources/menu/menuProfile/";
	private static final String RES_EXTENSION = ".png";

	private static final String PAGE_NAME = "menuProfile";

	private static final String PARAM_NAME_USERNAME = "username";
	private static final String PARAM_NAME_COLOR = "color";
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
	private Object profileConf;

	private XMLManager manager;

	private Color color;

	private WElement colorBlock;

	public MenuProfile(Menu m) {
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

				MenuProfile.this.widgets = new Vector<>();

				MenuProfile.this.loadResources();

				DatafilesManager dfm = DatafilesManager.getInstance();
				MenuProfile.this.profileConf = dfm.getFile("profile");
				MenuProfile.this.manager = dfm.getXmlManager();

				String username = (String) MenuProfile.this.manager.getParam(MenuProfile.this.profileConf,
						PARAM_NAME_USERNAME, 0);
				String colorHex = (String) MenuProfile.this.manager.getParam(MenuProfile.this.profileConf,
						PARAM_NAME_COLOR, 0);

				MenuProfile.this.color = Color.decode(colorHex);

				MenuProfile.this.wTitle();
				MenuProfile.this.wBack();
				MenuProfile.this.wFrame();
				MenuProfile.this.wUsernameInput().setData(username);
				MenuProfile.this.wColorRed().setValue(MenuProfile.this.color.getRed());
				MenuProfile.this.wColorGreen().setValue(MenuProfile.this.color.getGreen());
				MenuProfile.this.wColorBlue().setValue(MenuProfile.this.color.getBlue());
				MenuProfile.this.colorBlock = MenuProfile.this.wColorBlock();
				MenuProfile.this.wColorSelect();
				MenuProfile.this.setColor(MenuProfile.this.color);

				MenuProfile.this.loaded = true;
			}
		}).start();
	}

	private void loadResources() {
		ImageLoader il = new ImageLoader();

		il.load(RES_NAMES, RES_PATHS);
	}

	private void saveColor() {
		String colorHex = String.format("#%02x%02x%02x", this.color.getRed(), this.color.getGreen(),
				this.color.getBlue());

		this.manager.setParam(this.profileConf, PARAM_NAME_COLOR, colorHex);
		this.manager.saveFile(this.profileConf);
	}

	/**
	 * @param color the color to set
	 */
	private void setColor(Color color) {
		this.color = color;
		((DERectangle) this.colorBlock.getDrawElement()).setColor(this.color);
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
				MenuProfile.this.m.applyPage(new MenuSettings(MenuProfile.this.m));
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

	private WSlider wColorBlue() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				MenuProfile.this
						.setColor(new Color(MenuProfile.this.color.getRed(), MenuProfile.this.color.getGreen(), value));
				if (!pressed) {
					MenuProfile.this.saveColor();
				}
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(400, 8));
		bar.setRoundBorder(3);
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.BLUE);
		slider.setSize(new Point(16, 60));
		slider.setRoundBorder(8);
		s.setSlider(slider);

		s.setPos(new Point(1110, 845));
		s.setScope(255);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);

		return s;
	}

	private WSlider wColorGreen() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				MenuProfile.this
						.setColor(new Color(MenuProfile.this.color.getRed(), value, MenuProfile.this.color.getBlue()));
				if (!pressed) {
					MenuProfile.this.saveColor();
				}
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(400, 8));
		bar.setRoundBorder(3);
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.GREEN);
		slider.setSize(new Point(16, 60));
		slider.setRoundBorder(8);
		s.setSlider(slider);

		s.setPos(new Point(1110, 719));
		s.setScope(255);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);

		return s;
	}

	private WSlider wColorRed() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				MenuProfile.this.setColor(
						new Color(value, MenuProfile.this.color.getGreen(), MenuProfile.this.color.getBlue()));
				if (!pressed) {
					MenuProfile.this.saveColor();
				}
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(400, 8));
		bar.setRoundBorder(3);
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.RED);
		slider.setSize(new Point(16, 60));
		slider.setRoundBorder(8);
		s.setSlider(slider);

		s.setPos(new Point(1110, 585));
		s.setScope(255);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);

		return s;
	}

	private void wColorSelect() {
		WElement w = new WElement(this);

		DEImage img = new DEImage();
		img.setImage(ImageManager.getInstance().get(PAGE_NAME + "/colorSelect"));

		w.setDrawElement(img);

		w.setPos(new Point(1012, 526));

		this.widgets.add(w);
	}

	private void wFrame() {
		WElement w = new WElement(this);

		DEImage img = new DEImage();

		img.setImage(ImageManager.getInstance().get(PAGE_NAME + "/frame"));

		w.setDrawElement(img);
		w.setPos(new Point(333, 337));

		this.widgets.add(w);
	}

	private void wTitle() {
		WElement title = new WElement(this);

		title.setPos(new Point(550, 76));

		DEImage i = new DEImage();
		i.setImage(ImageManager.getInstance().get(PAGE_NAME + "/title"));

		title.setDrawElement(i);

		this.widgets.add(title);
	}

	private WUserInput wUsernameInput() {
		WUserInput u = new WUserInput(this) {
			@Override
			public void dataChanged(String data) {
				System.out.println(data);
				MenuProfile.this.manager.setParam(MenuProfile.this.profileConf, PARAM_NAME_USERNAME, data);
				MenuProfile.this.manager.saveFile(MenuProfile.this.profileConf);
			}
		};

		ImageManager im = ImageManager.getInstance();

		DEImage std = new DEImage();
		std.setImage(im.get(PAGE_NAME + "/usernameInputStd"));
		DEImage sel = new DEImage();
		sel.setImage(im.get(PAGE_NAME + "/usernameInputSel"));

		u.setStdDrawElement(std);

		u.setSelectedDrawElement(sel);

		u.setPos(new Point(434, 624));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 45), "", Color.BLACK,
				3);

		u.setTextData(td);

		u.setDataLength(20);
		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}
}
