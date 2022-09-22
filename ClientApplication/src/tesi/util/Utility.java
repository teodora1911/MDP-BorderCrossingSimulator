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
	
	private static final String USER_DIR = System.getProperty("user.dir");
	private static final String RESOURCES_RELATIVE_PATH = File.separator + "resources";
	private static String LogPath = "";
	
	private static FileHandler handler;
	public static Logger logger = Logger.getLogger(Utility.class.getName());
	
	static {
		try {
			String ResourcesDirectoryPath = USER_DIR + RESOURCES_RELATIVE_PATH;
			String RelativeLog = File.separator + "log";
			File resourcesDirectory = new File(ResourcesDirectoryPath);
			LogPath = (resourcesDirectory.exists()) ? ResourcesDirectoryPath + RelativeLog : getResourcesPath(true) + RelativeLog;
			File logDirectory = new File(LogPath);
			if(!logDirectory.exists())
				logDirectory.mkdir();
			
			setLog(logger, handler, "Util.log");
		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public static final String SERVER_PROJECT_PATH = USER_DIR + File.separator + "ClientApplication";
	
	public static String getProjectRootPath(boolean server) {
		return server ? SERVER_PROJECT_PATH : USER_DIR;
	}
	
	public static String getResourcesPath(boolean server) {
		return server ? SERVER_PROJECT_PATH + RESOURCES_RELATIVE_PATH : USER_DIR + RESOURCES_RELATIVE_PATH;
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
			handler = new FileHandler(LogPath + File.separator + loggerFile, true);
			handler.setFormatter(new SimpleFormatter());
			logger.setUseParentHandlers(false);
			logger.addHandler(handler);
		} catch (Exception e) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	private Utility() { }
}
