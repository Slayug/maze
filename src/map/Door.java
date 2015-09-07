package map;

public class Door {
	
	private int number;
	private boolean isOpen;
	
	public Door()
	{
		this.isOpen = false;
	}
	
	public Door(boolean isOpen){
		this.isOpen = isOpen;
	}
	
	public Door(boolean isOpen, int i)
	{
		number = i;
		this.isOpen = false;
	}
	

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	

	public int getNumber() {
		return number;
	}

	public String toString(){
		String str = "";
		if(isOpen)
			str += "Ouverte ";
		else{
			str += "Fermée";
			str += "Clef: "+this.number;
		}
		return str;
		
	}

	public void open() {
		this.isOpen = true;		
	}
}
