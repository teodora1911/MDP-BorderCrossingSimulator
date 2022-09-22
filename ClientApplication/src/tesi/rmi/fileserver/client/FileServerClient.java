package tesi.rmi.fileserver.client;

import java.io.FileInputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.Level;

import tesi.model.CustomsDocument;
import tesi.rmi.fileserver.service.FileServerService;
import tesi.rmi.fileserver.service.FileServerServiceInterface;
import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class FileServerClient {

	private String clientPolicyFilename;
	private String serverName;
	private int port;
	private FileServerServiceInterface fileServerService;
	
	private static FileServerClient instance;
	
	private FileServerClient() throws LoadingConfigurationFileException, RemoteException, NotBoundException{
		loadConfigFile();
		System.setProperty("java.security.policy", clientPolicyFilename);
        if(System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        Registry registry = LocateRegistry.getRegistry(port);
        fileServerService = (FileServerServiceInterface) registry.lookup(serverName);
	}
	
	public static FileServerClient getInstance() {
		if(instance == null) {
			try {
				instance = new FileServerClient();
			} catch (Exception e) {
				FileServerService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
		return instance;
	}
	
	public boolean uploadFiles(CustomsDocument document) {
		try {
			if(document == null)
				return false;
			return fileServerService.upload(document.getPassenger(), document.getContent());
		} catch (Exception e) {
			FileServerService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return false;
		}
	}
	
	private void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream input = new FileInputStream(FileServerService.CONFIG_FILEPATH)){
			Properties properties = new Properties();
			properties.load(input);
			
			try {
				port = Integer.parseInt(properties.getProperty("file-server-rmi-port"));
			} catch (Exception e) {
				throw new MissingFormatArgumentException("RMI port does not exist.");
			}
			if(port <= 0)
				throw new IllegalArgumentException("RMI port cannot be negative number.");
			clientPolicyFilename = properties.getProperty("client-policy-file");
			if(clientPolicyFilename == null)
				throw new MissingFormatArgumentException("Client policu file does not exist.");
			clientPolicyFilename = Utility.getProjectRootPath(false) + clientPolicyFilename;
			serverName = properties.getProperty("file-server-rmi-server-name");
			if(serverName == null)
				throw new MissingFormatArgumentException("RMI server name does not exist.");
		} catch (Exception e) {
			FileServerService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
}
