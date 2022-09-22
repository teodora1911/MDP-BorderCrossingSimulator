package tesi.service;

import java.util.LinkedList;

import tesi.util.Data;
import tesi.model.CustomsDocument;

public class SOAPClientService {

	public String login(String terminalName, int id, boolean police) {
		String terminal = Data.getTerminalAsString(terminalName, id);
		if(police && !Data.getPoliceControls().containsKey(terminal)) {
			Data.getPoliceControls().put(terminal, new LinkedList<>());
			return terminal;
		}
		else if(!police &&!Data.getCustomsControls().containsKey(terminal)) {
			Data.getCustomsControls().put(terminal, new LinkedList<>());
			return terminal;
		}
		return null;
	}
	
	public void stopTerminal(String terminalName) {
		Data.getStoppedTerminals().add(terminalName);
	}
	
	public void releaseTerminal(String terminalName) {
		Data.getStoppedTerminals().remove(terminalName);
	}
	
	public void logout(String terminal, boolean police) {
		if(police)
			Data.getPoliceControls().remove(terminal);
		else
			Data.getCustomsControls().remove(terminal);
	}
	
	public String getNextPassenger(String terminal) {
		if(Data.getPoliceControls().containsKey(terminal)) {
			String passenger = null;
			synchronized(Data.policeLock) {
				passenger = Data.getPoliceControls().get(terminal).pollFirst();
			}
			return passenger;
		}
		return null;
	}
	
	public CustomsDocument getDocument(String terminal) {
		if(Data.getCustomsControls().containsKey(terminal)) {
			CustomsDocument documents = null;
			synchronized(Data.customsLock) {
				documents = Data.getCustomsControls().get(terminal).pollFirst();
			}
			return documents;
		}
		return null;
	}
	
	public void updatePassenger(String passenger, boolean passed) {
		Data.getPassengers().put(passenger, passed);
	}
}
