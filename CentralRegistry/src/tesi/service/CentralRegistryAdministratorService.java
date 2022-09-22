package tesi.service;

import tesi.model.Terminal;

public class CentralRegistryAdministratorService {

	private static final PassengerService passengerService = new PassengerService();
	private static final TerminalService terminalService = new TerminalService();
	
	// TERMINALS
	public Terminal getTerminal(String name) {
		return terminalService.getTerminalByName(name);
	}
	
	public boolean addTerminal(Terminal terminal) {
		return terminalService.addTerminal(terminal);
	}
	
	public boolean updateTerminal(Terminal terminal) {
		return terminalService.updateTerminal(terminal);
	}
	
	public boolean deleteTerminal(String name) {
		return terminalService.deleteTerminal(name);
	}
	
	// PASSENGERS
	public String[] getPassengers() {
		return passengerService.getPassengers();
	}
}
