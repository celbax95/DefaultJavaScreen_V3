package fr.appli.appli;

import java.awt.Graphics2D;

public interface SCItem extends Runnable {
	public void draw(Graphics2D g);

	public Appli getAppli();

	public String getSCItemType();

	public void start();
}
