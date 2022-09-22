package tesi.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CustomsDocument implements Serializable {
	
	private String passenger;
	private byte[] content;

	public CustomsDocument() {
		super();
	}
	
	public CustomsDocument(String passenger, byte[] content) {
		this.passenger = passenger;
		this.content = content;
	}

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
