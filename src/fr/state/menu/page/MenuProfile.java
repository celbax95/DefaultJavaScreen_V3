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
import fr.state.menu.widget.WSlider;
import fr.state.menu.widget.WUserInput;
import fr.state.menu.widget.drawelements.DEImage;
import fr.state.menu.widget.drawelements.DERectangle;
import fr.util.point.Point;

public class MenuProfile implements MenuPage {

	private static final String[] RES_NAMES = { "title" };

	private static final String[] RES_PATHS = { "title" };

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

	private List<Widget> widgets;

	private Menu m;

	private Object profileConf;

	private XMLManager manager;

	private Color color;

	public MenuProfile(Menu m) {

		this.m = m;

		this.widgets = new Vector<>();

		this.loadResources();

		DatafilesManager dfm = DatafilesManager.getInstance();
		this.profileConf = dfm.getFile("profile");
		this.manager = dfm.getXmlManager();

		String username = (String) this.manager.getParam(this.profileConf, PARAM_NAME_USERNAME, 0);
		String colorHex = (String) this.manager.getParam(this.profileConf, PARAM_NAME_COLOR, 0);

		this.color = Color.decode(colorHex);

		this.wTitle();
		this.wBack();
		this.wUsernameInput().setData(username);
		this.wColorRed().setValue(this.color.getRed());
		this.wColorGreen().setValue(this.color.getGreen());
		this.wColorBlue().setValue(this.color.getBlue());
	}

	@Override
	public void draw(Graphics2D g) {
		for (Widget w : this.widgets) {
			w.draw(g);
		}

		g.setColor(this.color);
		g.fillRect(650, 500, 100, 100);
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

	private WSlider wColorBlue() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				MenuProfile.this.color = new Color(MenuProfile.this.color.getRed(),
						MenuProfile.this.color.getGreen(), value);
				if (!pressed) {
					MenuProfile.this.saveColor();
				}
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(400, 6));
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.BLUE);
		slider.setSize(new Point(12, 50));
		s.setSlider(slider);

		s.setPos(new Point(200, 590));
		s.setScope(255);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);

		return s;
	}

	private WSlider wColorGreen() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				MenuProfile.this.color = new Color(MenuProfile.this.color.getRed(), value,
						MenuProfile.this.color.getBlue());
				if (!pressed) {
					MenuProfile.this.saveColor();
				}
			}
		};

		DERectangle bar = new DERectangle();

		bar.setColor(Color.WHITE);
		bar.setSize(new Point(400, 6));
		s.setBar(bar);

		DERectangle slider = new DERectangle();

		slider.setColor(Color.GREEN);
		slider.setSize(new Point(12, 50));
		s.setSlider(slider);

		s.setPos(new Point(200, 520));
		s.setScope(255);
		s.setHitboxFromDrawElement();

		this.widgets.add(s);

		return s;
	}

	private WSlider wColorRed() {
		WSlider s = new WSlider(this) {
			@Override
			public void valueChanged(int value, boolean pressed) {
				MenuProfile.this.color = new Color(value, MenuProfile.this.color.getGreen(),
						MenuProfile.this.color.getBlue());
				if (!pressed) {
					MenuProfile.this.saveColor();
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

		s.setPos(new Point(200, 450));
		s.setScope(255);
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

	private WUserInput wUsernameInput() {
		WUserInput u = new WUserInput(this) {
			@Override
			public void dataChanged(String data) {
				MenuProfile.this.manager.setParam(MenuProfile.this.profileConf, "username", data);
				MenuProfile.this.manager.saveFile(MenuProfile.this.profileConf);
			}
		};

		BorderData border = new BorderData(2, Color.WHITE, 0);

		DERectangle rect = new DERectangle();
		rect.setColor(Color.BLACK);
		rect.setSize(new Point(300, 60));
		rect.setBorder(border);
		u.setStdDrawElement(rect);

		rect = (DERectangle) rect.clone();
		rect.setColor(new Color(0, 50, 50));
		u.setSelectedDrawElement(rect);

		u.setPos(new Point(200, 300));

		TextData td = new TextData(new Point(), new Font("Copperplate Gothic Bold", Font.PLAIN, 35), "",
				Color.WHITE, 3);

		u.setTextData(td);

		u.setDataLength(20);
		u.setHitboxFromDrawElement();

		this.widgets.add(u);

		return u;
	}
}
