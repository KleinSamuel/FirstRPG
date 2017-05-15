package model;

import java.util.Timer;
import java.util.TimerTask;

public class Game implements Runnable{

	private Timer timer;
	private long deltaTime;
	private int counter;
	
	public WorldMap map;
	
	public Game(long deltaTime) {
		this.timer = new Timer();
		this.deltaTime = deltaTime;
		setCounter(0);
		
		map = new WorldMap();
	}
	
	@Override
	public void run() {
		timer.schedule(new GameLoop(), 0, deltaTime);
	}
	
	class GameLoop extends TimerTask {
		@Override
		public void run() {
			setCounter(getCounter() + 1);
		}
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

}
