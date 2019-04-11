package Scheduler;

public class Timer {
	long start;
	long delay;
	
	public Timer(long delay) {
		this.delay = delay;
	}
	
	public void start(long start) {
		this.start = start;
	}
	
	public void start() {
		this.start = System.currentTimeMillis();
	}
	
	public boolean isExpired() {
		return (System.currentTimeMillis() - this.start) > this.delay;
	}
}
