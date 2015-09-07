package map;

import item.Burger;
import item.Gold;
import item.Item;
import item.Key;
import item.Meat;
import item.Pill;
import item.Treasure;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import maze.MazeGame;
import window.Composant;
import window.ImageManager;
import window.Window;
import entity.Cooker;
import entity.Entity;
import entity.Explosion;
import entity.Healer;
import entity.Monster;

public class Maze extends Composant {

	public static Room[][] rooms;
	private int height;
	private int width;
	private Room spawn;
	private ArrayList<Entity> entitys;
	/**
	 * chemin le plus court au minimum pour accèder au trésor
	 */
	private final int wayMinToWin = 9;
	private int nbreRoom;
	private final int nbreRoomMin = 75; // doit être inférieur au nombre total de case possible

	private boolean mazeDone;
	private MazeGame game;
	private int[][] floor;

	/**
	 * Nombre de tuile par largeur, chaque tuile représente une pièce
	 */
	public final static int tileByWidth = 9;
	/**
	 * De même pour la hauteur
	 */
	public final static int tileByHeight = 9;

	/**
	 * Tableaux permettant de générer les portes aléatoires avec leurs clef
	 */
	private int[] doors = new int[3];
	private int[] keys = new int[doors.length];
	private int indexKey = 0;
	/**
	 * 1024 maxi de base
	 * @param mazeGame 
	 */
	public Maze(MazeGame game){
		this(game, 10, 10);

	}

	public Maze(MazeGame game, int sizeMax){
		this(game, sizeMax / 2, sizeMax / 2);
	}

	public Maze(MazeGame game, int width, int height){
		this.game = game;
		this.width = width;
		this.height = height;
		rooms = new Room[width][height];
		entitys = new ArrayList<Entity>();
		// on ajoute un nombre entre (1 à 5) et 12
		// ensuite en générant les pièces on va décrémenter la valeur de keys[indexKey] jusqu'à 0 (on posera une clef) puis on passera à doors[indexKey]
		// que l'on décrémentera jusqu'à 0 et là on placera la porte correspondante
		for(int p = 0; p < keys.length; p++){
			keys[p] = (int)(Math.random()*7+1);
			doors[p] = (int)(Math.random()*4+1);
		}
		this.generateMaze();
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	private void generateMaze(){
		Random random = new Random();
		int r = random.nextInt(4);
		int x = this.width / 2;
		int y = this.height / 2;
		this.spawn = new Room(x, y);
		rooms[x][y] = this.spawn;// spawn au milieu

		mazeDone = false;
		//On genère les pièces
		this.nbreRoom = 1; // en comptant le spawn
		// on génère au minimum deux portes pour le spawn les portes face à face
		this.spawn.addDoor(r, new Door(true));
		this.spawn.addDoor(this.orientationOponent(r), new Door(true));

		generateRoom(this.spawn, r);
		generateRoom(this.spawn, this.orientationOponent(r));
	}

	public ArrayList<Entity> getEntitys(){
		return this.entitys;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param from 0: Ouest, 1, Nord, 2: Est, 3:Sud
	 * @return
	 */
	private Room generateRoom(Room lastRoom, int from){
		int lastOrientation = this.orientationOponent(from);
		Position pos = this.nextPos(lastRoom.getPosition().getX(), lastRoom.getPosition().getY(), from);
		
		Room next = new Room(pos);
		next.addDoor(lastOrientation, lastRoom.getDoor(from));

		this.addRoom(next, pos.getX(), pos.getY());

		this.nbreRoom++;
		// Ajout des entitys
		int r = (int) (Math.random()*100 + 1);
		if(r >= 10 && r <= 15){ // on ajoute un medecin
			this.entitys.add(new Healer(next));
		}else if(r >= 20 && r <= 35){ // on ajoute un cuisinier
			this.entitys.add(new Cooker(next));
		}else if(r > 85){ // On ajoute un monstre
			int strength = (int) (Math.random()*10 + 1);
			this.entitys.add(new Monster(next, strength));
		}

		// Ajout des items on relance le nombre aléatoire à chaque fois pour avoir la possibilité d'avoir plusieurs item dans la même pièce
		r = (int) (Math.random()*100 + 1);
		if(r >= 10 && r <= 50){ // on ajoute un burger
			next.addItem(new Burger((int) (Math.random()*4+1)));
		}
		r = (int) (Math.random()*100 + 1);
		if(r > 80){
			next.addItem(new Pill((int) (Math.random()*3+1)));
		}
		r = (int) (Math.random()*100 + 1);
		if(r < 24){
			next.addItem(new Meat((int) (Math.random()*3+1)));
		}
		if(nbreRoom > nbreRoomMin){
			return null;
		}
		if(this.nbreRoom > wayMinToWin){
			if(!mazeDone){
				mazeDone = true;
				next.addItem(new Treasure());
			}
		}

		// On genère les portes et si elles ont besoin d'une clef ou pas
		Door[] nextDoor = new Door[4];
		boolean doorCreated = false;
		//int p = 0;
		Boolean[] doorsDone = new Boolean[nextDoor.length];
		while(!doorCreated){ // tant qu'aucune porte n'a été créee on parcours les quatres côtés possible.
			//int nbreRoomAround = 0; // Si une pièce est créer avec déjà 4 pièces autour on doit seulement créer une porte et pas une prochaine pièce
			for(int p = 0; p < nextDoor.length; p++){
				if(p != lastOrientation){
					if(doorsDone[p] == null){
						int hasDoor = (int) (Math.random() * 10);// Supérieur à 5 on ajoute une porte
						Position nextPos = this.nextPos(pos.getX(), pos.getY(), p);
						if(nextPos == null){
							continue;
						}
						if(hasDoor > 5){
							if(!this.roomHere(nextPos)){ // On vérifie qu'il n y'a pas une pièce à la prochaine position
								boolean doorClosed = false;
								if(indexKey < keys.length){
									if(keys[indexKey] <= 0){ // on ajoute une clef
										if(keys[indexKey] == 0){
											next.addItem(new Key(indexKey));
											keys[indexKey]--;
										}

										if(doors[indexKey] == 0){
											// on ajoute une porte fermée une fois la clef placée
											doorClosed = true;
											nextDoor[p] = new Door(false, indexKey);
											if(indexKey + 1 <= keys.length){
												indexKey++;
											}
										}else{
											doors[indexKey]--;
										}
									}else{
										keys[indexKey]--;
									}
								}
								if(!doorClosed){
									nextDoor[p] = new Door(true);
								}
								next.addDoor(p, nextDoor[p]);
								doorsDone[p] = true;
								doorCreated = true;
								this.generateRoom(next, p);
							}else{ //Sinon on tente en tirant un nombre aléatoire de raccorder la pièce actuelle avec la suivante
								int random = (int) (Math.random() * 10);
								if(random > 5 || lastRoom.equals(this.getSpawn())){
									nextPos = this.nextPos(pos.getX(), pos.getY(), p);
									if(nextPos != null){
										doorCreated = true;
										next.addDoor(p, new Door(true));
										rooms[nextPos.getX()][nextPos.getY()].addDoor(this.orientationOponent(p), new Door(true));									
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Dessine par défault la pièce où est le joueur actuellement,
	 * si le joueur ouvre la map: dessine la map avec les pièces autour de lui, celle non visité avec un brouillard noir
	 */
	public void paint(Graphics g){
		if(MazeGame.player.seeMap){
			paintMap(g);			
		}else{
			Room roomPlayer = rooms[MazeGame.player.getPosition().getX()][MazeGame.player.getPosition().getY()];
			roomPlayer.setAbsolueX(this.getAbsolueX() - Window.marginLeft);
			roomPlayer.setAbsolueY(this.getAbsolueY() - Window.margingTop);
			roomPlayer.setAbsolueHeight(this.getAbsolueHeight());
			roomPlayer.setAbsolueWidth(this.getAbsolueWidth());
			roomPlayer.paint(g);

			int xEntity = 40 + Window.marginLeft;
			int yEntity = 80 + Window.margingTop;
			for(int p = 0; p < this.entitys.size(); p++){ // On dessine les entités
				if(entitys.get(p).getRoom().equals(roomPlayer)){ // seulement si elle est dans la même pièce que le joueur
					if(entitys.get(p) instanceof Monster){ // Si c'est un monstre on le place face au joueur
						entitys.get(p).setAbsolueX(MazeGame.player.getAbsolueX() - MazeGame.player.getAbsolueWidth() + 30);
						entitys.get(p).setAbsolueY(MazeGame.player.getAbsolueY() - 10);
					}
					else if(!(this.entitys.get(p) instanceof Explosion) ){ // Si ce n'est pas une explosion on place à la suite
						entitys.get(p).setAbsolueX(xEntity);
						entitys.get(p).setAbsolueY(yEntity);
						if(xEntity + entitys.get(p).getAbsolueWidth() >= Window.widthTile*tileByWidth){ // Si on arrive tout à droite du dessin on passe à la ligne
							xEntity = 20 + Window.marginLeft;
							yEntity += 100;
						}else{	
							xEntity += entitys.get(p).getAbsolueWidth() + 20;
						}
					}
					entitys.get(p).paint(g);
				}
			}
			// On dessine ensuite les items dans la pièce tout en les mettant clickable
			ArrayList<Item> items = roomPlayer.getItems();
			for(int i = 0; i < items.size(); i++){
				if(items.get(i) instanceof Treasure){ // Si c'est un trésor on le place devant le joueur
					items.get(i).setAbsolueX(MazeGame.player.getAbsolueX() - MazeGame.player.getAbsolueWidth() + 30);
					items.get(i).setAbsolueY(MazeGame.player.getAbsolueY() - 10);
				}else if(!(items.get(i) instanceof Gold)){
					items.get(i).setAbsolueX(xEntity);
					items.get(i).setAbsolueY(yEntity);
				}
				items.get(i).paint(g);
				if(!items.get(i).setClickable(true)){
					items.get(i).setGui(game.currentGui);
					items.get(i).setClickable(true);
				}
				if(xEntity >= Window.widthTile*tileByWidth){
					xEntity = 20 + Window.marginLeft;
					yEntity += 100;
				}else{	
					xEntity += items.get(i).getAbsolueWidth() + 20;
				}
			}
		}

	}

	private void paintMap(Graphics g){
		int x = this.getAbsolueX();
		int y = this.getAbsolueY();
		int widthRoom = (Window.widthTile * tileByWidth) / this.getWidth();
		int heightRoom = (Window.heightTile * tileByHeight) / this.getHeight();
		for(int h = 0; h < this.getHeight(); h++){
			for(int w = 0; w < this.getWidth(); w++){
				if(rooms[w][h] != null && rooms[w][h].isVisited()){
					rooms[w][h].setAbsolueX(x - Window.marginLeft); // on enlève la marge d'erreur du repère puisque elle est appliquée ensuite dans les getters
					rooms[w][h].setAbsolueY(y - Window.margingTop);
					rooms[w][h].setAbsolueWidth(widthRoom);
					rooms[w][h].setAbsolueHeight(heightRoom);
					rooms[w][h].paint(g);
				}else{
					g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
					g.fillRect(x, y, widthRoom, heightRoom);
				}
				g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
				g.fillRect(x, y, widthRoom, 2);
				g.fillRect(x, y, 2, heightRoom);
				g.fillRect(x + widthRoom - 2, y, 2, heightRoom);
				g.fillRect(x, y + heightRoom - 2, heightRoom, 2);
				//xDoor++;
				x += widthRoom;
			}
			x = Window.marginLeft;
			//w = MazeGame.player.getPosition().getX() - tileByWidth / 2;
			//yDoor++;
			y += heightRoom;
		}

	}

	private void generateFloor(){
		int width = (Window.widthTile*tileByWidth)/32;
		int height = (Window.heightTile*tileByHeight)/32;
		this.floor = new int[width][height];

		for(int w = 0; w < width; w++){
			for(int h = 0; h < height; h++){
				floor[w][h] = (int) (Math.random()*5 - 1);
			}
		}
	}

	public void drawFloor(Graphics g){
		BufferedImage[] img = ImageManager.loadSprites("stone", 32, 32);
		int width = (Window.widthTile*tileByWidth)/32;
		int height = (Window.heightTile*tileByHeight)/32;
		int x = Window.marginLeft;
		int y = Window.margingTop;
		for(int w = 0; w < width; w++){
			for(int h = 0; h < height; h++){
				g.drawImage(img[floor[w][h]], x, y, 32, 32, null);
				x += 32;
			}
			x = Window.marginLeft;
			y += 32;
		}
	}

	public void affiche(Position posPlayer){
		for(int l = 0; l < this.width; l++){
			for(int c = 0; c < this.height; c++){
				if(rooms[c][l] != null){
					String str = "  ";
					if(l == posPlayer.getY() && c == posPlayer.getX()){
						str = "@ ";
					}
					if(rooms[c][l].haveTreasure()){
						str = "T ";
					}
					Room currentRoom = rooms[posPlayer.getX()][posPlayer.getY()];
					if(l == posPlayer.getY() - 1 && c == posPlayer.getX()){
						if(currentRoom.getDoor(1) != null){
							if(currentRoom.getDoor(1).isOpen()){
								str = "O ";
							}
						}
					}
					else if(l == posPlayer.getY() + 1 && c == posPlayer.getX()){
						if(currentRoom.getDoor(3) != null){
							if(currentRoom.getDoor(3).isOpen()){
								str = "O ";
							}
						}
					}
					else if(l == posPlayer.getY() && c == posPlayer.getX() - 1){
						if(currentRoom.getDoor(0) != null){
							if(currentRoom.getDoor(0).isOpen()){
								str = "O ";
							}
						}
					}
					else if(l == posPlayer.getY() && c == posPlayer.getX() + 1){
						if(currentRoom.getDoor(2) != null){
							if(currentRoom.getDoor(2).isOpen()){
								str = "O ";
							}
						}
					}
					//System.out.println("str = !"+str+"! et "+currentRoom.isVisited());
					if(str.equals("  ") && rooms[c][l].isVisited()){
						str = "X ";
					}/*else if(str.equals("  ") && !treasureOrPlayer){
						str = "";
					}*/
					//System.out.print(str);
				}
				else{ // salle vide
					//System.out.print("_ ");
					//System.out.print("  ");
				}
			}
			//System.out.println("");

		}


	}

	public static int stringMoveToInteger(String s){
		int direction = 0;
		if(s.equalsIgnoreCase("z")){
			direction = 1;
		}else if(s.equalsIgnoreCase("d")){
			direction = 2;
		}else if(s.equalsIgnoreCase("s")){
			direction = 3;
		}
		return direction;
	}

	public int orientationOponent(int position){
		if(position < 2){
			return position + 2;
		}else{
			return position - 2;
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param direction 0: Ouest, 1, Nord, 2: Est, 3:Sud
	 * @return
	 */
	public Position nextPos(int x, int y, int direction){
		if(direction == 0){
			if(x - 1 >= 0)
				return new Position(x - 1, y);
		}
		else if(direction == 1){
			if(y - 1 >= 0){
				return new Position(x, y - 1);
			}
		}else if(direction == 2){
			if(x + 1 < this.width){
				return new Position(x + 1, y);
			}
		}else if(direction == 3){
			if(y + 1 < this.height){
				return new Position(x, y + 1);
			}
		}
		return null;
	}

	public Room nextRoom(Room room, int orientation){
		Position pos = nextPos(room.getPosition().getX(), room.getPosition().getY(), orientation);
		if(pos != null){
			return rooms[pos.getX()][pos.getY()];
		}else{
			return null;
		}
	}


	public void addRoom(Room room, int x, int y){
		if(!roomHere(x, y)){
			rooms[x][y] = room;
		}
	}

	public Room getRandomRoom(){


		Room room = null;
		while(room == null){
			int x = (int) (Math.random()*this.getWidth());
			int y = (int) (Math.random()*this.getHeight());
			room = rooms[x][y];
		}
		return room;

	}

	public boolean roomHere(Position pos){
		return this.roomHere(pos.getX(), pos.getY());
	}

	public boolean roomHere(int x, int y){
		if(rooms[x][y] == null){
			return false;
		}
		return true;
	}

	public Room getSpawn() {
		return this.spawn;
	}

	public void initWindow() {
		this.setAbsolueX(0);
		this.setAbsolueY(0);
		this.setAbsolueWidth(Maze.tileByWidth * Window.widthTile);
		this.setAbsolueHeight(Maze.tileByHeight * Window.heightTile);
		this.generateFloor();
		/*int x = 0;
		int y = 0;
		for(int w = 0; w < this.getWidth(); w++){
			for(int h = 0; h < this.getHeight(); h++){
				rooms[w][h].setAbsolueX(x);
				rooms[w][h].setAbsolueY(y);
				x += Window.widthTile;
			}
			x = 0;
			y += Window.heightTile;
		}*/

	}



}
