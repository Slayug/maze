package window;

import java.awt.Graphics;

/*
 * La classe button permet de déssiner un bouton,
 * en pouvant défnir sa position et ses dimensions
 */
public class Button extends Composant{
	
	private String name;
	
	public Button(int x, int y){
		super(x, y);
		this.setAbsolueHeight(100);
		this.setAbsolueWidth(200);
	}
	
	public Button(int x, int y, String name){
		super(x, y);
		this.name = name;
	}
	
	
	public Button(int x, int y,int width, int height){
		this(x, y);
		this.setAbsolueHeight(100);
		this.setAbsolueWidth(200);
	}
	
	public Button(int x, int y, int width, int height, String name){
		this(x, y, width, height);
		this.name = name;
	}
	
	
	
	public void paint(Graphics g){
		g.fillRect(this.getAbsolueX(), this.getAbsolueY(), this.getAbsolueWidth(), this.getAbsolueHeight());
		if(this.name != null){
			g.drawString(name, this.getAbsolueX() + this.getAbsolueWidth() / 2, this.getAbsolueY() / 2);
		}
	}

}
