package gui;

import item.Item;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import maze.MazeGame;
import window.Composant;
import window.Window;
/**
 * Graphical User Interace
 * Prend toute la taille de la fenêtre
 */
public class Gui implements MouseListener {
	
	/**
	 * Liste des composants du l'interface
	 */
	private ArrayList<Composant> composants;
	private ArrayList<Message> messages;
	private Window window;
	
	public Gui(Window window){
		this.window = window;
		this.composants = new ArrayList<Composant>();
		this.messages = new ArrayList<Message>();
	}

	/**
	 * Permet de dessiner tous les composants du gui
	 * @param g
	 */
	public void paint(Graphics g){
		for(int p = 0; p < composants.size(); p++){
			Composant compo = composants.get(p);
			if(!(compo instanceof Item && MazeGame.player.seeMap)){
				composants.get(p).paint(g);
			}
		}
		for(int m = 0; m < messages.size(); m++){
			messages.get(m).paint(g);
		}
	}
	
	public void logic(){
		for(int p = 0; p < messages.size(); p++){
			if(messages.get(p).getTime() <= 0.0F){
				messages.remove(p);
				p--;
			}
		}
	}
	
	public void addMessage(Message m){
		if(m.getTime() > 0){
			this.messages.add(m);
		}
	}
	
	public void resetMessage(){
		this.messages = new ArrayList<Message>();
	}
		
	public Window getWindow() {
		return window;
	}
	
	public ArrayList<Composant> getComposants() {
		return composants;
	}

	public void addComposant(Composant compo, int x, int y){
		compo.setAbsoluePosition(x, y);
		this.composants.add(compo);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		for(int p = 0; p < this.composants.size(); p++){
			if(composants.get(p).onCollision(event.getX(), event.getY())){
				composants.get(p).setClicked(true);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void resetComposants(){
		this.composants = new ArrayList<Composant>();
	}
}
