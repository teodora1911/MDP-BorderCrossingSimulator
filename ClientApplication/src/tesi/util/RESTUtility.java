package tesi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.Level;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import tesi.model.User;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class RESTUtility {
	
	public static final String CONFIG_FILEPATH = Utility.getResourcesPath(false) + File.separator + "config.properties";
	
	private static RESTUtility instance = null;
	private static String BaseURL;
	
	private RESTUtility() {
		try {
			loadConfigFile();
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public static RESTUtility getInstance() {
		if(instance == null)
			instance = new RESTUtility();
		return instance;
	}
	
	private void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream input = new FileInputStream(CONFIG_FILEPATH)){
			Properties properties = new Properties();
			properties.load(input);
			BaseURL = properties.getProperty("database-url");
			if(BaseURL == null)
				throw new MissingFormatArgumentException("Osnovni URL za prisup bazi nije naveden.");
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
	
	public boolean login(User user) {
		boolean success = false;
		String url = BaseURL + "/prijava";
		String method = "POST";
		
		try {
			HttpURLConnection connection = prepareRequest(url, method);
			ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String jsonObjectAsString = writer.writeValueAsString(user);
			OutputStream out = connection.getOutputStream();
			out.write(jsonObjectAsString.getBytes());
			out.flush();
			
			int status = connection.getResponseCode();
			
			out.close();
			connection.disconnect();
			
			return status == HttpURLConnection.HTTP_OK;
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return success;
	}
	
	public boolean changePassword(User user) {
		if(user == null || user.getUsername() == null)
			return false;
		boolean success = false;
		try {
			String url = BaseURL + "/" + user.getUsername();
			String method = "PUT";
			HttpURLConnection connection = prepareRequest(url, method);
			String jsonString = "{ \"username\":\"" + user.getUsername() + "\",\"password\":\"" + user.getPassword() + "\",\"newPassword\":\"" + user.getNewPassword() + "\" }";
			OutputStream out = connection.getOutputStream();
			out.write(jsonString.getBytes());
			out.flush();
			
			success = connection.getResponseCode() == HttpURLConnection.HTTP_OK;
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return success;
	}
	
	private HttpURLConnection prepareRequest(String urlString, String method) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		return connection;
	}
}
