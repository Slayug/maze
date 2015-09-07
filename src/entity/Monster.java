package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import window.ImageManager;
import map.Room;

public class Monster extends Entity{

	private static BufferedImage[] sprites;

	public Monster(Room pos) {
		super(pos);
		this.setAbsolueHeight(60);
		this.setAbsolueWidth(60);
		this.strength = 0;
		if(sprites == null){
			sprites = ImageManager.loadSprites("monster", 60, 60);
		}
		this.setSpeedAnimation(10F);
	}

	public Monster(Room next, int strength) {
		this(next);
		this.strength = strength;
	}
	
	

	public int getStrength() {
		return strength;
	}

	public void paint(Graphics g){
		super.paintAnimation(g, sprites);
	}
}
