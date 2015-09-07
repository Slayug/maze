package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import window.ImageManager;
import map.Room;

public class Cooker extends Entity {

	/**
	 * Tableau contenant les images pour réaliser l'animation de l'entity
	 */
	public static BufferedImage[] sprites;

	public Cooker(Room pos) {
		super(pos);
		this.setAbsolueHeight(128);
		this.setAbsolueWidth(64);
		if(sprites == null){
			sprites = ImageManager.loadSprites("cooker", 64, 128);
		}
	}

	public void paint(Graphics g){
		super.paintAnimation(g, sprites);
	}

}
