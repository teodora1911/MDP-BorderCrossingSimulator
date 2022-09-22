package tesi.util.serialization;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class SerializationFactory {
	
	private String SerializationRootDirectory;
	private static SerializationFactory instance;
	
	private ArrayList<SerializationUtility> supportedUtilities;
	
	private SerializationFactory() throws LoadingConfigurationFileException { 
		loadConfigProperties();
		initializeUtilities();
	}
	
	private static FileHandler handler;
	public static Logger logger = Logger.getLogger(SerializationFactory.class.getName());
	
	static {
		Utility.setLog(logger, handler, "CentralRegistry.log");
	}
	
	public static SerializationFactory getInstance() {
		if(instance == null) {
			try {
				instance = new SerializationFactory();
			} catch (Exception e) {
				SerializationFactory.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			}
		}
		return instance;
	}
	
	public SerializationUtility getSerializationUtility(int id) {
		--id;
		id %= supportedUtilities.size();
		return supportedUtilities.get(id);
	}
	
	private void loadConfigProperties() throws LoadingConfigurationFileException {
		try(InputStream input = new FileInputStream(Utility.CONFIG_FILE_PATH)){
            Properties properties = new Properties();
            properties.load(input);
            SerializationRootDirectory = properties.getProperty("serialization-folder");
            if(SerializationRootDirectory == null)
                throw new MissingFormatArgumentException("Serialization folder is not specified.");
            SerializationRootDirectory = Utility.PROJECT_ROOT + SerializationRootDirectory;
        } catch (Exception e){
        	SerializationFactory.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
            throw new LoadingConfigurationFileException(e);
        }
	}
	
	private void initializeUtilities() {
		supportedUtilities = new ArrayList<>(SerializationType.values().length);
		for(SerializationType type : SerializationType.values()) {
			switch(type) {
				case JAVA:
					supportedUtilities.add(new JavaSerialization(SerializationRootDirectory));
					break;
				case SMILE:
					supportedUtilities.add(new SmileSerialization(SerializationRootDirectory));
					break;
				case KRYO:
					supportedUtilities.add(new KryoSerialization(SerializationRootDirectory));
					break;
				case BSON:
					supportedUtilities.add(new BsonSerialization(SerializationRootDirectory));
			}
		}
	}
	
	public String getSerializationRootDirectory() {
		return SerializationRootDirectory;
	}
}
