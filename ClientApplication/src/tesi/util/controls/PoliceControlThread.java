package tesi.util.controls;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

import tesi.model.Terminal;
import tesi.rmi.warrants.client.ArrestWarrantClient;
import tesi.util.SOAPUtility;
import tesi.util.Utility;

public class PoliceControlThread extends ControlThread {
	
	public PoliceControlThread(Terminal terminal, Supplier<Boolean> temporaryStopCheck, Object lock, Consumer<String> sendNotification) {
		super(terminal, temporaryStopCheck, lock, sendNotification);
		setDaemon(true);
	}
	
	@Override
	public void run() {
		//System.out.println("Police thread started ...");
		while(true) {
			try {
				synchronized (lock) {
					if(temporaryStopCheck.get())
						lock.wait();
				}
				
				String passenger = SOAPUtility.getInstance().getNextPassenger(terminal.getKey());
				if(passenger != null) {
					//System.out.println(passenger);
					boolean passed = true;
					if(ArrestWarrantClient.getInstance().checkArrestWarrant(passenger)) {
						passed = false;
						sendNotification.accept("Privremeno zaustavljanje terminala {" + terminal.getName() + "}.");
					}
					SOAPUtility.getInstance().logOnWarrantPassenger(passenger, passed);
				} else {
					sleep();
				}
			} catch (Exception e) {
				Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
	}
}
