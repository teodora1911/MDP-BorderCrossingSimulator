package tesi.util;

import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class Utility {
	
	public static final String PROJECT_PATH = System.getProperty("user.dir");
	public static final String RESOURCE_PATH = PROJECT_PATH + File.separator + "resources";
	public static final String CONFIG_FILE = RESOURCE_PATH + File.separator + "config.properties";
	public static final String LOG_PATH = RESOURCE_PATH + File.separator + "log";

	
	static {
		try {
			File logDirectory = new File(LOG_PATH);
			if(!logDirectory.exists())
				logDirectory.mkdir();
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
