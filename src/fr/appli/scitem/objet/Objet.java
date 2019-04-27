package fr.appli.scitem.objet;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import fr.appli.appli.Appli;
import fr.appli.appli.SCItem;
import fr.screen.keyboard.KeyBoard;
import fr.util.pointd.PointCalc;
import fr.util.pointd.PointD;
import fr.util.time.Timer;

@SuppressWarnings("unused")
public class Objet implements SCItem {
	private Appli app;
	private String itemType = Objet.class.toString().toLowerCase();
	private Thread myThread;
	private PointD pos;
	private PointD size;
	private double speed;
	private Timer t;

	public Objet(PointD p, PointD s, double sp, Appli a) {
		pos = p;
		size = s;
		app = a;
		speed = sp;
		t = new Timer();
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(pos.getIntX(), pos.getIntY(), size.getIntX(), size.getIntY());
	}

	@Override
	public Appli getAppli() {
		return app;
	}

	@Override
	public String getSCItemType() {
		return itemType;
	}

	private void move(double time) {
		PointD dir = new PointD();
		if (KeyBoard.isPressed(KeyEvent.VK_Z)) {
			dir.y = -1;
		}
		if (KeyBoard.isPressed(KeyEvent.VK_S)) {
			dir.y = 1;
		}
		if (KeyBoard.isPressed(KeyEvent.VK_Q)) {
			dir.x = -1;
		}
		if (KeyBoard.isPressed(KeyEvent.VK_D)) {
			dir.x = 1;
		}
		dir = PointCalc.mult(PointCalc.mult(dir, speed), time);
		pos.translate(dir.x, dir.y);
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			t.tick();
			move(t.lastTick());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public void start() {
		(myThread = new Thread(this)).start();
	}

}