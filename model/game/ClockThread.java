package model.game;

/**
 * The Clock Thread is responsible for producing a consistent "pulse" which is
 * used to update the game state, and refresh the game.
 * 
 * @author yaoyucui
 */
public class ClockThread extends Thread {
	private int delay;
	private Game game;

	/**
	 * clock thread constructor
	 * @param delay
	 * @param game
	 */
	public ClockThread(int delay, Game game) {
		this.delay = delay;
		this.game = game;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(delay);
				game.clockTick();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
