package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import window.ImageManager;
import map.Room;

public class Explosion extends Entity{

	private static BufferedImage[] sprites;

	public Explosion(Room pos, int x, int y) {
		super(pos);
		this.setAbsolueX(x);
		this.setAbsolueY(y);
		if(sprites == null){
			sprites = ImageManager.loadSprites("explosion", 64, 64);
		}
		this.setAbsolueHeight(64);
		this.setAbsolueWidth(64);
		this.setSpeedAnimation(7F);
	}
	
	public void paint(Graphics g){
		super.paintAnimation(g, sprites);
	}

}
