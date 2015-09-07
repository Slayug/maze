package item;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import window.ImageManager;


public class Key extends Item{

	private int index;

	/**
	 * 
	 */
	public Key(int index) {
		super("Clef");
		this.setAbsolueHeight(32);
		this.setAbsolueWidth(32);
		this.index = index;
	}

	public String toString(){
		return "Clef: "+index;
	}
	
	public int getNumber(){
		return this.index;
	}
	
	public void paint(Graphics g){
		BufferedImage img = ImageManager.images.get("key_"+index);
		if(img != null){
			g.drawImage(img, this.getAbsolueX(), this.getAbsolueY(), this.getAbsolueWidth(), this.getAbsolueHeight(), null);
		}
	}
	
}
