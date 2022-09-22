package tesi.application.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import tesi.application.ClientApplication;
import tesi.model.Terminal;
import tesi.util.GUIUtility;
import tesi.util.SOAPUtility;

public class StartPageController extends InitializableController {
	
	@FXML private TextField terminalNameTextField;
	@FXML private TextField entryIdTextField;
	@FXML private RadioButton policeControlRadioButton;
	@FXML private RadioButton customsControlRadioButton;
	
	@FXML private ToggleGroup controlTypeGroup;
    
    @FXML
    void submit(ActionEvent event) {
    	String terminalName = terminalNameTextField.getText();
    	int entryId = -1;
    	try {
    		entryId = Integer.parseInt(entryIdTextField.getText());
    		if(entryId < 0)
    			throw new IllegalArgumentException("Greska!\nBroj ulaza/izlaza ne moze da bude negativan broj.");
    		if(terminalName.isEmpty())
    			throw new IllegalArgumentException("Greska!\nUnesite naziv terminala.");
    	
    		boolean policeControl = controlTypeGroup.getSelectedToggle().equals(policeControlRadioButton);
    		Terminal terminal = SOAPUtility.getInstance().login(terminalName, entryId, policeControl);
    		if(terminal != null) {
    			LoginPageController controller = new LoginPageController(stage, terminal);
    			controller.terminal = terminal;
    			controller.show();
    		} else {
    			GUIUtility.showAlert(AlertType.ERROR, "Neuspjesna prijava.\nPokusajte ponovo.");
    		}
    	} catch (IllegalArgumentException e) {
    		ClientApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
    		GUIUtility.showAlert(AlertType.ERROR, e.getMessage());
    	} catch (Exception e) {
    		ClientApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
    		GUIUtility.showAlert(AlertType.ERROR, "Greska!\nNiste unijeli ispravne parametre.");
    	}
    }

    public StartPageController(Stage stage) {
    	super(stage, "StartPage.fxml","Klijentska aplikacija");
    }

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		// Do something ...
	}

}
