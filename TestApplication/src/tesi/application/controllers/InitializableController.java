package tesi.application.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingFormatArgumentException;
import java.util.Properties;
import java.util.logging.Level;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tesi.application.TestApplication;
import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

public abstract class InitializableController implements Initializable {
	
	public static String FXMLSDirectory;
	
	protected Stage stage;
	protected String resourceFile;
	protected String title;
	
	static {
		try {
			loadConfigFile();
		} catch (LoadingConfigurationFileException e) {
			TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	private static void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream input = new FileInputStream(Utility.CONFIG_FILE)){
			Properties properties = new Properties();
			properties.load(input);
			
			FXMLSDirectory = properties.getProperty("gui-resources");
			if(FXMLSDirectory == null)
				throw new MissingFormatArgumentException("Directory for GUI resources is not defined.");
			FXMLSDirectory = Utility.PROJECT_PATH + FXMLSDirectory;
		} catch (Exception e) {
			TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			throw new LoadingConfigurationFileException(e);
		}
	}
	
	public InitializableController(Stage stage, String filename, String title) {
		this.stage = stage;
		this.resourceFile = FXMLSDirectory + File.separator + filename;
		this.title = title;
		
		try {
			URL url = new File(resourceFile).toURI().toURL();
			if(url != null) {
				FXMLLoader loader = new FXMLLoader(url);
				loader.setController(this);
				stage.setScene(new Scene(loader.load()));
				stage.setResizable(false);
				stage.setTitle(this.title);
			} else {
				throw new MalformedURLException();
			}
		} catch (MalformedURLException e) {
			TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		} catch (IOException e) {
			TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
		}
	}
	
	public void show() {
		stage.show();
	}
}
