package tesi.util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.xml.rpc.ServiceException;

import tesi.model.Entry;
import tesi.model.Passenger;
import tesi.model.Terminal;
import tesi.service.CentralRegistryAdministratorService;
import tesi.service.CentralRegistryAdministratorServiceServiceLocator;

public class SOAPUtility {
	
	private static SOAPUtility instance;

	private CentralRegistryAdministratorService service;
	
	private SOAPUtility() throws ServiceException {
		CentralRegistryAdministratorServiceServiceLocator locator = new CentralRegistryAdministratorServiceServiceLocator();
		service = locator.getCentralRegistryAdministratorService();
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
	
	private Terminal createTerminal(String name, int in, int out) {
		Entry[] entries = new Entry[in + out];
		int counter = 0;
		for(; counter < in; ++counter)
			entries[counter] = new Entry(true, (counter + 1));
		for(; counter < in + out; ++counter)
			entries[counter] = new Entry(false, (counter + 1));
		Terminal terminal = new Terminal(0, entries, name);
		return terminal;
	}
	
	public boolean addTerminal(String name, int entries, int exits) {
		Terminal terminal = createTerminal(name, entries, exits);
		try {
			return service.addTerminal(terminal);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
	}
	
	private boolean updateTerminal(Terminal terminal) {
		try {
			return service.updateTerminal(terminal);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
	}
	
	public boolean updateTerminalName(Terminal terminal) {
		return updateTerminal(terminal);
	}
	
	public boolean updateTerminalEntries(int id, String name, int in, int out) {
		Terminal terminal = createTerminal(name, in, out);
		terminal.setID(id);
		return updateTerminal(terminal);
	}
	
	public Terminal getTerminal(String name) {
		Terminal terminal = null;
		try {
			terminal = service.getTerminal(name);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return terminal;
	}
	
	public boolean deleteTerminal(String name) {
		try {
			return service.deleteTerminal(name);
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
	}
	
	public List<Passenger> getPassengers() {
		List<Passenger> passengers = new ArrayList<>();
		try {
			String[] downloadedPassengers = service.getPassengers();
			if(downloadedPassengers != null)
				for(String p : downloadedPassengers)
					passengers.add(new Passenger(p.split(" ")[0], p.split(" ")[1]));
		} catch (RemoteException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return passengers;
	}
}
