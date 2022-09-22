package tesi.application.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tesi.application.TestApplication;
import tesi.util.GUIUtility;
import tesi.util.SOAPUtility;

public class PoliceControlPageController extends InitializableController {
	
	@FXML private TextField passengerTextField;
	
	@FXML
    void submit(ActionEvent event) {
		String passenger = passengerTextField.getText();
		if(!passenger.isEmpty()) {
			SOAPUtility.getInstance().policeStop(terminalKey, passenger);
			int passed = 0;
			while(passed == 0) {
				passed = SOAPUtility.getInstance().passed(passenger);
				if(passed < 0) {
					GUIUtility.showAlert(AlertType.INFORMATION, "Na potjernici ste.");
					break;
				} else if (passed > 0) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
				}
			}
			
			if(passed > 0) {
				CustomControlPageController controller = new CustomControlPageController(stage, terminalKey, passenger);
				controller.terminalKey = terminalKey;
				controller.passenger = passenger;
				controller.show();
			} else {
				StartPageController controller = new StartPageController(stage);
				controller.show();
			}
		} else {
			GUIUtility.showAlert(AlertType.ERROR, "Niste unijeli identifikator putnika.");
		}
    }
	
	public String terminalKey;

	public PoliceControlPageController(Stage stage, String terminalKey) {
		super(stage, "PoliceControlPage.fxml", "Testna aplikacija");
		this.terminalKey = terminalKey;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		// Do something ...
	}

}
