package maze;

import gui.Gui;
import gui.GuiGame;
import gui.Message;
import item.Gold;
import item.Item;
import item.Treasure;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import window.ImageManager;
import window.Window;
import entity.Cooker;
import entity.Entity;
import entity.Explosion;
import entity.Healer;
import entity.Monster;
import entity.Player;
import map.Maze;


public class MazeGame implements Runnable{

	/**
	 * Représente le labyrinthe unique pour le jeu
	 */
	public static Maze maze;
	/**
	 * Représente le joueur unique pour le jeu
	 */
	public static Player player;
	public static final String help = "Manuel:"
			+ "\n -Aide: help"
			+ "\n -Monter: Z"
			+ " -Descendre: S"
			+ " -Gauche: Q"
			+ " -Droite: D"
			+ "\n -Confirmer: O"
			+ " -Annuler: N"
			+ "\n -Selectionner un objet:\n 1: pour le premier objet par exemple"
			+ "\n -Utiliser un objet: U"
			+ "\n -Jeter un objet: F"
			+ "\n -Map M"
			+ "\n -Clicker sur un objet dans la pièce pour le prendre";

	/**
	 * Représente la fenêtre
	 */
	public static Window window;
	/**
	 * Actuel interface affiché à l'utilisateur
	 */
	public Gui currentGui;
	private boolean run;

	/**
	 * Indique le temps d'attente après la logique et le rendu,
	 * Ce qui permet de déduire le nombre d'appel de logic et de rendu par seconde en négligeant le temps d'execution de ces derniers
	 * Varie de 100 à 999
	 */
	public final static int speedThread = 100;

	
	public MazeGame(){
		new ImageManager();
		maze = new Maze(this);
		player = new Player(this, maze.getSpawn());
		window = new Window(960, 720); // largeur et hauteur basé sur 320x240 16:9

		//new Window(800, 600);
		player.initWindow();
		maze.initWindow();
		player.setAbsolueX(maze.getAbsolueWidth() / 2 - Window.widthTile / 2);
		player.setAbsolueY(maze.getAbsolueHeight() / 2 - Window.heightTile / 2);
		player.move(maze.getSpawn());
		player.eat(); // On redonne à manger au joueur puisque on a fait un move avant et que chaque move lui enlève un point de force
		this.currentGui = new GuiGame(window, player, maze);
		//this.currentGui = new GuiHelp(window);
		run = true;
		Thread gameThread = new Thread(this);
		gameThread.start();
	}


	public void paint(){
		BufferStrategy bs = window.getBufferStrategy();
		if(bs == null){
			window.createBufferStrategy(3);
			return;
		}

		//this.map.paint(pixels, xOffset, yOffset);


		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, window.getWidth(), window.getHeight()); // On dessine un fond blanc pour effacer ce qui a été affiché avant
		this.currentGui.paint(g);
		g.dispose();
		bs.show();

	}


	public static void main(String[] args) {
		println("----------------------------------------------------");
		println("----------------------- Maze -----------------------");
		println("----------------------------------------------------");

		new MazeGame();
	}


	public static void print(String str){
		System.out.print(str);
	}

	public static void println(String str){
		System.out.println(str);
	}

	/**
	 * Logique et rendu du jeu pour le mode fenêtre
	 * sur un thread 
	 */
	@Override
	public void run() {
		while(run){
			try {
				Thread.sleep(speedThread);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logic();
			paint();
		}

	}

	public void stopThread(){
		this.run = false;
	}

	/**
	 */
	private void logic() {
		player.logic();
		
		this.currentGui.logic();
		
		ArrayList<Item> items = player.getRoom().getItems();
		for(int i = 0; i < items.size(); i++){
			if(items.get(i) instanceof Treasure){
				this.currentGui.addMessage(new Message("V I C T O I R E !", 999999, player.getAbsolueX()-45, player.getAbsolueY()-70));
				this.currentGui.addMessage(new Message("Vous avez trouvé le trésor !", 999999, player.getAbsolueX()-45, player.getAbsolueY()-55));
				this.currentGui.addMessage(new Message("Recommencer ? O: Oui, N:Non", 999999, player.getAbsolueX()-45, player.getAbsolueY()-40));
				player.getRoom().addItem(new Gold((int)(Math.random()*Maze.tileByWidth*Window.widthTile - 64), (int) (Math.random()*Maze.tileByHeight*Window.heightTile - 64)));
				player.won = true;
			}
		}
		if(player.getLife() == 0){ // le joueur est mort
			this.currentGui.addMessage(new Message("P E R D U (E) !", 999999, player.getAbsolueX()-45, player.getAbsolueY()-70));
			this.currentGui.addMessage(new Message("Vous êtes mort !", 999999, player.getAbsolueX()-45, player.getAbsolueY()-55));
			this.currentGui.addMessage(new Message("Recommencer ? O: Oui, N:Non", 999999, player.getAbsolueX()-45, player.getAbsolueY()-40));
			player.loose = true;
		}
		if(player.hasMoved && !player.duel){
			
			ArrayList<Entity> entitys = maze.getEntitys();
			for(int p = 0; p < entitys.size(); p++){ // On fait d'abord la logique de chaque entité autre que le joueur pour les déplacer ou pas automatiquement
				entitys.get(p).logic();
			}
			
			for(int p = 0; p < entitys.size(); p++){ // On gère ensuite chaque entité si elle en rencontre une autre
				if(entitys.get(p).getRoom().equals(player.getRoom())){
					if(entitys.get(p) instanceof Healer){
						player.heal();
					}
					if(entitys.get(p) instanceof Cooker){
						player.eat();
					}
					if(entitys.get(p) instanceof Monster){
						Monster monster = (Monster) entitys.get(p);
						this.currentGui.addMessage(
								new Message("C'est l'heure du duel !!", 4, player.getAbsolueX()-60, player.getAbsolueY()-70));
						this.currentGui.addMessage(
								new Message("Vous "+player.getStrength()+" - "+monster.getStrength()+" Monstre", 4, player.getAbsolueX()-60, player.getAbsolueY()-55));
						player.stepDuel = 30;
						player.duel = true;

					}
				}
			}
			player.hasMoved = false;
		}else if(player.duel){ // un duel a été lancé en entrant dans la pièce
			ArrayList<Entity> entitys = maze.getEntitys();
			for(int p = 0; p < entitys.size(); p++){
				if(entitys.get(p) instanceof Monster && entitys.get(p).getRoom().equals(player.getRoom())){
					Monster monster = (Monster) entitys.get(p);
					if(player.stepDuel == 29){
						if(monster.getStrength() > player.getStrength()){ // le monstre a plus de force
							// on enlève un point de vie au joueur
							player.setLife(player.getLife()-1);
							
							this.currentGui.addMessage(
									new Message("Vous avez perdu une vie le monstre est plus fort !!", 4, player.getAbsolueX()-60, player.getAbsolueY()-40));
							// on enlève un point de force au monstre
							maze.getEntitys().add(new Explosion(player.getRoom(), player.getAbsolueX() + 10, player.getAbsolueY()-20));

							// on affiche l'animation pour dire que le joueur et le monstre sont blessé et que le monstre est partie.

						}else{ // le joueur a plus de force
							// on tue le montre
							this.currentGui.addMessage(
									new Message("Vous tuez le monstre, vous êtes plus fort(e) !!", 4, player.getAbsolueX()-60, player.getAbsolueY()-40));
							maze.getEntitys().add(new Explosion(player.getRoom(), monster.getAbsolueX() - 10, monster.getAbsolueY()-20));
						}
					}else if(player.stepDuel <= 10){
						entitys.remove(entitys.get(p)); // on supprime le monstre 
						for(int e = 0; e < entitys.size(); e++){
							if(entitys.get(e) instanceof Explosion){ // on supprime l'explosion
								entitys.remove(e);
								e--;
							}
						}
					}
				}
			}
		}
	}


}
