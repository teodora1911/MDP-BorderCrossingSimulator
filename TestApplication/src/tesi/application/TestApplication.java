package tesi.application;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import tesi.application.controllers.StartPageController;
import tesi.util.Utility;

public class TestApplication extends Application {
	
	private static FileHandler handler;
	public static Logger logger = Logger.getLogger(TestApplication.class.getName());
	
	static {
		Utility.setLog(logger, handler, "TestApp.log");
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		StartPageController controller = new StartPageController(stage);
		controller.show();
	}
}
