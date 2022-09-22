package tesi.model;

public class Terminal {

	private String name;
	private String key;
	private int entry;
	private boolean police;
	
	public Terminal(String name, String key, int entry, boolean police) {
		this.name = name;
		this.key = key;
		this.entry = entry;
		this.police = police;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getEntry() {
		return entry;
	}

	public void setEntry(int entry) {
		this.entry = entry;
	}
	
	public boolean isPolice() {
		return police;
	}
	
	public void setPolice(boolean police) {
		this.police = police;
	}
}
