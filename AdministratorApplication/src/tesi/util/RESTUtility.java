package tesi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import tesi.model.Passenger;
import tesi.model.User;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class RESTUtility {
	
	private static RESTUtility instance = null;
	private String DatabaseURL;
	private String FileServerURL;
	private String WarrantsURL;
	private String CustomsDocumentsDirectory;
	
	private void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream input = new FileInputStream(Utility.CONFIG_FILE_PATH)){
			Properties properties = new Properties();
			properties.load(input);
			
			DatabaseURL = properties.getProperty("database-url");
			if(DatabaseURL == null)
				throw new MissingFormatArgumentException("Database URL is not defined.");
			FileServerURL = properties.getProperty("file-server-url");
			if(FileServerURL == null)
				throw new MissingFormatArgumentException("File Server URL is not defined.");
			WarrantsURL = properties.getProperty("warrants-url");
			if(WarrantsURL == null)
				throw new MissingFormatArgumentException("Warrants URL is not defined.");
			CustomsDocumentsDirectory = properties.getProperty("customs-documents");
			if(CustomsDocumentsDirectory == null)
				throw new MissingFormatArgumentException("Customs documents directory is not defined.");
			CustomsDocumentsDirectory = Utility.PROJECT_ROOT + CustomsDocumentsDirectory;
			File dir = new File(CustomsDocumentsDirectory);
			if(!dir.exists())
				dir.mkdir();
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
	
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
	
	public String getDatabaseURL() {
		return DatabaseURL;
	}

	public String getFileServerURL() {
		return FileServerURL;
	}

	public String getWarrantsURL() {
		return WarrantsURL;
	}

	public String getCustomsDocumentsDirectory() {
		return CustomsDocumentsDirectory;
	}

	// util
	private HttpURLConnection prepareRequest(String urlString, String method) throws Exception {
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "application/json");
		return connection;
	}
	
	private String readJSONString(HttpURLConnection connection) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String jsonString = "";
		String line = null;
		while((line = in.readLine()) != null)
			jsonString += line;
		in.close();
		return jsonString;
	}
	
	/*
	private JSONObject jsonObjectRequest(String urlString) {
		JSONObject response = null;
		try {
			HttpURLConnection connection = prepareRequest(urlString, "GET");
			String jsonString = readJSONString(connection);
			response = new JSONObject(jsonString);
			connection.disconnect();
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		
		return response;
	}
	*/
	
	private JSONArray jsonArrayRequest(String urlString) {
		JSONArray response = null;
		try {
			HttpURLConnection connection = prepareRequest(urlString, "GET");
			String jsonString = readJSONString(connection);
			response = new JSONArray(jsonString);
			connection.disconnect();
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return response;
	}
	
	private int sendRequest(String urlString, String method, Object content) {
		int status = -1;
		try {
			HttpURLConnection connection = prepareRequest(urlString, method);
			ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String jsonObjectAsString = mapper.writeValueAsString(content);
			OutputStream out = connection.getOutputStream();
			out.write(jsonObjectAsString.getBytes());
			out.flush();
			status = connection.getResponseCode();
			
			out.close();
			connection.disconnect();
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return status;
	}
	
	// users
	private boolean userManipulationAction(User user, String additionalURL, String method, int expectedStatusCode) {
		boolean success = false;
		try {
			int statusCode = sendRequest(DatabaseURL + additionalURL, method, user);
			if(statusCode == expectedStatusCode)
				success = true;
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return success;
	}
	public boolean addUser(User user) {
		return userManipulationAction(user, "", "POST", HttpURLConnection.HTTP_CREATED);
	}
	
	public boolean changeUserPassword(User user) {
		return userManipulationAction(user, "/" + user.getUsername(), "PUT", HttpURLConnection.HTTP_OK);
	}
	
	public boolean deleteUser(User user) {
		return userManipulationAction(user, "/" + user.getUsername(), "DELETE", HttpURLConnection.HTTP_OK);
	}
	
	public List<String> getAllUsers() {
		List<String> users = new ArrayList<>();
		JSONArray response = jsonArrayRequest(DatabaseURL);
		try {
			for(int i = 0; i < response.length(); ++i) {
				users.add(response.getString(i));
			}
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return users;
	}
	
	// warrants
	public List<Passenger> getOnWarrantDetectedPassengers() {
		List<Passenger> passengers = new ArrayList<>();
		JSONArray response = jsonArrayRequest(WarrantsURL);
		if(response != null) {
			for(int i = 0; i < response.length(); ++i) {
				JSONObject passenger = response.getJSONObject(i);
				Passenger p = new Passenger(passenger.getString("id"), passenger.getString("accessedTime"));
				passengers.add(p);
			}
		}
		return passengers;
	}
	
	// file server
	public boolean downloadCustomsDocuments(String id) {
		boolean downloaded = false;
		try {
			String url = FileServerURL + "/" + id;
			JSONArray response = jsonArrayRequest(url);
			for(int i = 0; i < response.length(); ++i) {
				FileOutputStream out = new FileOutputStream(CustomsDocumentsDirectory + File.separator + id + "-" + System.currentTimeMillis() + ".zip");
				ObjectMapper mapper = new ObjectMapper();
				byte[] byteArray = mapper.readValue(response.get(i).toString(), byte[].class);
				out.write(byteArray);
				out.flush();
				out.close();
				downloaded = true;
			}
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return downloaded;
	}
}
