package Common;

public class buttonClass {
	private boolean pushed;
	public buttonClass() {
		this.setButton(false);
	}
	
	public void setButton(boolean temp) {
		this.pushed=temp;
	}

	public boolean getButton() {
		return pushed;
	}
}
