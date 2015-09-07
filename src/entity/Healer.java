package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import window.ImageManager;
import map.Room;

public class Healer extends Entity{

	private static BufferedImage[] sprites;

	public Healer(Room pos) {
		super(pos);

		this.setAbsolueHeight(113);
		this.setAbsolueWidth(64);
		if(sprites == null){
			sprites = ImageManager.loadSprites("healer", 64, 113);
		}
		this.setSpeedAnimation(4F);
	}
	
	public void paint(Graphics g){
		super.paintAnimation(g, sprites);
		
		
	}

	
}