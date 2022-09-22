package tesi.util.controls;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;

import tesi.model.CustomsDocument;
import tesi.model.Terminal;
import tesi.rmi.fileserver.client.FileServerClient;
import tesi.util.SOAPUtility;
import tesi.util.Utility;

public class CustomsControlThread extends ControlThread {
	
	public CustomsControlThread(Terminal terminal, Supplier<Boolean> temporaryStopCheck, Object lock, Consumer<String> sendNotification) {
		super(terminal, temporaryStopCheck, lock, sendNotification);
		setDaemon(true);
	}

	@Override
	public void run() {
		//System.out.println("Customs thread started ...");
		while(true) {
			try {
				synchronized (lock) {
					if(temporaryStopCheck.get())
						lock.wait();
				}
				
				CustomsDocument documents = SOAPUtility.getInstance().getNextPassengerDocuments(terminal.getKey());
				if(documents !=  null) {
					//System.out.println(documents.getPassenger());
					boolean filesSent = FileServerClient.getInstance().uploadFiles(documents);
					SOAPUtility.getInstance().logPassenger(documents.getPassenger());
					String message = filesSent ? "Dokumenti su poslati." : "Greska! Neuspjesno slanje dokumenata";
					sendNotification.accept(message);
				} else {
					sleep();
				}
			} catch (Exception e) {
				Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
	}
}
