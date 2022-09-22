package tesi.model;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;

@JacksonXmlRootElement(localName = "Passengers")
public class Passengers {
	
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "Passenger")
	private List<Passenger> passengers = new ArrayList<>();

	public Passengers() { }
	
	public List<Passenger> getPassengers() {
		return passengers;
	}
	
	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}
}
