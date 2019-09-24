package fr.state.menu.widget;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

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
	private abstract class LabelDrawer {
		public int prevLabelState;

		public abstract void draw(Graphics2D g, Point absp);
	}

	private Point pos;

	private Point halfSize;

	private AABB hitbox;

	private DrawElement stdDrawElement;

	private DrawElement selectedDrawElement;

	private DrawElement currentDE;

	private boolean selected;

	private boolean visible;

	private LabelDrawer labelDrawer;

	private TextData textData;

	private String data;

	private boolean eraseOnEdit;

	private boolean lostFocusToValidate;

	private String saveData;

	private int dataLength;

	private MenuPage page;

	public WUserInput(MenuPage p) {
		this.page = p;
		this.pos = new Point();

		this.halfSize = new Point();

		this.hitbox = new AABB(this.pos, new Point(), new Point());

		this.visible = true;

		this.currentDE = null;

		this.stdDrawElement = null;

		this.selectedDrawElement = null;

		this.selected = false;

		this.textData = null;
		this.labelDrawer = new LabelDrawer() {
			@Override
			public void draw(Graphics2D g, Point absp) {
			}
		};
		this.labelDrawer.prevLabelState = -1;

		this.data = "";

		this.dataLength = 0;

		this.saveData = "";

		this.eraseOnEdit = false;
		this.lostFocusToValidate = false;
	}

	private void changeLabelDrawer() {
		if (this.labelDrawer.prevLabelState == this.textData.getState())
			return;

		switch (this.textData.getState()) {
		case 0:
			// Relative
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g, Point absp) {
					Point absoluteTextPos = absp.clone().add(WUserInput.this.textData.getPos());

					g.drawString(WUserInput.this.textData.getText(), absoluteTextPos.ix(),
							absoluteTextPos.iy());
				}
			};
			break;
		case 1:
			// X centered - Y relative
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g, Point absp) {
					Point absoluteTextPos = absp.clone().add(WUserInput.this.textData.getPos());

					g.drawString(WUserInput.this.textData.getText(), absp.ix() + WUserInput.this.halfSize.ix()
							- WUserInput.this.textData.getSize().ix() / 2, absoluteTextPos.iy());
				}
			};
			break;
		case 2:
			// X relative - Y centered
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g, Point absp) {
					Point absoluteTextPos = absp.clone().add(WUserInput.this.textData.getPos());

					g.drawString(WUserInput.this.textData.getText(), absoluteTextPos.ix(), absp.iy()
							+ WUserInput.this.halfSize.iy() + WUserInput.this.textData.getSize().iy() / 4);
				}
			};
			break;
		case 3:
			// Centered
			this.labelDrawer = new LabelDrawer() {
				@Override
				public void draw(Graphics2D g, Point absp) {
					g.drawString(WUserInput.this.textData.getText(),
							absp.ix() + WUserInput.this.halfSize.ix()
									- WUserInput.this.textData.getSize().ix() / 2,
							absp.iy() + WUserInput.this.halfSize.iy()
									+ WUserInput.this.textData.getSize().iy() / 4);
				}
			};
			break;
		}
	}

	public abstract void dataChanged(String data);

	@Override
	public void draw(Graphics2D g) {
		if (!this.visible)
			return;

		if (this.currentDE != null) {
			this.currentDE.draw(g, this.getPos());
			this.drawData(g);
		} else {
			Logger.err("Un " + this.getClass().getSimpleName() + " n'a pas de drawElement");
		}
	}

	public void drawData(Graphics2D g) {
		if (this.textData != null) {
			this.textData.setText(this.data);

			g.setFont(this.textData.getFont());
			g.setColor(this.textData.getColor());
			this.labelDrawer.draw(g, this.getPos());
		}
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
		return null;
	}

	/**
	 * @return the drawElement
	 */
	public DrawElement getStdDrawElement() {
		return this.stdDrawElement;
	}

	public TextData getTextData() {
		return this.textData;
	}

	public boolean isEraseOnEdit() {
		return this.eraseOnEdit;
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
		this.halfSize.set(this.currentDE.getSize().clone().div(2));
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
		this.selectedDrawElement = selectedDrawElement;
	}

	/**
	 * @param drawElement the drawElement to set
	 */
	public void setStdDrawElement(DrawElement drawElement) {
		if (drawElement != null) {
			drawElement.lock();
		}

		this.stdDrawElement = drawElement;
		if (this.currentDE == null) {
			this.currentDE = this.stdDrawElement;
		}
	}

	public void setTextData(TextData textData) {
		this.textData = textData;
		this.changeLabelDrawer();
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
				String t = KeyEvent.getKeyText(e.key);
				if (t.length() > 1) {
					t = "";
				}
				if (e.pressed) {
					if (e.key == 8 && this.data.length() > 0) {
						// DELETE
						this.data = this.data.substring(0, this.data.length() - 1);
					} else if (e.key == 10) {
						// ENTER
						this.setSelected(false);
						if (this.lostFocusToValidate) {
							this.dataChanged(this.data);
						}
					} else if (this.data.length() < this.dataLength) {
						if (e.key == 32) {
							// SPACE
							t = " ";
						}
						this.data += t;
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
