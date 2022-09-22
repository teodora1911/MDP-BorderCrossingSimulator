package tesi.application.controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tesi.application.AdministratorApplication;
import tesi.model.Terminal;
import tesi.util.GUIUtility;
import tesi.util.SOAPUtility;

public class TerminalDetailsFormController extends InitializableController {
	
    @FXML private TextField exitNumberTextField;
    @FXML private TextField entryNumberTextField;
    @FXML private TextField terminalNameTextField;
    @FXML private Label bannerLabel;
    
    private Terminal terminal;

    @FXML
    void submit(ActionEvent event) {
    	try {
    		String terminalName = terminalNameTextField.getText();
    		int entries = Integer.parseInt(entryNumberTextField.getText());
			int exits = Integer.parseInt(exitNumberTextField.getText());
			
			if(entries <= 0 || exits <= 0)
				throw new IllegalArgumentException("Broj ulaza/izlaza mora da bude pozitivan broj.");
			
			if(terminal != null) { // azuriranje
				int in = (int) Arrays.stream(terminal.getEntries()).filter(e -> e.isEntry()).count();
				int out = terminal.getEntries().length - in;
				if(terminal.getName() == terminalName && in == entries && out == exits) {
					GUIUtility.showAlert(AlertType.ERROR, "Niste izmijenili podatke.");
				} else {
					SOAPUtility service = SOAPUtility.getInstance();
					terminal.setName(terminalName);
	    			boolean result = (entries == in && exits == out) ? service.updateTerminalName(terminal) : service.updateTerminalEntries(terminal.getID(), terminalName, entries, exits);
	    			if(result) {
	    				GUIUtility.showAlert(AlertType.CONFIRMATION, "Uspjesno ste azurirali terminal.");
	    				stage.close();
	    			} else {
	    				GUIUtility.showAlert(AlertType.ERROR, "Neuspjesno azuriranje terminala {" + terminalName + "}.\nPokusajte ponovo.");
	    			}
				}
				
	    	} else { // dodavanje
	    		if(SOAPUtility.getInstance().addTerminal(terminalName, entries, exits)) {
	    			GUIUtility.showAlert(AlertType.CONFIRMATION, "Uspjesno ste dodali terminal.");
	    			stage.close();
	    		} else {
	    			GUIUtility.showAlert(AlertType.ERROR, "Neuspjesno dodavenje novog terminala.\nPokusajte ponovo.");
	    		}
	    	}
    	} catch (IllegalArgumentException e) {
    		AdministratorApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			GUIUtility.showAlert(AlertType.ERROR, e.getMessage());
    	} catch (Exception e) {
    		AdministratorApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			GUIUtility.showAlert(AlertType.ERROR, "Greska!");
    	}
    }
    
    public TerminalDetailsFormController() {
    	super(new Stage(), "TerminalDetailsForm.fxml", "Administratorska aplikacija");
    }
    
    public TerminalDetailsFormController(Terminal terminal) {
    	this();
    	setTerminal(terminal);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resource) {
    	bannerLabel.setVisible(false);
    	setLabels();
    }
    
    private void setLabels() {
    	if(terminal != null) {
    		terminalNameTextField.setText(terminal.getName());
    		long in = Arrays.stream(terminal.getEntries()).filter(e -> e.isEntry()).count();
    		entryNumberTextField.setText(String.valueOf(in));
    		exitNumberTextField.setText(String.valueOf(terminal.getEntries().length - in));
    	}
    }
    
    public Terminal getTerminal() {
    	return terminal;
    }
    
    public void setTerminal(Terminal terminal) {
    	this.terminal = terminal;
    	setLabels();
    }
}
