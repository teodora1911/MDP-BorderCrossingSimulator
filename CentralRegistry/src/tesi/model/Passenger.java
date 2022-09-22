package tesi.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Passenger {

	@JacksonXmlProperty(localName = "Id", isAttribute = true)
	private String id;
	@JacksonXmlProperty(localName = "AccessedTime")
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
