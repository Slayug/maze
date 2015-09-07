package entity;

import java.awt.Graphics;

import window.Composant;
import map.Room;

public class Entity extends Composant {

	protected Room room;
	protected int strength;
	protected int life;
	/**
	 * Indique que l'entité vient d'arriver dans une pièce
	 */
	public boolean hasMoved = false;
	

	public Entity(Room pos){
		this.room = pos;
	}

	public Room getRoom(){
		return this.room;
	}

	
	public void setRoom(Room room){
		this.room = room;
	}

	public void affiche(){
		System.out.println(this);
	}

	public String toString(){
		String str = "";
		return str;
	}

	public void move(Room room){
		this.room = room;
	}

	public int getLife() {
		return this.life;
	}

	public boolean isDead(){
		if(this.getLife() <= 0){
			return true;
		}
		return false;
	}

	public boolean canMove(int orientation){
		return this.room.getDoor(orientation) != null;		
	}

	public void setLife(int i) {
		this.life = i;

	}

	public void logic(){

	}

	public void paint(Graphics g){

	}

	
}
