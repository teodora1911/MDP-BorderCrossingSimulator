package tesi.application;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import tesi.application.controllers.StartPageController;
import tesi.util.Utility;

public class ClientApplication extends Application {
	
	private static FileHandler handler;
	public static Logger logger = Logger.getLogger(ClientApplication.class.getName());
	
	static {
		Utility.setLog(logger, handler, "ClientApp.log");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		StartPageController controller = new StartPageController(stage);
		controller.show();
	}
}
