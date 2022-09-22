package tesi.chat.server;

import static tesi.chat.model.User.*;
import static tesi.chat.server.ChatServer.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.List;

import tesi.chat.model.Message;
import tesi.chat.model.MessageType;
import tesi.chat.model.User;

public class ChatThread extends Thread {
	
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private User user;
	
	public ChatThread(Socket socket) {
		this.socket = socket;
		this.user = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
 		} catch (IOException e) {
 			ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
 		}
		start();
	}
	
	@Override
	public void run() {
		System.out.println("User successfully established connection.");
		String option = "";
		String command = "";
		boolean registered = false;
		
		// Klijent se prvo mora da prijavi
		while(!registered) {
			try {
				option = in.readLine();
				String[] parts = option.split(CommandSeparator);
				command = parts[0];
				if(command.equals(ConnectCommand)) {
					if(parts.length != 2) {
						out.println(RejectionMessage + ": Broj argumenata nije validan.");
					} else if (!connect(parts[1])) {
						out.println(RejectionMessage + ": Neuspjesna prijava. Pokusajte ponovo.");
					} else {
						out.println(ConfirmationMessage + ": Uspjesno ste se prijavili.");
						registered = true;
					}
				} else if(command.equals(DisconnectCommand)) {
					disconnect();
					return;
				}
			} catch (IOException e) {
				ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
			
		}
		
		while(!command.equals(DisconnectCommand)) {
			try {
				option = in.readLine();
				String[] parts = option.split(CommandSeparator);
				command = parts[0];
				
				if(command.equals(GetMessagesCommand)) {
					// GET ALL MESSAGES
					String messageList = "";
					synchronized(ChatServer.messages) {
						LinkedList<Message> messages = ChatServer.messages.get(user);
						List<Message> copyOfMessages = new ArrayList<>();
						if(messages != null) {
							while(!messages.isEmpty())
								copyOfMessages.add(messages.poll());
						}
						messageList = messageListToString(copyOfMessages);
					}
					out.println(messageList);
				} else if(command.equals(SendMessageCommand)) {
					// SEND MESSAGE
					if(parts.length != 2) {
						out.println(RejectionMessage + ": Broj argumenata nije validan.");
					}
					if(sendMessage(parts[1])) {
						out.println(ConfirmationMessage + ": Uspjesno ste poslali poruku.");
					} else {
						out.println(RejectionMessage + ": Poruka nije poslata. Pokusajte ponovo.");
					}
				} else if(command.equals(DisconnectCommand)) {
					// Nista ... Izlazak iz petlje 
				} else {
					out.println(RejectionMessage + ": Komanda nije podrzana.");
				}
			} catch (IOException e) {
				ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
		
		disconnect();
	}
	
	/*
	 * LOGIN FORMAT: username#terminal#entry#control
	 * 
	 * terminal : naziv terminala
	 * entry : broj ulaza/izlaza
	 * control : P - policijski | C - carinski
	 */
	private boolean connect(String argument) {
		String [] parts = argument.split(LoginSeparator);
		if(parts.length != 4)
			return false;
		
		String username = parts[0];
		String terminal = parts[1];
		int entry = 0;
		try {
			entry = Integer.parseInt(parts[2]);
		} catch (Exception e) {
			ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
		
		String control = parts[3];
		if(!control.equals(POLICE) && !control.equals(CUSTOMS))
			return false;
		
		user = new User(username, terminal, entry, control);
		synchronized(ChatServer.messages) {
			messages.put(user, new LinkedList<>());
		}
		return true;
	}
	
	private void disconnect() {
		if(user != null)
			synchronized(ChatServer.messages) {
				messages.remove(user);
			}
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		System.out.println("Disconnecting ...");
	}
	
	/*
	 * MESSAGE FORMAT: sender;type;content;timestamp
	 */
	private String messageToString(Message message) {
		return message.getSender() + IntraMessageSeparator 
				+ message.getMessageType().toString() + IntraMessageSeparator
					+ message.getContent() + IntraMessageSeparator 
						+ message.getTimestamp();
	}
	
	/*
	 * LIST OF MESSAGES FORMAT: message1#message2#...
	 */
	private String messageListToString(List<Message> messages) {
		String toReturn = "";
		if(messages != null) {
			for(int i = 0; i < messages.size(); ++i) {
				toReturn += messageToString(messages.get(i));
				if(i != messages.size() - 1)
					toReturn += InterMessageSeparator;
			}
		}
		return toReturn;
	}
	
	/*
	 * FORMAT: type;content;<terminal>;<entry>;<control> 
	 */
	private boolean sendMessage(String argument) {
		boolean sent = false;
		String[] parts = argument.split(IntraMessageSeparator);
		if(parts.length < 2)
			return false;
		MessageType type;
		try {
			type = MessageType.valueOf(parts[0]);
			String content = parts[1];
			Message message = new Message(type, user.getUsername(), null, content, LocalDateTime.now().toString());
			switch(type) {
				case BROADCAST:
					sendBroadcastMessage(message);
					sent = true;
					break;
				case MULTICAST:
					if(parts.length == 3) {
						String terminal = parts[2];
						sendMulticastMessage(terminal, message);
						sent = true;
					}
					break;
				case UNICAST:
					if(parts.length == 5) {
						String terminal = parts[2];
						int entry;
						String control;
						try {
							entry = Integer.parseInt(parts[3]);
							control = parts[4];
							if(!control.equals(POLICE) && !control.equals(CUSTOMS))
								throw new IllegalArgumentException();
							sent = sendUnicastMessage(terminal, entry, control, message);
						} catch (Exception e) {
							ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
						}
					}
					break;
				default:
					break;
			}
		} catch (Exception e) {
			ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return sent;
	}
	
	private void sendBroadcastMessage(Message message) {
		synchronized(ChatServer.messages) {
			ChatServer.messages.forEach((user, messages) -> {
				if(!user.equals(ChatThread.this.user)) {
					message.setReceiver(user.getUsername());
					messages.add(message);
				}
			});
		}
	}
	
	private void sendMulticastMessage(String terminal, Message message) {
		synchronized (ChatServer.messages) {
			ChatServer.messages.forEach((user, messages) -> {
				if(user.getTerminal().equals(terminal) && !user.equals(ChatThread.this.user)) {
					message.setReceiver(user.getUsername());
					messages.add(message);
				}
			});
		}
	}
	
	private boolean sendUnicastMessage(String terminal, int entry, String control, Message message) {
		boolean sent = false;
		synchronized (ChatServer.messages) {
			for(Map.Entry<User, LinkedList<Message>> el : ChatServer.messages.entrySet()) {
				if(!el.getKey().equals(ChatThread.this.user) && el.getKey().getTerminal().equals(terminal) && el.getKey().getEntry() == entry && el.getKey().getControl().equals(control)) {
					message.setReceiver(el.getKey().getUsername());
					el.getValue().offer(message);
					sent = true;
					break;
				}
			}
		}
		return sent;
	}
}
