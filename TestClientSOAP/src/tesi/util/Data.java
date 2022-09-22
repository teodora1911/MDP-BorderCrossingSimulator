package tesi.util;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collections;

import tesi.model.CustomsDocument;

public class Data {
	
	public static final Object policeLock = new Object();
	public static final Object customsLock = new Object();
	
	private static Map<String, LinkedList<String>> policeControls = Collections.synchronizedMap(new HashMap<>());
	private static Map<String, LinkedList<CustomsDocument>> customsControl = Collections.synchronizedMap(new HashMap<>());
	
	private static Set<String> stoppedTerminals = Collections.synchronizedSet(new HashSet<>());

	private static Map<String, Boolean> passengers = Collections.synchronizedMap(new HashMap<>());
	
	private Data() { }
	
	public static Map<String, LinkedList<String>> getPoliceControls() {
		return policeControls;
	}
	
	public static Map<String, LinkedList<CustomsDocument>> getCustomsControls() {
		return customsControl;
	}
	
	public static Set<String> getStoppedTerminals() {
		return stoppedTerminals;
	}
	
	public static Map<String, Boolean> getPassengers() {
		return passengers;
	}
	
	public static String getTerminalAsString(String terminalName, int entryId) {
		String toReturn = terminalName + "#";
		toReturn += String.valueOf(entryId);
		return toReturn;
	}
}
