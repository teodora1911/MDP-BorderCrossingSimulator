package tesi.rmi.fileserver.model;

public class Passenger {
	
	private String id;
	private String accessedTime;
	
	public Passenger() { }

	public Passenger(String id, String accessedTime) {
		this.id = id;
		this.accessedTime = accessedTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccessedTime() {
		return accessedTime;
	}

	public void setAccessedTime(String accessedTime) {
		this.accessedTime = accessedTime;
	}
	
	@Override
	public String toString() {
		return id + " [" + accessedTime + "]";
	}
}
