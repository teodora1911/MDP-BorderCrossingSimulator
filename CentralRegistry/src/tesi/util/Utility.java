package tesi.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class Utility {
	
	public static final String PROJECT_ROOT = System.getProperty("user.dir") + File.separator + "CentralRegistry";
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
			
			setLog(logger, handler, "CentralRegistry.log");
		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}

	public static byte[] readAllBytes(InputStream in) throws IOException {
		final int length = 1024;
		byte[] buffer = new byte[length];
		int read;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while((read = in.read(buffer, 0, length)) != -1)
			baos.write(buffer, 0, read);
		byte[] bytes = baos.toByteArray();
		baos.close();
		return bytes;
	}
	
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
	
	private Utility() { } 
}