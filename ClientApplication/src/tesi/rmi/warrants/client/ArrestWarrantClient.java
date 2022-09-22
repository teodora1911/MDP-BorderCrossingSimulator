package tesi.rmi.warrants.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.Level;

import tesi.rmi.warrants.service.ArrestWarrantInterface;
import tesi.rmi.warrants.service.ArrestWarrantService;
import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class ArrestWarrantClient {

    private String clientPolicyFilename;
    private String serverName;
    private int port;
    private ArrestWarrantInterface arrestWarrantService;
    
    private static ArrestWarrantClient instance = null;

    private ArrestWarrantClient() throws LoadingConfigurationFileException, RemoteException, NotBoundException {
        loadConfigFile();
        System.setProperty("java.security.policy", clientPolicyFilename);
        if(System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        Registry registry = LocateRegistry.getRegistry(port);
        arrestWarrantService = (ArrestWarrantInterface) registry.lookup(serverName);
    }
    
    public static ArrestWarrantClient getInstance() {
    	if(instance == null) {
    		try {
    			instance = new ArrestWarrantClient();
    		} catch(Exception e) {
    			ArrestWarrantService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
    		}
    	}
    	return instance;
    }

    public boolean checkArrestWarrant(String personID) {
        try{
            return arrestWarrantService.hasArrestWarrant(personID);
        } catch (IOException e){
        	ArrestWarrantService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        	return false;
        }
    }

    private void loadConfigFile() throws LoadingConfigurationFileException {
        try(FileInputStream input = new FileInputStream(ArrestWarrantService.CONFIG_FILEPATH)){
            Properties properties = new Properties();
            properties.load(input);
            try{
                port = Integer.parseInt(properties.getProperty("warrant-rmi-port"));
            } catch (Exception e){
                throw new MissingFormatArgumentException("RMI port does not exist.");
            }
            if(port <= 0)
                throw new IllegalArgumentException("RMI port cannot be negative number.");
            clientPolicyFilename = properties.getProperty("client-policy-file");
            if(clientPolicyFilename == null)
                throw new MissingFormatArgumentException("Client policy file does not exist.");
            clientPolicyFilename = Utility.getProjectRootPath(false) + clientPolicyFilename;
            serverName = properties.getProperty("warrant-rmi-server-name");
            if(serverName == null)
                throw new MissingFormatArgumentException("RMI server name does not exist.");
        } catch (Exception e){
        	ArrestWarrantService.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            throw new LoadingConfigurationFileException(e);
        }
    }
}