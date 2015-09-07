package window;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageManager {

	public static Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	public ImageManager(){
		loadImages();
	}

	private void loadImages(){
		File folder = new File("res/");
		File[] files = folder.listFiles();

		for(File file : files){
			BufferedImage image = null;
			if(file.getName().contains(".png")){
				try {
					image = ImageIO.read(file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(image == null){
					System.err.println("Erreur chargement image: "+file.getName());
				}else{
					images.put(file.getName().replace(".png", ""), image);
				}
			}
		}


	}

	/**
	 * Permet de retourner un tableau de sprite (permettant ensuite de dérouler les sriptes pour avoir une animtion)
	 * @param name le nom de l'image
	 * @param w la largeur d'un sprite
	 * @param h la hauteur d'un sprite, revient à la hauteur de l'image
	 * @return 
	 */
	public static BufferedImage[] loadSprites(String name, int w, int h){
		BufferedImage img = ImageManager.images.get(name);
		if(img == null){
			return null;
		}
		BufferedImage[] sprites = new BufferedImage[img.getWidth() / w];
		int index = 0;
		for(int x = 0; x < img.getWidth(); x += w){
			sprites[index] = img.getSubimage(x, 0, w, h);
			index++;
		}
		return sprites;
	}
	
	/**
	 * 
	 * @param folderName dossier à passer contenant les images à assembler
	 * @param posX position x où commencer à découper
	 * @param posY position y où commencer à découper
	 * @param width largeur de la séléction à prendre sur l'image
	 * @param height hauteur de la séléction à prendre sur l'image
	 * Pour prendre l'image en entière mettre nomDuDossier, 0, 0, largeurImage, hauteurImage
	 * @return vrai si l'image finale a bien été créee
	 */
	public static boolean createSpriteAnimation(String folderName, int posX, int posY, int width, int height){
		File folder = new File(folderName);
		if(folder.isDirectory()){
			int nbre = folder.listFiles().length;
			File[] files = folder.listFiles();
			BufferedImage sprite = null;
			if(nbre > 0){
				sprite = new BufferedImage(width * nbre, height, BufferedImage.TYPE_INT_ARGB);
				if(sprite != null){ // ajout des différents sprites sur le final
					int x = 0;
					for(int index = 0; index < nbre; index++){
						BufferedImage img;
						try {
							img = ImageIO.read(files[index]);
							System.out.println(files[index].getName());
							img = img.getSubimage(posX, posY, width, height);
							sprite.setRGB(x, 0, width, height, img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth()), 0, width);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						x += width;
					}
					try {
						ImageIO.write(sprite, "png", new File(folderName+".png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}else{
			return false;
		}

		return true;
	}

}
