package tesi.rmi.fileserver.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class FileServerService implements FileServerServiceInterface {

	public static final String CONFIG_FILEPATH = Utility.getResourcesPath(false) + File.separator + "rmi-config.properties";
	
	private static String ServerPolicyFilename;
	private static String StorageDirectoryPath;
	private static String ServerName;
	private static int Port;
	
	private static FileHandler handler;
	public static Logger logger = Logger.getLogger(FileServerService.class.getName());
	
	static {
		Utility.setLog(logger, handler, "ClientAppService.log");
	}
	
	@Override
	public boolean upload(String passenger, byte[] bytes) throws RemoteException {
		File passengerDirectory = new File(StorageDirectoryPath + File.separator + passenger);
		if(!passengerDirectory.exists())
			passengerDirectory.mkdir();
		try(FileOutputStream out = new FileOutputStream(passengerDirectory + File.separator + System.currentTimeMillis() + ".zip")){
			out.write(bytes);
			out.flush();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
		
		return true;
	}

	public static void main(String[] args) {
		try {
			loadConfigFile();
			System.setProperty("java.security.policy", ServerPolicyFilename);
			if(System.getSecurityManager() == null)
				System.setSecurityManager(new SecurityManager());
			FileServerService server = new FileServerService();
			FileServerServiceInterface stub = (FileServerServiceInterface) UnicastRemoteObject.exportObject(server, 1);
			Registry registry = LocateRegistry.createRegistry(Port);
			registry.rebind(ServerName, stub);
			System.out.println("File server started ...");
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	private static void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream input = new FileInputStream(CONFIG_FILEPATH)){
			Properties properties = new Properties();
			properties.load(input);
			try {
				Port = Integer.parseInt(properties.getProperty("file-server-rmi-port"));
			} catch (Exception e) {
				// TODO: Logger
				throw new MissingFormatArgumentException("RMI port does not exist.");
			}
			if(Port <= 0)
				throw new IllegalArgumentException("RMI port cannot be negative number.");
			ServerPolicyFilename = properties.getProperty("server-policy-file");
			if(ServerPolicyFilename == null)
				throw new MissingFormatArgumentException("Server policy file does not exist.");
			ServerPolicyFilename = Utility.getProjectRootPath(false) + ServerPolicyFilename;
			ServerName = properties.getProperty("file-server-rmi-server-name");
			if(ServerName == null)
				throw new MissingFormatArgumentException("RMI server name does not exist.");
			StorageDirectoryPath = properties.getProperty("file-server-storage");
			if(StorageDirectoryPath == null)
				throw new MissingFormatArgumentException("Documents storage is not defined.");
			StorageDirectoryPath = Utility.getProjectRootPath(false) + StorageDirectoryPath;
			File storageDirectory = new File(StorageDirectoryPath);
			if(!storageDirectory.exists())
				storageDirectory.mkdir();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
}
