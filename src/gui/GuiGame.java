package gui;

import item.Item;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import window.Composant;
import window.Window;
import entity.Player;
import map.Maze;
import maze.MazeGame;

public class GuiGame extends Gui{


	private Player player;
	private Maze maze;

	public GuiGame(Window window, Player player, Maze maze){
		super(window);
		this.player = player;
		this.maze = maze;
		window.addMouseListener(this);
	}


	public void paint(Graphics g){
		if(!player.seeMap)
			maze.drawFloor(g);
		player.paint(g);
		maze.paint(g);
		Font font = new Font("Tahoma", Font.BOLD, 12);
		g.setFont(font);
		super.paint(g);
		
		int xInfo = getWindow().getWidth() - 370;
		int yInfo = Window.margingTop + 15;
		g.setColor(Color.BLACK);
		g.drawString("Vie(s)", xInfo, yInfo);
		yInfo += 10;
		g.setColor(Color.red);
		for(int life = 0; life < player.getLife(); life++){
			g.fillRect(xInfo, yInfo, 15, 15);
			xInfo += 17;
		}
		yInfo += 32;
		xInfo = getWindow().getWidth() - 370;
		g.setColor(Color.black);
		g.drawString("Force", xInfo, yInfo);
		yInfo += 10;
		g.setColor(Color.DARK_GRAY);
		for(int str = 0; str < player.getStrength(); str++){
			g.fillRect(xInfo, yInfo, 15, 15);
			xInfo += 17;
		}
		yInfo += 20;
		xInfo = getWindow().getWidth() - 370;
		g.drawString("Mission: Trouver le trésor !", xInfo, yInfo + 20);
		g.drawString("Astuce: éviter les monstres ! Sauf si vous êtes plus fort(e).", xInfo, yInfo + 40);
		yInfo += 80;
		String[] lines = MazeGame.help.split("\n");
		for(int p = 0; p < lines.length; p++){
			g.drawString(lines[p], xInfo, yInfo+=20);
		}

		// Inventaire
		int xInventory = Window.marginLeft + 5;
		int yInventory = getWindow().getHeight() - 100;
		g.drawString("Inventaire", xInventory, yInventory);
		ArrayList<Item> items = player.getItems();
		for(int p = 0; p < items.size(); p++){
			items.get(p).setAbsoluePosition(xInventory, yInventory);
			items.get(p).paint(g);
			if(p == player.getItemSelected()){
				g.setColor(Color.red);
				g.drawRect(Window.marginLeft + xInventory, Window.margingTop + yInventory, items.get(p).getAbsolueWidth(), items.get(p).getAbsolueHeight());
			}
			xInventory += items.get(p).getAbsolueWidth() + 20;
		}

	}

	public void logic(){
		super.logic();
		for(int p = 0; p < this.getComposants().size(); p++){
			Composant compo = this.getComposants().get(p);
			if(compo.isClicked()){
				if(compo instanceof Item){
					if(player.placeItemFree()){
						compo.setClicked(false);
						compo.setClickable(false);
						Item item = (Item) compo;
						item.setAbsolueHeight(item.getAbsolueHeight());
						item.setAbsolueWidth(item.getAbsolueWidth());
						Maze.rooms[player.getPosition().getX()][player.getPosition().getY()].removeItem(item);
						player.addItem(item);
					}else{
						this.addMessage(new Message("Vous n'avez plus de place dans votre inventaire !", 3, player.getAbsolueX() - 30, player.getAbsolueY()));
						compo.setClicked(false);
					}
				}
			}
		}
		if(player.hasMoved){
			this.resetComposants();
		}
	}

}
