package window;

import javax.swing.JFrame;

import maze.MazeGame;

public class Window extends JFrame{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2841570605382137949L;
	public static int widthTile;
	public static int heightTile;
	
	/**
	 * Le point de rep�re 0 en abscisse et 0 en ordonn� ne s'affiche pas � cause du bord de la fen�tre,
	 * il faut donc ajouter cette valeur � chaque composant affich� pour le voir si il se trouve
	 * � la position 0, 0 par exemple
	 */
	public final static int margingTop = 30;	
	public final static int marginLeft = 9;
	
	
	public Window(int width, int height){
		this.setTitle("Maze");
		this.addKeyListener(MazeGame.player);
		this.setSize(width, height);
		
		widthTile = heightTile = width / 15;
		this.setResizable(false);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	

}
