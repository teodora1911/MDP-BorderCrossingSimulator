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

public class StartPageController extends InitializableController {
	
	@FXML private TextField terminalNameTextField;
    @FXML private TextField entryIdTextField;
   
    @FXML
    void submit(ActionEvent event) {
    	String terminalName = terminalNameTextField.getText();
    	int entryId = -1;
    	try {
    		entryId = Integer.parseInt(entryIdTextField.getText());
    		if(entryId < 0)
    			throw new IllegalArgumentException("Broj ulaza/izlaza mora da bude pozitivan broj.");
    		if(terminalName.isEmpty())
    			throw new IllegalArgumentException("Niste unijeli naziv terminala.");
    		
    		String terminalKey = SOAPUtility.getInstance().checkAvailability(terminalName, entryId);
    		if(terminalKey != null) {
    			PoliceControlPageController controller = new PoliceControlPageController(stage, terminalKey);
    			controller.terminalKey = terminalKey;
    			controller.show();
    		} else {
    			GUIUtility.showAlert(AlertType.ERROR, "Trazeni terminal trenutno nije dostupan.\nPokusajte ponovo kasnije.");
    		}
    	} catch (IllegalArgumentException e) {
    		TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
    		GUIUtility.showAlert(AlertType.ERROR, e.getMessage());
    	} catch (Exception e) {
    		TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
    		GUIUtility.showAlert(AlertType.ERROR, "Greska.");
    	}
    }

	public StartPageController(Stage stage) {
		super(stage, "StartPage.fxml", "Testna aplikacija");
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		// Do something ...
	}
}
