package view.renderTesting;

public class Clock extends Thread {
	private int delay;
	private GameTest game;

	public Clock(int delay, GameTest game) {
		this.delay = delay;
		this.game = game;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(delay);
				game.clockTick();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
