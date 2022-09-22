package tesi.util.controls;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

import tesi.model.Terminal;
import tesi.util.Utility;

public abstract class ControlThread extends Thread {
	
	public static final int SLEEP = 3000;

	protected Terminal terminal;
	protected Supplier<Boolean> temporaryStopCheck;
	protected Consumer<String> sendNotification;
	protected Object lock;
	
	public ControlThread(Terminal terminal, Supplier<Boolean> temporaryStopCheck, Object lock, Consumer<String> sendNotification) {
		this.terminal = terminal;
		this.temporaryStopCheck = temporaryStopCheck;
		this.lock = lock;
		this.sendNotification = sendNotification;
	}
	
	public void sleep() {
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
}
