package tesi.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.Level;

//import org.jdom2.Document;
//import org.jdom2.Element;
//import org.jdom2.input.SAXBuilder;
//import org.jdom2.output.Format;
//import org.jdom2.output.XMLOutputter;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import tesi.model.Passenger;
import tesi.model.Passengers;
import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

public class PassengerService {
	
	public static String PassengersFilePath;
	public static String WarrantFilePath;
	
	private static void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream input = new FileInputStream(Utility.CONFIG_FILE_PATH)){
			Properties properties = new Properties();
			properties.load(input);
			
			PassengersFilePath = properties.getProperty("passengers-file");
			if(PassengersFilePath == null)
				throw new MissingFormatArgumentException("Passengers txt file is not specified.");
			PassengersFilePath = Utility.PROJECT_ROOT + PassengersFilePath;
			WarrantFilePath = properties.getProperty("warrant-passengers-file");
			if(WarrantFilePath == null)
				throw new MissingFormatArgumentException("Warrant file is not specified.");
			WarrantFilePath = Utility.PROJECT_ROOT + WarrantFilePath;
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
	
	static {
		try {
			loadConfigFile();
		} catch (LoadingConfigurationFileException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public void passengerCrossed(String passengerID) {
		try(PrintWriter out = new PrintWriter(new FileOutputStream(PassengersFilePath, true))){
			Passenger passenger = new Passenger(passengerID, LocalDateTime.now().toString());
			out.println(passenger.toString());
			out.flush();
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public String[] getPassengers() {
		String[] retValue = null;
		try {
			List<String> passengers = Files.readAllLines(Paths.get(PassengersFilePath));
			retValue = passengers.toArray(new String[passengers.size()]);
		} catch (IOException e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return retValue;
	}
	
	public void passengerOnWarrantDetected(String passengerID) {
		try {
			Passengers passengers = getDetectedPassengers();
			passengers.getPassengers().add(new Passenger(passengerID, LocalDateTime.now().toString()));
			XmlMapper xmlMapper = new XmlMapper();
			String xmlString = xmlMapper.writeValueAsString(passengers);
			FileWriter out = new FileWriter(WarrantFilePath);
			out.write(xmlString);
			out.flush();
			out.close();
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	private Passengers getDetectedPassengers() throws Exception {
		String xmlContent = "";
		XmlMapper mapper = new XmlMapper();
		try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(WarrantFilePath)))){
			String line = null;
			while((line = in.readLine()) != null)
				xmlContent += line;
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
		return (xmlContent.isEmpty() ? new Passengers() : mapper.readValue(xmlContent, Passengers.class)); 
	}
}
