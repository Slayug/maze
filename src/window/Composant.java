package window;

import gui.Gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import map.Position;
import maze.MazeGame;
/**
 * Chaque composant peut être cliqué
 * et dessiner depuis un gui, ou depuis tout autre objet sans utiliser la logic de gui
 */
public class Composant {

	private int x, y, width, height;
	/**
	 * Si le composant vient d'être cliqué
	 */
	private boolean isClicked = false;
	private Gui gui;
	/**
	 * Vitesse à laquelle les images defilent pour l'animation
	 */
	private float speedAnimation;

	private float stepAnim;

	public Composant(){
		stepAnim = 0.0F;
		this.speedAnimation = 1.0F;
	}

	public Composant(Gui gui){
		this.gui = gui;
	}

	public Composant(int x, int y){
		stepAnim = 0.0F;
		this.speedAnimation = 1.0F;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Permet de changer la vitesse d'animation
	 * Si le paramètre passé est supérieur à 5, ceci indique que toutes les images ne seront pas affichées, donc on limite
	 * @param speed
	 */
	public void setSpeedAnimation(float speed){
		if((MazeGame.speedThread/1000.0F)*this.speedAnimation > 1){
			speedAnimation = 1.0F / (MazeGame.speedThread/1000.0F); //On met la vitesse maxi
		}else if(speed >= 0){
			speedAnimation = speed;
		}
		//System.out.println(speed+" - "+speedAnimation);
	}
	
	public float getSpeedAnimation(){
		return this.speedAnimation;
	}

	public void setGui(Gui gui){
		this.gui = gui;
	}

	public boolean setClickable(boolean click){
		if(click && gui != null){
			if(!gui.getComposants().contains(this)){ // On vérifie que le composant n'a pas déjà été ajouté
				gui.addComposant(this, x, y);
				return true;
			}
		}else if(!click && gui != null){
			gui.getComposants().remove(this);
			return true;
		}
		return false;
	}

	public boolean isClicked() {
		return isClicked;
	}

	public int getAbsolueX() {
		return x + Window.marginLeft;
	}

	public void setAbsolueX(int x) {
		this.x = x;
	}

	public int getAbsolueY() {
		return y + Window.margingTop;
	}

	public void setAbsolueY(int y) {
		this.y = y;
	}

	public int getAbsolueWidth() {
		return width;
	}

	public void setAbsolueWidth(int width) {
		this.width = width;
	}

	public int getAbsolueHeight() {
		return height;
	}

	public void setAbsolueHeight(int height) {
		this.height = height;
	}

	/**
	 * Chaque composant est paint depuis sa position absolue
	 * @param g
	 */
	public void paint(Graphics g){
	}
	
	/**
	 * Permet de desciner une animation comme un gif
	 * la vitesse est d'une image par seconde
	 * @param g
	 * @param sprites
	 */
	public void paintAnimation(Graphics g, BufferedImage[] sprites){
		if(sprites != null){
			if(stepAnim >= sprites.length - ((MazeGame.speedThread/1000.0F)*speedAnimation)){
				stepAnim = 0.0F;
			}else{
				stepAnim += ((MazeGame.speedThread/1000.0F)*speedAnimation);
			}
			int index = (int) stepAnim;
			g.drawImage(sprites[index], this.getAbsolueX(), this.getAbsolueY(), this.getAbsolueWidth(), this.getAbsolueHeight(), null);
		}
	}

	/**
	 * Permet de tester si un pixel est dans ce composant
	 * @param x position en abscisse du pixel
	 * @param y position en ordonné du pixel
	 * @return
	 */
	public boolean onCollision(int x, int y){
		if(x >= this.getAbsolueX() &&
				x <= this.getAbsolueX() + this.width &&
				y >= this.getAbsolueY() &&
				y <= this.getAbsolueY() + this.height){
			return true;
		}
		return false;
	}

	public void setAbsoluePosition(int x, int y) {
		this.x = x;
		this.y = y;		
	}

	/**
	 * Retourne la position par rapport au repère de la fenêtre
	 * @return
	 */
	public Position getAbsoluePositionAbsolue(){
		return new Position(x + Window.marginLeft, y + Window.margingTop);
	}

	public void setClicked(boolean click) {
		this.isClicked = click;
	}
}
