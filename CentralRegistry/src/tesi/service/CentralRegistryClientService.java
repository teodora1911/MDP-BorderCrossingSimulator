package tesi.service;

public class CentralRegistryClientService {

	public static final TerminalService terminalService = new TerminalService();
	public static final PassengerService passengerService = new PassengerService();
	
	// terminals
	public int login(String name,int entry) {
		return terminalService.login(name, entry);
	}
	
	// passengers
	public void logPassengerCrossed(String passenger) {
		passengerService.passengerCrossed(passenger);
	}
	
	public void logPassengerOnWarrant(String passenger) {
		passengerService.passengerOnWarrantDetected(passenger);
	}
	
	public String[] getPassengers() {
		return passengerService.getPassengers();
	}
}
