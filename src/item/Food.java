package item;

/**
 * influe sur la foce
 *
 */
public class Food extends Item {
	private int value;
	private boolean good;
	
	public Food(String name) {
		// TODO Auto-generated constructor stub
		super(name);
		value = 2;
		good = true;
	}
	public Food(String name, int value, boolean good){
		super(name);
		this.value = value;
		this.good = good;
	}	
	public Food(String name, int value){
		super(name);
		this.value = value;
		this.good = true;
	}
	
	
	public int getValue(){
		if(good)
			return value;
		else
			return -value;
	}
	public boolean isGood(){
		return good;
	}
}
