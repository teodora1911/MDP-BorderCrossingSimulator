package tesi.application;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import tesi.application.controllers.AdministratorMainPageController;
import tesi.util.Utility;

public class AdministratorApplication extends Application {
	
	private static FileHandler handler;
	public static Logger logger = Logger.getLogger(AdministratorApplication.class.getName());
	
	static {
		Utility.setLog(logger, handler, "AdministratorApp.log");
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		AdministratorMainPageController controller = new AdministratorMainPageController(stage);
		controller.show();
	}
}
