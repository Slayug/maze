package item;

import gui.Gui;
import window.Composant;


public class Item extends Composant{
	
	private String name;
	
	public Item(String name){
		this.name = name;
	}
	
	public Item(Gui gui, String name){
		super(gui);
		this.name = name;
	}

	public String toString(){
		return this.getClass().getName();
	}
	
	public String getName(){
		return this.name;
	}

}
