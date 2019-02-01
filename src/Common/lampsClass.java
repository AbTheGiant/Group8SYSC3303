package Common;

public class lampsClass {

	private boolean turnedOn;
	public  lampsClass() {
		// TODO Auto-generated constructor stub
		this.setLamps(false);
	}
	
	public void setLamps(boolean temp) {
		this.turnedOn=temp;
	}

	public boolean getLamps() {
		return turnedOn;
	}
	
}
