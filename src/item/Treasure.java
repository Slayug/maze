package item;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import window.ImageManager;


public class Treasure extends Item {

	public Treasure() {
		super("Trésor");

		this.setAbsolueWidth(69);
		this.setAbsolueHeight(48);
	}

	public void paint(Graphics g){
		BufferedImage img = ImageManager.images.get("chest");
		if(img != null){
			g.drawImage(img, this.getAbsolueX(), this.getAbsolueY(), this.getAbsolueWidth(), this.getAbsolueHeight(), null);
		}
	}
	
}
