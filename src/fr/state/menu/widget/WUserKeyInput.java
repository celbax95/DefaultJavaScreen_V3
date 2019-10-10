package fr.state.menu.widget;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.inputs.Input;
import fr.inputs.keyboard.KeyboardEvent;
import fr.inputs.mouse.MouseEvent;
import fr.logger.Logger;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.state.menu.widget.data.TextData;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WUserKeyInput implements Widget {
	private static final int DATA_MOUSE_LEFT = -1;
	private static final int DATA_MOUSE_MIDDLE = -2;
	private static final int DATA_MOUSE_RIGHT = -3;

	private static final Map<Integer, String> MOUSE_LABELS;

	private static final List<Integer> EXCLUDED_KEYS;

	private static final String DEFAULT_LABEL_ON_SELECT = "-?-";

	static {
		MOUSE_LABELS = new HashMap<>();
		MOUSE_LABELS.put(DATA_MOUSE_LEFT, "Mouse Left");
		MOUSE_LABELS.put(DATA_MOUSE_MIDDLE, "Mouse Middle");
		MOUSE_LABELS.put(DATA_MOUSE_RIGHT, "Mouse Right");

		EXCLUDED_KEYS = new ArrayList<>();
		EXCLUDED_KEYS.add(0); // NULL
		EXCLUDED_KEYS.add(524); // WIN KEY
	}

	private final int PADDING = 10;

	private final int MIN_FONT_SIZE = 10;

	private Point pos;

	private Point size;

	private AABB hitbox;

	private DrawElement stdDrawElement;

	private DrawElement selectedDrawElement;

	private DrawElement currentDE;

	private boolean selected;

	private boolean visible;

	private TextData currentTextData;

	private TextData originalTextData;

	private String labelOnSelect;

	private int data;

	private String label;

	private MenuPage page;

	public WUserKeyInput(MenuPage p) {
		this.page = p;
		this.pos = new Point();

		this.size = new Point();

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.visible = true;

		this.currentDE = null;

		this.stdDrawElement = null;

		this.selectedDrawElement = null;

		this.selected = false;

		this.currentTextData = null;

		this.originalTextData = null;

		this.labelOnSelect = DEFAULT_LABEL_ON_SELECT;

		this.data = 0;
		this.label = "";
	}

	public WUserKeyInput(WUserKeyInput other) {
		this(other.page);

		this.setPos(other.pos);
		this.setHitbox(other.hitbox);
		this.setStdDrawElement(other.stdDrawElement);
		this.setSelectedDrawElement(other.selectedDrawElement);
		this.setVisible(other.visible);
		this.setTextData(other.originalTextData);
		this.setLabelOnSelect(other.labelOnSelect);
	}

	private void changeData(int key) {

		this.setData(key);
		this.dataChanged(this.data);
		System.out.println(this.currentTextData.getColor());
	}

	private boolean changeFontSize() {
		TextData tmpt = new TextData(this.originalTextData);
		tmpt.setText(this.label);

		float newFontSize = -1;

		float step = 1f;

		while (tmpt.getSize().ix() >= this.stdDrawElement.getSize().ix() - this.PADDING * 2) {
			newFontSize = tmpt.getFont().getSize() - step;

			if (newFontSize > this.MIN_FONT_SIZE) {
				tmpt.setFont(tmpt.getFont().deriveFont(newFontSize));
			} else {
				break;
			}
		}

		if (newFontSize > this.MIN_FONT_SIZE) {
			tmpt.setFont(tmpt.getFont().deriveFont(newFontSize - 1));
		}

		if (newFontSize == -1 || newFontSize > this.MIN_FONT_SIZE) {
			this.currentTextData.setFont(tmpt.getFont());
			return true;
		}

		return false;
	}

	public abstract void dataChanged(int data);

	@Override
	public void draw(Graphics2D g) {
		if (!this.visible)
			return;

		if (this.currentDE != null) {
			this.currentDE.draw(g, this.getPos());

			if (this.currentTextData != null) {

				if (this.selected) {
					this.originalTextData.setText(this.labelOnSelect);
					this.originalTextData.draw(g, this.pos, this.size);
				} else {
					this.currentTextData.setText(this.label);
					this.currentTextData.draw(g, this.pos, this.size);
				}
			}
		} else {
			Logger.err("Un " + this.getClass().getSimpleName() + " n'a pas de drawElement");
		}
	}

	public int getData() {
		return this.data;
	}

	public AABB getHitbox() {
		return this.hitbox;
	}

	public String getLabel() {
		return this.label;
	}

	public String getLabelOnSelect() {
		return this.labelOnSelect;
	}

	public MenuPage getPage() {
		return this.page;
	}

	public Point getPos() {
		return this.pos;
	}

	public DrawElement getSelectedDrawElement() {
		return this.selectedDrawElement;
	}

	public Point getSize() {
		return this.size;
	}

	/**
	 * @return the drawElement
	 */
	public DrawElement getStdDrawElement() {
		return this.stdDrawElement;
	}

	public TextData getTextData() {
		return this.currentTextData;
	}

	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	private void setCurrentDE() {
		if (!this.selected) {
			this.currentDE = this.stdDrawElement;
		} else if (this.selectedDrawElement != null) {
			this.currentDE = this.selectedDrawElement;
		}
		this.setSize(this.currentDE.getSize().clone());
	}

	public void setData(int key) {
		this.data = key;
		if (MOUSE_LABELS.containsKey(this.data)) {
			this.setLabel(MOUSE_LABELS.get(this.data));
		} else if (this.data > 0) {
			this.setLabel(KeyEvent.getKeyText(this.data));
		} else {
			this.setLabel("None");
		}
		this.setSelected(false);
	}

	public void setHitbox(AABB hitbox) {
		this.hitbox = hitbox;
	}

	public void setHitboxFromDrawElement() {
		if (this.currentDE == null)
			return;

		this.hitbox.min(this.pos.clone().add(this.currentDE.getPos()));
		this.hitbox.max(this.pos.clone().add(this.currentDE.getPos()).add(this.currentDE.getSize()));
	}

	public void setLabel(String label) {
		this.label = label;
		this.changeFontSize();
	}

	public void setLabelOnSelect(String labelOnSelect) {
		this.labelOnSelect = labelOnSelect;
	}

	public void setPage(MenuPage page) {
		this.page = page;
	}

	public void setPos(Point pos) {
		this.pos.set(pos);
	}

	public void setSelected(boolean selected) {
		if (selected != this.selected) {
			this.selected = selected;
			this.setCurrentDE();

			if (selected) {
				this.label = this.labelOnSelect;
			}
		}
	}

	public void setSelectedDrawElement(DrawElement selectedDrawElement) {
		if (selectedDrawElement != null) {
			this.selectedDrawElement = selectedDrawElement.clone();
			this.selectedDrawElement.lock();
		}
	}

	private void setSize(Point size) {
		this.size = size;
	}

	/**
	 * @param drawElement the drawElement to set
	 */
	public void setStdDrawElement(DrawElement drawElement) {
		if (drawElement != null) {
			this.stdDrawElement = drawElement.clone();
			this.stdDrawElement.lock();
		}

		this.setCurrentDE();
	}

	public void setTextData(TextData textData) {
		if (this.currentTextData == null) {
			this.currentTextData = textData;
		}
		this.originalTextData = new TextData(textData);
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public void update(Input input) {
		if (!this.visible)
			return;

		for (MouseEvent e : input.mouseEvents) {
			switch (e.id) {
			case MouseEvent.LEFT_PRESSED:
				if (!this.selected && Collider.AABBvsPoint(this.hitbox, e.pos)) {
					this.setSelected(true);
				} else if (this.selected) {
					this.changeData(DATA_MOUSE_LEFT);
				}
				continue;
			case MouseEvent.MIDDLE_PRESSED:
				if (this.selected) {
					this.changeData(DATA_MOUSE_MIDDLE);
				}
				continue;
			case MouseEvent.RIGHT_PRESSED:
				if (this.selected) {
					this.changeData(DATA_MOUSE_RIGHT);
				}
				continue;
			}
		}

		if (this.selected) {
			for (KeyboardEvent e : input.keyboardEvents) {
				if (e.pressed && !EXCLUDED_KEYS.contains(e.key)) {
					if (e.key == 27) {
						this.changeData(0);
					} else {
						this.changeData(e.key);
					}
				}
			}
		}
	}
}
