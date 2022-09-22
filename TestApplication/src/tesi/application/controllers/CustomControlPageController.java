package tesi.application.controllers;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tesi.application.TestApplication;
import tesi.util.GUIUtility;
import tesi.util.SOAPUtility;
import tesi.util.Utility;

public class CustomControlPageController extends InitializableController {
	
	@FXML private ListView<File> filesListView;
	
	private Set<File> selectedFiles = new HashSet<>();

    @FXML
    void submit(ActionEvent event) {
    	if(!selectedFiles.isEmpty()) {
    		SOAPUtility.getInstance().customsStop(terminalKey, passenger, selectedFiles);
    		int passed = 0;
    		while(passed == 0) {
    			passed = SOAPUtility.getInstance().passed(passenger);
    			try {
    				Thread.sleep(1000);
    			} catch (InterruptedException e) {
    				TestApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
    			}
    		}
    		
    		if(passed < 0) {
    			GUIUtility.showAlert(AlertType.ERROR, "Greska pri slanju dokumenata.");
    		} else {
    			GUIUtility.showAlert(AlertType.CONFIRMATION, "Uspjesno ste presli granicu.\nSrecan put.");
    		}
    		StartPageController controller = new StartPageController(stage);
    		controller.show();
    	} else {
    		GUIUtility.showAlert(AlertType.ERROR, "Morate da prilozite dokumente.");
    	}
    }

    @FXML
    void addFiles(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(Utility.PROJECT_PATH));
        List<File> picked = fileChooser.showOpenMultipleDialog(stage);
        selectedFiles.addAll(picked);
        filesListView.getItems().setAll(selectedFiles);
    }

    @FXML
    void removeSelectedFile(ActionEvent event) {
    	File selectedFile = filesListView.getSelectionModel().getSelectedItem();
    	if(selectedFile != null) {
    		selectedFiles.remove(selectedFile);
    		filesListView.getItems().setAll(selectedFiles);
    	}
    }
    
    public String terminalKey;
    public String passenger;

	public CustomControlPageController(Stage stage, String terminalKey, String passenger) {
		super(stage, "CustomControlPage.fxml", "Testna aplikacija");
		this.terminalKey = terminalKey;
		this.passenger = passenger;
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		filesListView.setStyle("-fx-font-family: 'Arial Narrow';" + " -fx-font-size: 12;" + "-fx-text-fill: #20232a;");
	}

}
