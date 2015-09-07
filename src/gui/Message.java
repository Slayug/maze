package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import maze.MazeGame;
import window.Composant;

public class Message extends Composant {
	
	private String message;
	private float time;
	private Color color;


	/**
	 * 
	 * @param m Message à afficher
	 * @param timer Temps du message
	 */
	public Message(String m, float timer, int x, int y){
		this.message = m;
		this.time = timer;
		this.setAbsolueX(x);
		this.setAbsolueY(y);
		color = Color.red;
	}
	
	
	public String getMessage() {
		return message;
	}


	public float getTime() {
		return time;
	}


	public Color getColor() {
		return color;
	}


	public void setColor(Color color) {
		this.color = color;
	}


	public void paint(Graphics g){
		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(message);
		g.setColor(Color.BLACK);
		g.fillRect(getAbsolueX() - 2, getAbsolueY() - fm.getHeight(), width + 4, fm.getHeight() + 4);
		g.setColor(color);
		g.drawString(message, this.getAbsolueX(), this.getAbsolueY());
		time -= MazeGame.speedThread / 1000.0F;
	}
	
}
