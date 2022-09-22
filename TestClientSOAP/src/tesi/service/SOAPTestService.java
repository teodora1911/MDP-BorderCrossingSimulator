package tesi.service;

import java.util.Map;

import tesi.model.CustomsDocument;
import tesi.util.Data;

public class SOAPTestService {

	public String checkAvailability(String terminalName, int id) {
		if(Data.getStoppedTerminals().contains(terminalName))
			return null;
		String terminal = Data.getTerminalAsString(terminalName, id);
		return available(terminal) ? terminal : null;
	}
	
	private boolean available(String terminal) {
		return Data.getCustomsControls().containsKey(terminal) && Data.getPoliceControls().containsKey(terminal);
	}
	
	public void policeStop(String terminal, String passenger) {
		if(available(terminal)) {
			synchronized (Data.policeLock) {
				Data.getPoliceControls().get(terminal).offer(passenger);
			}
		}
	}
	
	public void customsStop(String terminal, CustomsDocument documents) {
		if(available(terminal)) {
			synchronized (Data.customsLock) {
				Data.getCustomsControls().get(terminal).offer(documents);
			}
		}
	}
	
	public int passed(String passenger) {
		Map<String, Boolean> passengers = Data.getPassengers();
		int value = 0;
		synchronized (passengers) {
			if(passengers.containsKey(passenger))
				value = passengers.get(passenger) ? 1 : -1;
		}
		return value;
	}
}
