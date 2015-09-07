package item;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import window.ImageManager;

public class Pill extends Medication {


	public Pill() {
		super("Pill", -1, false);
		this.setAbsolueWidth(32);
		this.setAbsolueHeight(32);
	}


	public Pill(int value) {
		super("pill", Math.abs(value), false);
		this.setAbsolueWidth(32);
		this.setAbsolueHeight(32);
	}


	public void paint(Graphics g){
		BufferedImage img = ImageManager.images.get(this.getName().toLowerCase());
		if(img != null){
			g.drawImage(img, this.getAbsolueX(), this.getAbsolueY(), this.getAbsolueWidth(), this.getAbsolueHeight(), null);
		}else{
			g.setColor(Color.red);
			g.fillRect(this.getAbsolueX(), this.getAbsolueY(), this.getAbsolueWidth(), this.getAbsolueHeight());
		}
	}
}
