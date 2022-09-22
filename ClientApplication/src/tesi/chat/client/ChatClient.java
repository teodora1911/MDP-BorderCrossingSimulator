package tesi.chat.client;

import static tesi.chat.model.User.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import tesi.chat.model.Message;
import tesi.chat.model.MessageType;
import tesi.model.User;
import tesi.chat.server.ChatServer;
import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class ChatClient extends Thread {
	
	private static int Port;
	private static String HostAddress;
	private static String KeystorePath;
	private static String KeystorePassword;
	
	private static String ConnectCommand;
	private static String GetMessagesCommand;
	private static String SendMessageCommand;
	private static String DisconnectCommand;
	
	private static String ConfirmationMessage;
	private static String RejectionMessage;
	private static String TemporaryStopMessage;
	private static String ContinueTerminalMessage;
	
	private static String IntraMessageSeparator;
	private static String InterMessageSeparator;
	private static String LoginSeparator;
	private static String CommandSeparator;
	
	private SSLSocket socket;
	private BufferedReader in;
	private PrintWriter out;

	private User user;
	private List<Message> messages = new ArrayList<>();
	
	private Consumer<Boolean> stopTerminal;
	private Consumer<List<Message>> pushMessages;
	private Consumer<String> sendNotificationMessage;
	
	static {
		try {
			loadConfigFile();
		} catch (Exception e) {
			ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public ChatClient(User user, Consumer<Boolean> stopTerminal, Consumer<List<Message>> pushMessages, Consumer<String> sendNotificationMessage) throws Exception {
		connectToServer();
		this.user = user;
		String command = ConnectCommand + CommandSeparator
				+ user.getUsername() + LoginSeparator
					+ user.getTerminal().getName() + LoginSeparator
						+ String.valueOf(user.getTerminal().getEntry()) + LoginSeparator 
							+ (user.getTerminal().isPolice() ? POLICE : CUSTOMS);
		out.println(command);
		String response = in.readLine();
		if(response.startsWith(RejectionMessage))
			throw new Exception(response);
		this.stopTerminal = stopTerminal;
		this.pushMessages = pushMessages;
		this.sendNotificationMessage = sendNotificationMessage;
		setDaemon(true);
		start();
	}
	
	@Override
	public void run() {
		System.out.println("Chat client thread started ...");
		while(true) {
			try {
				String messageListString = "";
				synchronized(this) {
					out.println(GetMessagesCommand);
					messageListString = in.readLine();
				}
				if(messageListString != null && !messageListString.isEmpty()) {
					String[] messageStringList = messageListString.split(InterMessageSeparator);
					for(String messageString : messageStringList) {
						Message message = parseMessage(messageString);
						if(message != null) {
							if(message.getContent().startsWith(TemporaryStopMessage))
								updateTerminalState(true);
							else if(message.getContent().startsWith(ContinueTerminalMessage))
								updateTerminalState(false);
							messages.add(message);
						}
					}
					pushMessages.accept(messages);
				}
			} catch (IOException e) {
				ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
	}
	
	// MESSAGE FORMAT: sender;type;content;timestamp
	private Message parseMessage(String messageString) {
		String[] messageParts = messageString.split(IntraMessageSeparator);
		if(messageParts.length == 4) {
			try {
				MessageType type = MessageType.valueOf(messageParts[1]);
				return new Message(type, messageParts[0], user.getUsername(), messageParts[2], messageParts[3]);
			} catch (Exception e) {
				// TODO: Logger
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	private void updateTerminalState(boolean stop) {
		stopTerminal.accept(Boolean.valueOf(stop));
		String message = stop ? "Privremeno zaustavljanje terminala!" : "Nastavak rada.";
		sendNotificationMessage.accept(message);
	}
	
	private String messageToString(MessageType type, String content, String terminal, Integer entry, Boolean police) {
		String messageString = type.toString() + IntraMessageSeparator + content;
		if(terminal != null) {
			messageString += IntraMessageSeparator + terminal;
			if(entry != null && police != null)
				messageString += IntraMessageSeparator + entry.toString() + IntraMessageSeparator + (police.booleanValue() ? POLICE : CUSTOMS);
		}
		return messageString;
	}
	
	private void sendMessage(String argument) {
		String command = SendMessageCommand + CommandSeparator + argument;
		boolean sent = false;
		try {
			synchronized(this) {
				out.println(command);
				String response = in.readLine();
				sent = response.startsWith(ConfirmationMessage);
			}
		} catch (IOException e) {
			ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		if(sent)
			sendNotificationMessage.accept("Uspjesno ste poslali poruku.");
	}
	
	// BROADCAST
	public void sendBroadcastMessage(String content) {
		if(content.startsWith(TemporaryStopMessage) || content.startsWith(ContinueTerminalMessage))
			return;
		String message = messageToString(MessageType.BROADCAST, content, null, null, null);
		sendMessage(message);
	}
	
	// MUTLICAST
	private void sendMulticastMessage(String terminal, String content, boolean temporaryStop) {
		if(!temporaryStop && (content.startsWith(TemporaryStopMessage) || content.startsWith(ContinueTerminalMessage)))
			return;
		sendMessage(messageToString(MessageType.MULTICAST, content, terminal, null, null));
	}
	
	public void sendMulticastMessage(String terminal, String content) {
		sendMulticastMessage(terminal, content, false);
	}
	
	public void notifyTemporaryStop() {
		sendMulticastMessage(user.getTerminal().getName(), TemporaryStopMessage, true);
	}
	
	public void notifyContinuation() {
		sendMulticastMessage(user.getTerminal().getName(), ContinueTerminalMessage, true);
	}
	
	// UNICAST
	public void sendUnicastMessage(String terminal, int entry, boolean police, String content) {
		if(content.startsWith(TemporaryStopMessage) || content.startsWith(ContinueTerminalMessage))
			return;
		sendMessage(messageToString(MessageType.UNICAST, content, terminal, Integer.valueOf(entry), Boolean.valueOf(police)));
	}
	
	public void logout() {
		out.println(DisconnectCommand);
		try {
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	private static void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream in = new FileInputStream(ChatServer.CONFIG_FILEPATH)){
			Properties properties = new Properties();
			properties.load(in);
			
			Port = Integer.parseInt(properties.getProperty("chat-port"));
			if(Port <= 0)
				throw new IllegalArgumentException("Port must be positive number.");
			KeystorePath = properties.getProperty("keystore-path");
			if(KeystorePath == null)
				throw new MissingFormatArgumentException("Keystore path is not defined.");
			KeystorePath = Utility.getProjectRootPath(false) + KeystorePath;
			KeystorePassword = properties.getProperty("keystore-password");
			if(KeystorePassword == null)
				throw new MissingFormatArgumentException("Keystore password is not defined.");
			
			ConnectCommand = properties.getProperty("connect-command");
			if(ConnectCommand == null)
				throw new MissingFormatArgumentException("Command for establishing connection is not defined.");
			GetMessagesCommand = properties.getProperty("get-messages-command");
			if(GetMessagesCommand == null)
				throw new MissingFormatArgumentException("Command for getting messages is not defined.");
			SendMessageCommand = properties.getProperty("send-message-command");
			if(SendMessageCommand == null)
				throw new MissingFormatArgumentException("Command for sending messages is not defined.");
			DisconnectCommand = properties.getProperty("disconnect-command");
			if(DisconnectCommand == null)
				throw new MissingFormatArgumentException("Command for disconnecting is not defined.");
			
			ConfirmationMessage = properties.getProperty("confirmation-message");
			if(ConfirmationMessage == null)
				throw new MissingFormatArgumentException("Confirmation message is not specified.");
			RejectionMessage = properties.getProperty("rejection-message");
			if(RejectionMessage == null)
				throw new MissingFormatArgumentException("Rejection message is not specified.");
			TemporaryStopMessage = properties.getProperty("temporary-stop-message");
			if(TemporaryStopMessage == null)
				throw new MissingFormatArgumentException("Temporary stop message is not specified.");
			ContinueTerminalMessage = properties.getProperty("continue-terminal-message");
			if(ContinueTerminalMessage == null)
				throw new MissingFormatArgumentException("Continue terminal message is not specified.");
			
			IntraMessageSeparator = properties.getProperty("intra-message-separator");
			if(IntraMessageSeparator == null)
				throw new MissingFormatArgumentException("Intra message separator is not specified.");
			InterMessageSeparator = properties.getProperty("inter-message-separator");
			if(InterMessageSeparator == null)
				throw new MissingFormatArgumentException("Inter message separator is not specified.");
			LoginSeparator = properties.getProperty("login-separator");
			if(LoginSeparator == null)
				throw new MissingFormatArgumentException("Login separator is not specified.");
			CommandSeparator = properties.getProperty("command-separator");
			if(CommandSeparator == null)
				throw new MissingFormatArgumentException("Command separator is not specified.");
		} catch (Exception e) {
			ChatServer.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
	
	private void connectToServer() throws UnknownHostException, IOException {
		System.setProperty("javax.net.ssl.trustStore", KeystorePath);
		System.setProperty("javax.net.ssl.trustStorePassword", KeystorePassword);
		
		SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		socket = (SSLSocket) socketFactory.createSocket(HostAddress, Port);
		
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
	}
}
