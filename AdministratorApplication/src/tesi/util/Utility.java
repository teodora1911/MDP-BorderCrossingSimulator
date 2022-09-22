package tesi.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Utility {
	
	public static final String PROJECT_ROOT = System.getProperty("user.dir");
	public static final String RESOURCES_PATH = PROJECT_ROOT + File.separator + "resources";
	public static final String CONFIG_FILE_PATH = RESOURCES_PATH + File.separator + "config.properties";
	public static final String LOG_PATH = RESOURCES_PATH + File.separator + "log";
	
	private static FileHandler handler;
	public static Logger logger = Logger.getLogger(Utility.class.getName());
	
	static {
		try {
			File logDirectory = new File(LOG_PATH);
			if(!logDirectory.exists())
				logDirectory.mkdir();
			
			setLog(logger, handler, "AdministratorApp.log");
		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	private Utility() { }
	
	public static void setLog(Logger logger, FileHandler handler, String loggerFile) {
		try {
			handler = new FileHandler(LOG_PATH + File.separator + loggerFile, true);
			handler.setFormatter(new SimpleFormatter());
			logger.setUseParentHandlers(false);
			logger.addHandler(handler);
		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
}
