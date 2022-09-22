package tesi.chat.server;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.ServerSocket;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.MissingFormatArgumentException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocketFactory;

import tesi.util.Utility;
import tesi.chat.model.User;
import tesi.chat.model.Message;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class ChatServer {
	
	public static final String CONFIG_FILEPATH = Utility.getResourcesPath(false) + File.separator + "chat-config.properties";
	
	private static int Port;
	private static String KeystorePath;
	private static String KeystorePassword;
	
	static String ConnectCommand;
	static String GetMessagesCommand;
	static String SendMessageCommand;
	static String DisconnectCommand;
	
	static String ConfirmationMessage;
	static String RejectionMessage;
	
	static String IntraMessageSeparator;
	static String InterMessageSeparator;
	static String LoginSeparator;
	static String CommandSeparator;
	
	private static FileHandler handler;
	public static Logger logger = Logger.getLogger(ChatServer.class.getName());
	
	static {
		try {
			Utility.setLog(logger, handler, "ChatApp.log");
			loadConfigFile();
		} catch (LoadingConfigurationFileException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	private static void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream in = new FileInputStream(CONFIG_FILEPATH)){
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
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
	
	public static HashMap<User, LinkedList<Message>> messages = new HashMap<>();
	
	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.keyStore", KeystorePath);
		System.setProperty("javax.net.ssl.keyStorePassword", KeystorePassword);
		try {
			SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			ServerSocket serverSocket = serverSocketFactory.createServerSocket(Port);
			System.out.println("Chat server is running ...");
			while(true) {
				SSLSocket sslSocket = (SSLSocket) serverSocket.accept();
				new ChatThread(sslSocket);
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
}
