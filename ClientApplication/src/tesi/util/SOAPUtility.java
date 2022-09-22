package tesi.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.rpc.ServiceException;

import tesi.model.CustomsDocument;
import tesi.model.Terminal;
import tesi.service.CentralRegistryClientService;
import tesi.service.CentralRegistryClientServiceServiceLocator;
import tesi.service.SOAPClientService;
import tesi.service.SOAPClientServiceServiceLocator;
import tesi.rmi.fileserver.model.Passenger;

public class SOAPUtility {
	
	private static SOAPUtility instance;
	
	private SOAPClientService testService;
	private CentralRegistryClientService service;

	private SOAPUtility() throws ServiceException {
		SOAPClientServiceServiceLocator testLocator = new SOAPClientServiceServiceLocator();
		CentralRegistryClientServiceServiceLocator locator = new CentralRegistryClientServiceServiceLocator();
		
		testService = testLocator.getSOAPClientService();
		service = locator.getCentralRegistryClientService();
	}

	public static SOAPUtility getInstance() {
		if(instance == null) {
			try {
				instance = new SOAPUtility();
			} catch (ServiceException e) {
				Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
		
		return instance;
	}
	
	// terminals
	public Terminal login(String terminalName, int entry, boolean policeControl) {
		try {
			int response = service.login(terminalName, entry);
			if(response >= 0) {
				Terminal terminal = new Terminal(terminalName, null, entry, policeControl);
				String key = testService.login(terminalName, entry, policeControl);
				terminal.setKey(key);
				return (key == null) ? null : terminal;
			} else {
				return null;
			}
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return null;
		}
	}
	
	public void logout(Terminal terminal) {
		try {
			testService.logout(terminal.getKey(), terminal.isPolice());
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public void stopTerminal(String terminalName) {
		try {
			testService.stopTerminal(terminalName);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public void releaseTerminal(String terminalName) {
		try {
			testService.releaseTerminal(terminalName);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	// passengers
	public String getNextPassenger(String key) {
		try {
			return testService.getNextPassenger(key);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return null;
		}
	}
	
	public CustomsDocument getNextPassengerDocuments(String key) {
		try {
			return testService.getDocument(key);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return null;
		}
	}
	
	public void logOnWarrantPassenger(String passenger, boolean passed) {
		try {
			testService.updatePassenger(passenger, passed);
			if(!passed) {
				service.logPassengerOnWarrant(passenger);
			}
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public void logPassenger(String passenger) {
		try {
			service.logPassengerCrossed(passenger);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public List<Passenger> getPassengers() {
		List<Passenger> passengers = new ArrayList<>();
		try {
			String[] downloadedPassengers = service.getPassengers();
			if(downloadedPassengers != null)
				for(String line : downloadedPassengers)
					passengers.add(new Passenger(line.split(" ")[0], line.split(" ")[1]));
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return passengers;
	}
}
