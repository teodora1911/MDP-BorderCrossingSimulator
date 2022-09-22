package tesi.application.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingFormatArgumentException;
import java.util.Properties;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tesi.util.Utility;
import tesi.util.exceptions.LoadingConfigurationFileException;

public abstract class InitializableController implements Initializable {
	
	public static final String CONFIG_FILEPATH = Utility.getResourcesPath(false) + File.separator + "config.properties";
	public static String FXMLSDirectory;
	
	protected Stage stage;
	protected String resourceFile;
	protected String title;
	
	static {
		try {
			loadConfigFile();
		} catch (LoadingConfigurationFileException exc) {
			// TODO: Logger
			exc.printStackTrace();
			throw new RuntimeException(exc);
		}
	}
	
	public InitializableController(Stage stage, String filename, String title) {
		this.stage = (stage == null) ? new Stage() : stage;
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
		} catch (MalformedURLException exc) {
			// TODO: Logger
			exc.printStackTrace();
		} catch (IOException exc) {
			// TODO: Logger
			exc.printStackTrace();
		}
	}
	
	public InitializableController(String filename, String title) {
		this(null, filename, title);
	}
	
	public void show() {
		stage.show();
	}
	
	private static void loadConfigFile() throws LoadingConfigurationFileException {
		try(FileInputStream input = new FileInputStream(CONFIG_FILEPATH)){
			Properties properties = new Properties();
			properties.load(input);
			
			FXMLSDirectory = properties.getProperty("gui-resources");
			if(FXMLSDirectory == null)
				throw new MissingFormatArgumentException("Directory for GUI resources is not defined.");
			FXMLSDirectory = Utility.getProjectRootPath(false) + FXMLSDirectory;
		} catch (Exception exc) {
			throw new LoadingConfigurationFileException(exc);
		}
	}
}
