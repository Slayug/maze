package maze;

import item.Item;

import java.util.ArrayList;
import java.util.Scanner;

import map.Maze;
import map.Position;
import entity.Cooker;
import entity.Entity;
import entity.Healer;
import entity.Player;

public class MazeGameConsole {
	
	private Player player;
	private Maze maze;

	public MazeGameConsole(Player player, Maze maze){		
		this.player = player;
		this.maze = maze;
		this.game();
	}
	
	public void game(){
		Scanner sc = new Scanner(System.in);
		/*System.out.println("Vous vous rï¿½veillez tranquillement comme chaque matin, en vous disant je remet mon rï¿½veil,");
		System.out.println("j'ai encore le temps.");

		System.out.println(help);
		System.out.println("----------------------------------------------------");*/

		String str = "";
		/*while(true){
			System.out.println("Prï¿½t ï¿½ rentrer ï¿½ la maison ?");
			str = sc.nextLine();
			if(str.equalsIgnoreCase("o")){
				System.out.println("C'est partie !");
				break;
			}
		}*/
		//System.out.println("Tu dï¿½marres avec:");
		//player.getRoom().affiche();
		boolean inComing = true;
		boolean hasMoved = true;
		Position currentPos = player.getRoom().getPosition();
		while(true){
			/*if(currentPos.getX() == player.getRoom().getPosition().getX() && currentPos.getY() == player.getRoom().getPosition().getY()){ // le joueur n'a pas changï¿½ de piï¿½ce
				inComing = false;
			}else{
				inComing = true;
			}*/
			currentPos = player.getRoom().getPosition();
			//maze.affiche(currentPos);
			player.affiche();
			System.out.println(player.getRoom().toString());


			if(player.getRoom().haveTreasure()){
				//print gg
				System.out.println("  _____    _____ ");
				System.out.println(" / ____|  / ____|");
				System.out.println("| |  __  | |  __ ");
				System.out.println("| | |_ | | | |_ |");
				System.out.println("| |__| | | |__| |");
				System.out.println(" \\_____|  \\_____|");
			}
			else if(hasMoved){
				ArrayList<Entity> entitys = maze.getEntitys();
				for(int p = 0; p < entitys.size(); p++){
					if(entitys.get(p).getRoom().equals(player.getRoom())){
						if(entitys.get(p) instanceof Healer){
							player.heal();
							//entitys.remove(p);
							System.out.println("Vous avez été soigné!!");
							break;
						}
						if(entitys.get(p) instanceof Cooker){
							player.eat();
							//entitys.remove(p);
							System.out.println("Miam Miam");
							System.out.println("yummmi yummi");
							System.out.println("groooorgoro");
							break;
						}
					}
				}
			}

			str = "";
			System.out.println("Que faire ?");
			str = sc.nextLine();
			int orientation = -1;
			hasMoved = false;
			if(str.equalsIgnoreCase("z")){ // monter
				hasMoved = true;
				orientation = 1;
			}else if(str.equalsIgnoreCase("s")){ // descendre
				hasMoved = true;
				orientation = 3;	
			}else if(str.equalsIgnoreCase("q")){ // gauche 
				hasMoved = true;
				orientation = 0;	
			}else if(str.equalsIgnoreCase("d")){ // droite
				hasMoved = true;
				orientation = 2;
			}
			else if(str.equalsIgnoreCase("p")){ // prendre un objet
				//gestion des items
				ArrayList<Item> items = player.getRoom().getItems();
				if(items.size() > 0){
					System.out.println("Il y'a "+items.size()+" objet(s) disponible.");
					boolean objectTaken = false;
					while(!objectTaken){
						System.out.println("Pour prendre un objet tapez son chiffre, sinon autre chose");
						String value = sc.nextLine();
						int item = -1;
						try{
							item = Integer.valueOf(value);
						}catch(NumberFormatException e){
							System.out.println("On passe à la suite !");
							objectTaken = true;
						}
						if(item != -1){
							if(item >= 0 && item < items.size()){
								//currentPos = player.getRoom().getPosition();
								Item itemSelected = items.get(item);
								Maze.rooms[currentPos.getX()][currentPos.getY()].removeItem(item);
								player.addItem(itemSelected);
								System.out.println("Objet prit avec succï¿½s !");
								objectTaken = true;
							}else{
								System.out.println("Aucun item de ce numï¿½ro !");
							}
						}
					}
				}else{
					System.out.println("Il n'y a aucun objet !");
				}
			}else if(str.equalsIgnoreCase("help")){ // HELP
				System.out.println(MazeGame.help);
			}else if(str.equalsIgnoreCase("u")){ // utiliser un objet si objet selectionné
				if(player.getItemSelected() != -1){
					player.useItem(player.getItemSelected());
					player.setItemSelect(-1);
				}else{ // si aucun objet séléctionné
					System.out.println("Merci de sélécionner un item, help pour vous aider.");
				}
			}else{
				//tester si un chiffre
				int nbre = -1;
				try{
					nbre = Integer.valueOf(str);

				}catch(NumberFormatException e){}

				if(nbre > -1){
					if(nbre < player.getItems().size()){
						player.setItemSelect(nbre);
						System.out.println("Item sï¿½lï¿½ctionï¿½: "+player.getItems().get(player.getItemSelected()).getName());
					}else{
						System.out.println("Cet id n'existe pas ! ");
					}
				}				
			}



			if(hasMoved){
				if(player.canMove(orientation)){
					Position posPlayer = player.getRoom().getPosition();

					if(player.getRoom().getDoor(orientation).isOpen()){
						Maze.rooms[currentPos.getX()][currentPos.getY()].setVisited(true);
						Position pos = maze.nextPos(posPlayer.getX(), posPlayer.getY(), orientation);
						player.move(Maze.rooms[pos.getX()][pos.getY()]);
					}else{
						if(player.haveKey()){

						}else{
							System.out.println("La porte est fermï¿½e ï¿½ clef et vous n'en avez pas !");
						}
					}
				}else{
					System.out.println("Aucune porte par lï¿½ !");
				}
				if(player.getLife() <= 0){
					System.out.println("T'es mort ! Crï¿½ve !");
					break;
				}
			}

		}

		
	}

}
