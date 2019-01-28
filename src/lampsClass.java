
public class lampsClass {

	private boolean turnedOn;
	public  lampsClass() {
		// TODO Auto-generated constructor stub
		this.setButton(false);
	}
	
	public void setButton(boolean temp) {
		this.turnedOn=temp;
	}

	public boolean getButton() {
		return turnedOn;
	}
	
}
