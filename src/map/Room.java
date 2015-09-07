package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import maze.MazeGame;
import window.Composant;
import window.ImageManager;
import window.Window;
import item.Item;
import item.Treasure;
import entity.Entity;


public class Room extends Composant{
	private Position pos;

	private ArrayList<Item> items;
	/**
	 * position 0: Ouest, 1, Nord, 2: Est, 3:Sud
	 */
	private Door[] doors;
	private boolean visited = false;

	public Room(Position pos){
		items = new ArrayList<Item>();
		doors = new Door[4];
		this.pos = pos;

		this.setAbsolueHeight(Window.heightTile);
		this.setAbsolueWidth(Window.widthTile);

	}

	public Room(int x, int y){
		this(new Position(x, y));
	}

	public Room(Position pos, Entity entity){
		this(pos);
	}

	public Room(Position pos, Item item){
		this(pos);
		items = new ArrayList<Item>();
	}

	public Door getDoor(int orientation){
		return doors[orientation];
	}

	public int getNbreDoor(){
		int count = 0;
		for(int p = 0; p < 4; p++){
			if(doors[p] != null){
				count ++;
			}
		}
		return count;
	}

	/**
	 * 
	 * @param position 0: Ouest, 1, Nord, 2: Est, 3:Sud
	 */
	public void addDoor(int orientation, Door door){
		doors[orientation] = door;
	}


	public void affiche()
	{
		System.out.println(this);
	}



	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public String toString(){
		String str = "";
		str += "Position "+pos+" ";
		String[] doorsName = new String[]{"Porte Ouest", "Porte Nord", "Porte Est", "Porte Sud"};
		for(int p = 0; p < 4; p ++){
			if(doors[p] != null)
				str += doorsName[p]+": "+doors[p]+" ";
		}
		for(int p = 0; p < this.items.size(); p++){
			str += items.get(p).getName()+"("+p+") ";
		}
		return str;

	}

	public void addItem(Item item){
		this.items.add(item);
	}

	public Position getPosition() {
		return this.pos;
	}

	public boolean haveTreasure() {
		for(int p = 0; p < this.items.size(); p++){
			if(items.get(p) instanceof Treasure){
				return true;
			}
		}
		return false;
	}

	public boolean hasItem(Item item){
		for(int p = 0; p < items.size(); p++){
			if(item.getClass().equals(items.get(p).getClass())){
				return true;
			}
		}
		return false;
	}

	public ArrayList<Item> getItems() {
		return this.items;
	}

	public void removeItem(int item) {
		this.items.remove(item);

	}

	public void removeItem(Item item){
		if(this.items.contains(item)){
			items.remove(item);
		}
	}

	public void paint(Graphics g){		

		//draw doors
		int widthDoor = 8;
		if(!MazeGame.player.seeMap){
			widthDoor *= 4;
		}
		g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
		// Gestion des murs en haut
		BufferedImage wallLight = ImageManager.images.get("wall_top_light"); 
		BufferedImage wallLightLeft = ImageManager.images.get("wall_top_left_light");
		BufferedImage wallLightRight = ImageManager.images.get("wall_top_right_light");
		BufferedImage stoneTop = ImageManager.images.get("stone_top");
		if(doors[1] != null){
			if(wallLight == null || wallLightLeft == null || wallLightRight == null || MazeGame.player.seeMap){
				g.fillRect(this.getAbsolueX(), this.getAbsolueY(), this.getAbsolueWidth()/3, widthDoor);	
				g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - this.getAbsolueWidth()/3,
						this.getAbsolueY(), this.getAbsolueHeight()/3,
						widthDoor);
			}else{
				int index = 0;
				int y = Window.margingTop;
				int third = ((Window.widthTile * Maze.tileByWidth)-32)/3;
				for(int x = Window.marginLeft; x < (Maze.tileByWidth*(Window.widthTile-2)); x += 32){
					if(x < third || x > third * 2){
						if(index == 0){
							g.drawImage(wallLightLeft, x, y, null);
						}else if(index == 1){
							g.drawImage(wallLight, x, y, null);
						}else if(index == 2){
							g.drawImage(wallLightRight, x, y, null);
							index = -1;
						}
						index++;
					}else{
						g.drawImage(stoneTop, x, y, null);
					}
				}
			}
			if(!doors[1].isOpen()){ // on dessin la porte si elle est fermée
				if(MazeGame.player.seeMap){
					g.setColor(Color.RED);
					g.fillRect(this.getAbsolueX() + this.getAbsolueWidth()/3, this.getAbsolueY(), this.getAbsolueWidth()/3, widthDoor);
					g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
				}else{
					BufferedImage door = ImageManager.images.get("door");
					g.drawImage(door, this.getAbsolueX() + this.getAbsolueWidth()/3 + door.getWidth()/2, this.getAbsolueY(), null);
				}
			}
		}else{
			if(wallLight == null || wallLightLeft == null || wallLightRight == null || MazeGame.player.seeMap){
				g.fillRect(this.getAbsolueX(), this.getAbsolueY(), this.getAbsolueWidth(), widthDoor);
			}else{
				int index = 0;
				int y = Window.margingTop;
				for(int x = Window.marginLeft + 16; x < (Maze.tileByWidth*(Window.widthTile-2)); x += 32){
					if(index == 0){
						g.drawImage(wallLightLeft, x, y, null);
					}else if(index == 1){
						g.drawImage(wallLight, x, y, null);
					}else if(index == 2){
						g.drawImage(wallLightRight, x, y, null);
						index = -1;
					}
					index++;
				}
			}
		}


		//Gestion des murs à gauche
		if(MazeGame.player.seeMap){
			if(doors[0] != null){
				g.fillRect(this.getAbsolueX(), this.getAbsolueY(), widthDoor, this.getAbsolueHeight()/3);
				g.fillRect(this.getAbsolueX(), this.getAbsolueY() + (this.getAbsolueHeight() - this.getAbsolueHeight()/3),
						widthDoor, this.getAbsolueHeight()/3);
				if(!doors[0].isOpen()){
					g.setColor(Color.red);
					g.fillRect(this.getAbsolueX(), this.getAbsolueY() + this.getAbsolueHeight()/3,
							widthDoor, this.getAbsolueHeight()/3);
					g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
				}
			}else{
				g.fillRect(this.getAbsolueX(), this.getAbsolueY(), widthDoor, this.getAbsolueHeight());
			}
		}else{
			//Gestion des murs à gauche
			BufferedImage wallRightTop = ImageManager.images.get("wall_right_top");
			if(doors[0] != null){
				int thirdHeight = (Window.heightTile * Maze.tileByHeight) / 3;
				BufferedImage stoneLeft = ImageManager.images.get("stone_left");
				g.fillRect(this.getAbsolueX(), Window.margingTop, 14, thirdHeight);
				if(wallRightTop != null && !MazeGame.player.seeMap){
					g.setColor(Color.BLACK);
					g.fillRect(this.getAbsolueX() + 14, Window.margingTop, 2, thirdHeight);
					for(int y = thirdHeight; y < 2 * thirdHeight; y += stoneLeft.getHeight()){
						g.drawImage(stoneLeft, this.getAbsolueX(), y, null);
					}
					g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
					g.fillRect(this.getAbsolueX(), 2 * thirdHeight, 14, thirdHeight);
					g.setColor(Color.BLACK);
					g.fillRect(this.getAbsolueX() + 14, 2 * thirdHeight, 2, thirdHeight);
				}
				if(!doors[0].isOpen()){ // la porte est fermée
					BufferedImage door = ImageManager.images.get("door");
					g.drawImage(door, this.getAbsolueX(), thirdHeight + 25, null);
				}
			}else{
				if(wallRightTop != null && !MazeGame.player.seeMap){
					g.fillRect(this.getAbsolueX(), this.getAbsolueY(), 14, this.getAbsolueHeight());
					g.setColor(Color.BLACK);
					g.fillRect(this.getAbsolueX() + 14, this.getAbsolueY(), 2, this.getAbsolueHeight());
				}else{
					g.fillRect(this.getAbsolueX(), this.getAbsolueY(), widthDoor, this.getAbsolueHeight());
				}
			}
			if(wallRightTop != null && !MazeGame.player.seeMap){
				g.drawImage(wallRightTop, this.getAbsolueX(), this.getAbsolueY(), null);
			}
		}

		g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));



		//Gestion des murs à droite
		if(MazeGame.player.seeMap){
			if(doors[2] != null){
				g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - widthDoor, this.getAbsolueY(), widthDoor, this.getAbsolueHeight()/3);
				g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - widthDoor,
						this.getAbsolueHeight() + this.getAbsolueY() - this.getAbsolueHeight()/3,
						widthDoor, this.getAbsolueHeight()/3);
				if(!doors[2].isOpen()){
					g.setColor(Color.red);
					g.fillRect(this.getAbsolueWidth() + this.getAbsolueX() - widthDoor,
							this.getAbsolueY() + this.getAbsolueHeight()/3,
							widthDoor, this.getAbsolueHeight()/3);
					g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
				}
			}else{
				g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - widthDoor, this.getAbsolueY(), widthDoor, this.getAbsolueHeight());
			}
		}else{
			BufferedImage wallRightTop = ImageManager.images.get("wall_right_top");
			if(doors[2] != null){
				int thirdHeight = (Window.heightTile * Maze.tileByHeight) / 3;
				BufferedImage stoneLeft = ImageManager.images.get("stone_left");
				g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - 14,
						Window.margingTop, 14, thirdHeight);
				if(wallRightTop != null && !MazeGame.player.seeMap){
					g.setColor(Color.BLACK);
					g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - 16,
							Window.margingTop, 2, thirdHeight);
					for(int y = thirdHeight; y < 2 * thirdHeight; y += stoneLeft.getHeight()){
						g.drawImage(stoneLeft, this.getAbsolueX() + this.getAbsolueWidth() - 16, y, null);
					}
					g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
					g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - 14, 2 * thirdHeight, 14, thirdHeight);
					g.setColor(Color.BLACK);
					g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - 16, 2 * thirdHeight, 2, thirdHeight);
				}
				if(!doors[2].isOpen()){ // la porte est fermée
					BufferedImage door = ImageManager.images.get("door");
					g.drawImage(door, this.getAbsolueX() + this.getAbsolueWidth() - door.getWidth(), thirdHeight + 25, null);
				}
			}else{
				if(wallRightTop != null && !MazeGame.player.seeMap){
					g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - 14, Window.margingTop, 14, this.getAbsolueHeight());
					g.setColor(Color.BLACK);
					g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - 16, Window.margingTop, 2, this.getAbsolueHeight());
				}else{
					g.fillRect(this.getAbsolueX(), this.getAbsolueY(), widthDoor, this.getAbsolueHeight());
				}
			}
			if(wallRightTop != null && !MazeGame.player.seeMap){
				g.drawImage(wallRightTop, this.getAbsolueX() + this.getAbsolueWidth() - 16, this.getAbsolueY(), null);
			}
		}




		//Gestion des murs en bas
		BufferedImage stoneBottom = ImageManager.images.get("stone_bottom");
		BufferedImage wallBottom = ImageManager.images.get("wall_bottom");
		if(doors[3] != null){
			if(wallLight == null || wallLightLeft == null || wallLightRight == null || MazeGame.player.seeMap){
				g.fillRect(this.getAbsolueX(),
						this.getAbsolueY() + this.getAbsolueHeight() - widthDoor,
						this.getAbsolueWidth()/3,
						widthDoor);
				g.fillRect(this.getAbsolueX() + this.getAbsolueWidth() - this.getAbsolueWidth()/3,
						this.getAbsolueY() + this.getAbsolueHeight() - widthDoor,
						this.getAbsolueWidth()/3, widthDoor);
			}else{
				int y = this.getAbsolueY() + this.getAbsolueHeight() - widthDoor;
				int third = ((Window.widthTile * Maze.tileByWidth)-32)/3;
				for(int x = Window.marginLeft; x < (Maze.tileByWidth*(Window.widthTile-2)); x += 32){
					if(x < third || x > third * 2){
						g.drawImage(wallBottom, x, y, null);
					}else{
						g.drawImage(stoneBottom, x, y, null);
					}
				}
			}
			if(!doors[3].isOpen()){ // Si la porte est fermée on l'affiche
				if(MazeGame.player.seeMap){
					g.setColor(Color.RED);
					g.fillRect(this.getAbsolueX() + this.getAbsolueWidth()/3, this.getAbsolueY() + this.getAbsolueHeight() - widthDoor,
							this.getAbsolueWidth()/3, widthDoor);
					g.setColor(Color.getHSBColor(0.83F, 0.051F, 0.305F));
				}else{
					BufferedImage door = ImageManager.images.get("door");
					g.drawImage(door, this.getAbsolueX() + this.getAbsolueWidth()/3 + door.getWidth()/2,
							this.getAbsolueY()+this.getAbsolueHeight()-door.getHeight(), null);
				}
			}
		}else{
			if(wallLight == null || wallLightLeft == null || wallLightRight == null || MazeGame.player.seeMap){
				g.fillRect(this.getAbsolueX(), this.getAbsolueY() + this.getAbsolueHeight() - widthDoor, this.getAbsolueWidth(), widthDoor);
			}else{
				int y = this.getAbsolueY() + this.getAbsolueHeight() - widthDoor;
				for(int x = Window.marginLeft; x < (Maze.tileByWidth*(Window.widthTile-2)); x += 32){
					g.drawImage(wallBottom, x, y, null);
				}
			}
		}

		if(this.haveTreasure() && MazeGame.player.seeMap){
			g.setColor(Color.magenta);
			g.fillRect(this.getAbsolueX() + 10, this.getAbsolueY() + 10, this.getAbsolueWidth()-20, this.getAbsolueHeight()-20);
		}
	}
} 
