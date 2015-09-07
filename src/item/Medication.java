package item;

/**
 * Influe sur la vie
 */
public class Medication extends Item {

	private int value;
	private boolean good;
	public Medication(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public Medication(String name, int value, boolean good){
		super(name);
		this.value = value;
		this.good = good;
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
