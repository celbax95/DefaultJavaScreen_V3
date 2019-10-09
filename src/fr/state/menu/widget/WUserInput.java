package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.inputs.keyboard.KeyboardEvent;
import fr.inputs.mouse.MouseEvent;
import fr.logger.Logger;
import fr.state.menu.MenuPage;
import fr.state.menu.Widget;
import fr.util.collider.AABB;
import fr.util.collider.Collider;
import fr.util.point.Point;

public abstract class WUserInput implements Widget {
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

	private String data;

	private boolean eraseOnEdit;

	private boolean lostFocusToValidate;

	private String saveData;

	private int dataLength;

	private MenuPage page;

	public WUserInput(MenuPage p) {
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

		this.data = "";

		this.dataLength = 0;

		this.saveData = "";

		this.eraseOnEdit = false;
		this.lostFocusToValidate = false;
	}

	public WUserInput(WUserInput other) {
		this(other.page);

		this.setPos(other.pos);
		this.setHitbox(other.hitbox);
		this.setStdDrawElement(other.stdDrawElement);
		this.setSelectedDrawElement(other.selectedDrawElement);
		this.setCurrentDE();
		this.setVisible(other.visible);
		this.setTextData(other.originalTextData);
		this.setData("");
		this.setEraseOnEdit(other.eraseOnEdit);
		this.setLostFocusToValidate(other.lostFocusToValidate);
		this.setDataLength(other.dataLength);
	}

	private void addCharToData(char keyChar) {
		String tmpData = this.data + keyChar;
		if (this.changeFontSize(tmpData)) {
			this.data = tmpData;
		}
	}

	private boolean changeFontSize(String tmpData) {
		TextData tmpt = new TextData(this.originalTextData);
		tmpt.setText(tmpData);

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

	public abstract void dataChanged(String data);

	@Override
	public void draw(Graphics2D g) {
		if (!this.visible)
			return;

		if (this.currentDE != null) {
			this.currentDE.draw(g, this.getPos());

			if (this.currentTextData != null) {
				if (this.currentTextData != null) {
					this.currentTextData.setText(this.data);
					this.currentTextData.draw(g, this.pos, this.size);
				}
			}
		} else {
			Logger.err("Un " + this.getClass().getSimpleName() + " n'a pas de drawElement");
		}
	}

	public String getData() {
		return this.data;
	}

	public int getDataLength() {
		return this.dataLength;
	}

	public AABB getHitbox() {
		return this.hitbox;
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

	public boolean isEraseOnEdit() {
		return this.eraseOnEdit;
	}

	public boolean isLostFocusToValidate() {
		return this.lostFocusToValidate;
	}

	public boolean isSelected() {
		return this.selected;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	private void removeCharToData() {
		String tmpData = this.data.substring(0, this.data.length() - 1);

		this.changeFontSize(tmpData);
		this.data = tmpData;
	}

	private void setCurrentDE() {
		if (!this.selected) {
			this.currentDE = this.stdDrawElement;
		} else if (this.selectedDrawElement != null) {
			this.currentDE = this.selectedDrawElement;
		}
		this.setSize(this.currentDE.getSize().clone());
	}

	public void setData(String data) {
		this.data = data;
		this.changeFontSize(data);
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public void setEraseOnEdit(boolean eraseOnEdit) {
		this.eraseOnEdit = eraseOnEdit;
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

	public void setLostFocusToValidate(boolean lostFocusToValidate) {
		this.lostFocusToValidate = lostFocusToValidate;
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

			if (this.eraseOnEdit) {
				if (selected) {
					this.saveData = "" + this.data;
					this.data = "";
				} else {
					if (this.data.equals(this.saveData)) {
						this.data = "" + this.saveData;
					}
				}
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

			this.setCurrentDE();
		}

	}

	public void setTextData(TextData textData) {
		if (this.currentTextData == null) {
			this.currentTextData = textData;
		}
		this.originalTextData = new TextData(textData);
		this.changeFontSize(this.data);
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
				if (Collider.AABBvsPoint(this.hitbox, e.pos)) {
					this.setSelected(true);
				} else {
					this.setSelected(false);
				}
				continue;
			}
		}

		if (this.selected) {
			for (KeyboardEvent e : input.keyboardEvents) {
				if (e.pressed) {
					if (e.key == 8) {
						// DELETE
						if (this.data.length() > 0) {
							this.removeCharToData();
							this.dataChanged(this.data);
						}
					} else if (e.key == 10) {
						// ENTER
						this.setSelected(false);
						if (this.lostFocusToValidate) {
							this.dataChanged(this.data);
						}
					} else if (this.data.length() < this.dataLength && e.keyChar != Character.MIN_VALUE) {
						this.addCharToData(e.keyChar);
						if (!this.lostFocusToValidate) {
							this.dataChanged(this.data);
						}
						this.data.replace("  ", " ");
					}
				}
			}
		}
	}
}
