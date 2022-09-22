package tesi.rmi.warrants.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

public class ArrestWarrantService implements ArrestWarrantInterface {

    public static final String CONFIG_FILEPATH = Utility.getResourcesPath(false) + File.separator + "rmi-config.properties";

    private static String ServerPolicyFilename;
    private static String ServerName;
    private static int Port;
    private static String ArrestWarrantsDatabaseFile;
    
    private static FileHandler handler;
    public static Logger logger = Logger.getLogger(ArrestWarrantService.class.getName());
    
    static {
    	Utility.setLog(logger, handler, "ClientAppService.log");
    }

    // Smatra se da je svaki identifikator osobe napisan u novom redu
    @Override
    public boolean hasArrestWarrant(String passenger) throws IOException {
        return Files.lines(Paths.get(ArrestWarrantsDatabaseFile)).anyMatch(line -> line.equals(passenger));
    }

    public static void main(String[] args) {
        try{
            loadConfigFile();
            System.setProperty("java.security.policy", ServerPolicyFilename);
            if(System.getSecurityManager() == null)
                System.setSecurityManager(new SecurityManager());
            ArrestWarrantService server = new ArrestWarrantService();
            ArrestWarrantInterface stub = (ArrestWarrantInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(Port);
            registry.rebind(ServerName, stub);
            System.out.println("Arrest warrant server started ...");
        } catch (Exception e){
        	logger.log(Level.SEVERE, e.fillInStackTrace().toString());
        }
    }

    private static void loadConfigFile() throws LoadingConfigurationFileException {
        try(FileInputStream input = new FileInputStream(CONFIG_FILEPATH)){
            Properties properties = new Properties();
            properties.load(input);
            try{
                Port = Integer.parseInt(properties.getProperty("warrant-rmi-port"));
            } catch (Exception e){
                // TODO: Logger
                throw new MissingFormatArgumentException("RMI port does not exist.");
            }
            if(Port <= 0)
                throw new IllegalArgumentException("RMI port cannot be negative number.");
            ServerPolicyFilename = properties.getProperty("server-policy-file");
            if(ServerPolicyFilename == null)
                throw new MissingFormatArgumentException("Server policy file does not exist.");
            ServerPolicyFilename = Utility.getProjectRootPath(false) + ServerPolicyFilename;
            ServerName = properties.getProperty("warrant-rmi-server-name");
            if(ServerName == null)
                throw new MissingFormatArgumentException("RMI server name does not exist.");
            ArrestWarrantsDatabaseFile = properties.getProperty("arrest-warrant-file");
            if(ArrestWarrantsDatabaseFile == null)
                throw new MissingFormatArgumentException("Warrants database is not specified.");
            ArrestWarrantsDatabaseFile = Utility.getProjectRootPath(false) + ArrestWarrantsDatabaseFile;
        } catch (Exception e){
        	logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            throw new LoadingConfigurationFileException(e);
        }
    }
}
