package tesi.model;

import java.io.Serializable;

public class Entry implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean entry;
	private int id;
	
	public Entry() { }
	
	public Entry(int id, boolean entry) {
		this.id = id;
		this.entry = entry;
	}

	public boolean isEntry() {
		return entry;
	}

	public void setEntry(boolean entry) {
		this.entry = entry;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
