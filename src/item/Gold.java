package item;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import window.ImageManager;

public class Gold extends Item{

	private static BufferedImage[] sprites;

	public Gold(int x, int y) {
		super("Gold");
		this.setAbsolueX(x);
		this.setAbsolueY(y);
		this.setAbsolueHeight(16);
		this.setAbsolueWidth(16);
		if(sprites == null){
			sprites = ImageManager.loadSprites("gold", 56, 64);
		}
		this.setSpeedAnimation(10F);
	}

	public void paint(Graphics g){
		super.paintAnimation(g, sprites);
	}
}
