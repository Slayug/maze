package entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import window.ImageManager;
import window.Window;
import gui.Message;
import item.Food;
import item.Item;
import item.Key;
import item.Medication;
import map.Door;
import map.Maze;
import map.Position;
import map.Room;
import maze.MazeGame;

public class Player extends Entity implements KeyListener{

	public static final int lifeMax = 7;
	public static final int strengthMax = 7;
	private int itemSelected = -1;
	private ArrayList<Item> items;

	/**
	 * Jeu aussi jouable au clavier ce qui est plus rapide
	 * 0 h: help
	 * 1 z: monter
	 * 2 q: gauche
	 * 3 s: bas
	 * 4 d: droite
	 * 5 o: confirmer
	 * 6 n: annuler
	 * 7 p: prendre un objet
	 * 8 u: utiliser un objet séléctionné
	 * 9
	 * 10 f: jeter l'objet séléctionné
	 * 11 m: map
	 */
	private boolean keys[];

	/**
	 * si le joueur appui sur un chiffre pour séléctionner un item, cette variable contiendra ce chiffre
	 */
	private int keyNumber = -1;

	/**
	 * Permet de savoir si le joueur a décidé de regarder la mini map ou non
	 */
	public boolean seeMap = false;
	/**
	 * Si le joueur est en mode pour selectionner un objet: il vient d appuyer sur la touche pour selectionner un objet
	 */
	public boolean selectItem = false;
	private MazeGame game;
	private static BufferedImage[] sprites;
	/**
	 * Permet de savoir si le joueur est en duel ou non
	 */
	public boolean duel;
	/**
	 * Permet de savoir à quel moment du duel on est
	 */
	public int stepDuel;

	public boolean won;
	public boolean loose;

	public Player(MazeGame game, Room pos) {
		super(pos);
		this.game = game;
		this.life = lifeMax;
		this.strength = strengthMax;
		items = new ArrayList<Item>();
		won = false;
		loose = false;
		keys = new boolean[12];

		if(sprites == null){
			sprites = ImageManager.loadSprites("player", 60, 60);
			this.setSpeedAnimation(8F);
		}
		duel = false;
		stepDuel = 0;
	}

	public void move(Room room){
		room.setVisited(true);
		strength--;
		if(this.strength <= 0){
			this.life--;
		}
		this.room = room;
	}


	public int getLifeMax() {
		return lifeMax;
	}

	public int getStrengthMax() {
		return strengthMax;
	}

	public int getStrength() {
		return strength;
	}

	public void eat(){
		this.strength = strengthMax;
	}

	public void eat(int what){
		this.life += what;
		if(this.life > lifeMax){
			this.life = lifeMax;
		}
	}

	public boolean useItem(int id){
		if(items.get(id) != null){
			if(items.get(id) instanceof Food){
				Food food = (Food)items.get(id);
				if(strength + food.getValue() <= strengthMax)
					this.strength += food.getValue();
			}else if(items.get(id) instanceof Medication){
				Medication medoc = (Medication)items.get(id);
				if(life + medoc.getValue() <= lifeMax)
					this.life += medoc.getValue();
			}else if(items.get(id) instanceof Key){
				Key key = (Key) items.get(id);
				boolean doorClosed = false;
				for(int d = 0; d < 4; d++){
					if(this.getRoom().getDoor(d) != null){
						if(!this.getRoom().getDoor(d).isOpen()){
							doorClosed = true;
						}
					}
				}
				if(doorClosed){
					doorClosed = false;
					for(int d = 0; d < 4; d++){ // on reparcourt au cas où il y'a plusieurs porte fermées
						if(this.getRoom().getDoor(d) != null){
							Door door = this.getRoom().getDoor(d);
							if(!door.isOpen()){
								if(door.getNumber() == key.getNumber()){
									game.currentGui.addMessage(
											new Message("Vous avez ouvert la porte avec la clef n°"+key.getNumber(), 4, this.getAbsolueX()-40, this.getAbsolueY()-70));
									this.getRoom().getDoor(d).open();
									MazeGame.maze.nextRoom(getRoom(), d).getDoor(MazeGame.maze.orientationOponent(d)).open();
									doorClosed = true; // une porte a été ouverte
								}else{
									game.currentGui.addMessage(new Message("Ici c'est la porte n°"+door.getNumber(), 4, this.getAbsolueX()-40, this.getAbsolueY()-90));
								}
							}
						}
					}
					if(!doorClosed){
						game.currentGui.addMessage(new Message("Vous avez essayé la clef n°"+key.getNumber(), 4, this.getAbsolueX()-40, this.getAbsolueY()-50));
						return false;
					}
				}else{
					game.currentGui.addMessage(new Message("Aucune porte n'est fermée ici !", 4, this.getAbsolueX()-40, this.getAbsolueY()-70));
					game.currentGui.addMessage(new Message("Vous avez essayé la clef n°"+key.getNumber(), 4, this.getAbsolueX()-40, this.getAbsolueY()-50));
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public String toString(){
		String str = super.toString();
		str += "Life: ";
		for(int p = 0; p < this.life; p++){
			str += "|";
		}
		str += " Strength: ";
		for(int p = 0; p < this.strength; p++){
			str += "|";
		}
		str += " Objet(s): ";
		for(int p = 0; p < this.items.size(); p++){
			str += items.get(p).getName();
		}
		return str;
	}



	public int getItemSelected() {
		return itemSelected;
	}

	public void setItemSelect(int id) {
		this.itemSelected = id;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public boolean placeItemFree(){
		if(items.size() < 7){
			return true;
		}
		return false;
	}

	public void addItem(Item item){
		this.items.add(item);
	}

	public void removeItem(int index){
		items.remove(index);
	}

	public boolean removeItem(String name){

		return false;
	}

	public boolean haveKey() {
		// TODO Auto-generated method stub
		return false;
	}

	public void heal() {
		this.life = lifeMax;		
	}

	public void logic(){
		if(this.won || this.loose){ // Si le joueur a gagné ou perdu on boucle pour savoir si il veut rejouer ou non
			if(keys[5]){
				game.stopThread();
				new MazeGame();
				//MazeGame.window.dispatchEvent(new WindowEvent(MazeGame.window, WindowEvent.WINDOW_CLOSING));
			}
			if(keys[6]){
				System.exit(0);
			}
		}else{
			int orientation = -1;
			if(!seeMap && !duel){
				if(keys[1]){
					orientation = 1;
				}else if(keys[2]){
					orientation = 0;
				}else if(keys[3]){
					orientation = 3;
				}else if(keys[4]){
					orientation = 2;
				}
			}else{
				keys[1] = false;
				keys[2] = false;
				keys[3] = false;
				keys[4] = false;
			}
			if(keys[11] && !this.seeMap){ // le joueur affiche la carte
				this.seeMap = true;
				keys[11] = false;
				game.currentGui.resetMessage();
			}else if(keys[11] && this.seeMap){ // le joueur enlève la map
				this.seeMap = false;
				this.keys[11] = false;
			}else if(keys[7]){ // Mode séléction objet
				this.selectItem = true;
			}
			if(keys[8]){ // U
				if(this.itemSelected > -1){ // un item est séléctionné, utilisation de celui ci
					if(this.useItem(this.getItemSelected())){
						this.items.remove(this.getItemSelected());
					}
					this.itemSelected = -1;
				}else{
					game.currentGui.addMessage(new Message("Vous devez d'avord séléctioner un objet !", 3, this.getAbsolueX()-40, this.getAbsolueY()-70));
				}
				keys[8] = false;
			}if(this.keyNumber > -1){
				if(this.keyNumber < this.items.size()){ // selection d'un objet
					this.itemSelected = this.keyNumber;
				}
				this.keyNumber = -1;
			}
			if(keys[10]){ // le joueur veut lâcher un objet on vérifie si il a bien séléctionner un objet avant
				if(this.itemSelected > -1){
					this.getRoom().addItem(this.items.get(itemSelected));
					this.items.remove(itemSelected);
					this.itemSelected = -1;
				}else{
					game.currentGui.addMessage(new Message("Vous devez d'avord séléctioner un objet !", 3, this.getAbsolueX()-40, this.getAbsolueY()-70));
				}
				keys[10] = false;
			}
			if(orientation > -1){
				if(this.canMove(orientation)){
					if(this.getRoom().getDoor(orientation).isOpen()){
						Position pos = MazeGame.maze.nextPos(this.getPosition().getX(), this.getPosition().getY(), orientation);
						this.move(Maze.rooms[pos.getX()][pos.getY()]);
						game.currentGui.resetMessage();
					}else{
						game.currentGui.addMessage(new Message("Cette porte est fermée !", 3, this.getAbsolueX()-40, this.getAbsolueY()-70));
						game.currentGui.addMessage(new Message("Trouver la clef ou utilisé la si vous l'avez !", 3, this.getAbsolueX()-40, this.getAbsolueY()-52));
					}
				}
				this.hasMoved = true;
				this.resetKeys();
			}

			if(this.duel){ // un duel a été lancé on décompte à chaque passage le temps
				this.stepDuel--;
				if(stepDuel == 0){
					this.duel = false;
				}
			}
		}
	}

	/**
	 * Position par rapport au labyrinthe
	 * @return
	 */
	public Position getPosition(){
		return this.getRoom().getPosition();
	}

	public void paint(Graphics g) {
		if(sprites == null || this.seeMap){
			g.setColor(Color.DARK_GRAY);
			if(this.seeMap){
				g.fillRect(this.room.getAbsolueX() + this.room.getAbsolueWidth()/4, this.room.getAbsolueY() + this.room.getAbsolueHeight()/4,
						this.room.getAbsolueWidth()/2, this.room.getAbsolueHeight()/2);
			}else{
				g.fillRect(this.getAbsoluePositionAbsolue().getX() + 10, this.getAbsoluePositionAbsolue().getY() + 10,
						this.getAbsolueWidth() - 20, this.getAbsolueHeight() - 20);
			}
		}else{
			this.setAbsolueWidth(96);
			this.setAbsolueHeight(96);
			super.paintAnimation(g, sprites);
		}

	}

	public void initWindow() {
		this.setAbsolueHeight((Window.widthTile * Maze.tileByWidth) / MazeGame.maze.getWidth());
		this.setAbsolueWidth((Window.heightTile * Maze.tileByHeight) / MazeGame.maze.getHeight());
	}

	private void resetKeys(){
		for(int p = 0; p < keys.length; p++){
			keys[p] = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if(key.getKeyCode() == 90){ // Z
			resetKeys();
			keys[1] = true;
		}
		else if(key.getKeyCode() == 81){ // Q
			resetKeys();
			keys[2] = true;
		}else if(key.getKeyCode() == 83){ // S
			resetKeys();
			keys[3] = true;
		}else if(key.getKeyCode() == 68){ // D
			resetKeys();
			keys[4] = true;
		}else if(key.getKeyCode() == 77){ // M
			resetKeys();
			keys[11] = true;
		}else if(key.getKeyCode() == 80){ // P
			resetKeys();
			keys[7] = true;
		}else if(key.getKeyCode() == 85){ // U
			resetKeys();
			keys[8] = true;
		}else if(key.getKeyCode() > 48 && key.getKeyCode() < 58){
			this.keyNumber = key.getKeyCode() - 48 - 1;
		}else if(key.getKeyCode() == 70){ // F
			resetKeys();
			keys[10] = true;
		}else if(key.getKeyCode() == 79){ // O
			resetKeys();
			keys[5] = true;
		}else if(key.getKeyCode() == 78){ // N
			resetKeys();
			keys[6] = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent key) {
		// TODO Auto-generated method stub

	}


}
