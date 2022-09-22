package tesi.chat.model;

public class Message {

	private MessageType messageType;
	private String sender;
	private String receiver;
	private String content;
	private String timestamp;
	
	public Message() { }

	public Message(MessageType messageType, String sender, String receiver, String content, String timestamp) {
		this.messageType = messageType;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return sender + " [" + timestamp + "]";
	}
}
