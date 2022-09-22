package tesi.application.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.awt.*;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
//import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import tesi.application.AdministratorApplication;
import tesi.model.Passenger;
import tesi.model.Terminal;
import tesi.model.User;
import tesi.util.GUIUtility;
import tesi.util.RESTUtility;
import tesi.util.SOAPUtility;

public class AdministratorMainPageController extends InitializableController {
	
	private static final String RED_BACKGROUND = "-fx-background-color: #a01d26";
	private static final String BLUE_BACKGROUND = "-fx-background-color: #20232a";
	
	// panes
	@FXML private AnchorPane terminalsPane;
	@FXML private AnchorPane userPane;
	@FXML private AnchorPane warrantPassengersPane;
	@FXML private AnchorPane passengersPane;
	
	// buttons
	@FXML private Button terminalsButton;
	@FXML private Button userButton;
	@FXML private Button warrantPassengersButton;
	@FXML private Button passengersButton;
    
    // passengers on warrant
    @FXML private ListView<Passenger> warrantPassengersListView;
    
	// terminals
	@FXML private TextField searchTerminalTextField;
    @FXML private Label terminalIDLabel;
    @FXML private Label entryNumberLabel;
    @FXML private Label exitNumberLabel;
    
    private Terminal currentTerminal;

    @FXML
    void searchTerminal(ActionEvent event) {
    	String terminalName = searchTerminalTextField.getText();
    	currentTerminal = SOAPUtility.getInstance().getTerminal(terminalName);
    	if(currentTerminal != null) {
    		terminalIDLabel.setText(String.valueOf(currentTerminal.getID()));
    		int in = (int) Arrays.stream(currentTerminal.getEntries()).filter(e -> e.isEntry()).count();
    		entryNumberLabel.setText(String.valueOf(in));
    		exitNumberLabel.setText(String.valueOf(currentTerminal.getEntries().length - in));
    	} else {
    		GUIUtility.showAlert(AlertType.ERROR, "Terminal nije dostupan.");
    	}
    }

    @FXML
    void addNewTerminal(ActionEvent event) {
    	TerminalDetailsFormController controller = new TerminalDetailsFormController();
    	controller.show();
    }
    
    @FXML
    void updateTerminal(ActionEvent event) {
    	if(currentTerminal != null) {
    		TerminalDetailsFormController controller = new TerminalDetailsFormController(currentTerminal);
    		controller.setTerminal(currentTerminal);
    		controller.show();
    	} else{
    		GUIUtility.showAlert(AlertType.ERROR, "Nije pretrazen nijedan terminal.");
    	}
    }
    
    @FXML
    void deleteTerminal(ActionEvent event) {
    	String terminalName = searchTerminalTextField.getText();
    	if(SOAPUtility.getInstance().deleteTerminal(terminalName)) {
    		GUIUtility.showAlert(AlertType.CONFIRMATION, "Terminal {" + terminalName + "} je uspjesno obrisan.");
    	} else {
    		GUIUtility.showAlert(AlertType.ERROR, "Neuspjesno brisanje terminala {" + terminalName + "}");
    	}
    }
    
    // users
    @FXML private ListView<String> usersListView;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private Label newPasswordLabel;
    @FXML private PasswordField newPasswordField;
    
    @FXML private Button changePasswordButton;
    
    @FXML
    void selectUpdate(ActionEvent event) {
    	String username = usersListView.getSelectionModel().getSelectedItem();
    	if(username != null) {
    		newPasswordLabel.setVisible(true);
        	newPasswordField.setVisible(true);
        	changePasswordButton.setVisible(true);
        	usernameTextField.setText(username);
    	}
    }

    @FXML
    void addNewUser(ActionEvent event) {
    	String username = usernameTextField.getText();
    	String password = passwordField.getText();
    	if(username == null || username.isEmpty()) {
    		GUIUtility.showAlert(AlertType.ERROR, "Niste unijeli sve kredencijale.");
    	} else {
    		if(RESTUtility.getInstance().addUser(new User(username, password, password)))
    			GUIUtility.showAlert(AlertType.CONFIRMATION, "Uspjesno ste dodali novog korisnika.");
    		else
    			GUIUtility.showAlert(AlertType.ERROR, "Neuspjesno dodavenje novog korisnika.\nPokusajte ponovo.");
    			
    	}
    }

    @FXML
    void updateUser(ActionEvent event) {
    	String username = usernameTextField.getText();
    	String password = passwordField.getText();
    	String newPassword = newPasswordField.getText();
    	
    	if(username == null || username.isEmpty()) {
    		GUIUtility.showAlert(AlertType.ERROR, "Niste unijeli sve kredencijale.");
    	} else {
    		if(RESTUtility.getInstance().changeUserPassword(new User(username, password, newPassword))) {
    			GUIUtility.showAlert(AlertType.CONFIRMATION, "Uspjesno ste azurirali lozinku korisnika {" + username + "}.");
    			newPasswordLabel.setVisible(false);
    			newPasswordField.setVisible(false);
    			changePasswordButton.setVisible(false);
    		}
    		else
    			GUIUtility.showAlert(AlertType.ERROR, "Neuspjesno azuriranje lozinke korisnika {" + username + "}.\nPokusajte ponovo.");
    	}
    }

    @FXML
    void deleteUser(ActionEvent event) {
//    	String username = usernameTextField.getText();
    	String username = usersListView.getSelectionModel().getSelectedItem();
    	if(username == null) {
    		GUIUtility.showAlert(AlertType.ERROR, "Niste unijeli sve kredencijale.");
    	} else {
    		if(RESTUtility.getInstance().deleteUser(new User(username, null, null)))
    			GUIUtility.showAlert(AlertType.CONFIRMATION, "Uspjesno ste obrisali korisnika {" + username + "}.");
    		else
    			GUIUtility.showAlert(AlertType.ERROR, "Neuspjesno brisanje korisnika {" + username + "}.\nPokusajte ponovo.");
    	}
    }

    // passengers crossed
    @FXML private TableView<Passenger> passengersTableView;
    @FXML private TableColumn<Passenger, String> passengerIdColumn;
    @FXML private TableColumn<Passenger, String> passengerCrossingTimeColumn;

    @FXML
    void downloadDocuments(ActionEvent event) {
    	Passenger selectedPassenger = passengersTableView.getSelectionModel().getSelectedItem();
    	if(selectedPassenger != null) {
    		if(RESTUtility.getInstance().downloadCustomsDocuments(selectedPassenger.getId())) {
    			try {
    				Desktop.getDesktop().open(new File(RESTUtility.getInstance().getCustomsDocumentsDirectory()));
    			} catch (IOException e) {
    				AdministratorApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
    			}
    		} else {
    			GUIUtility.showAlert(AlertType.ERROR, "Nije moguce preuzeti dokumente.\nPokusajte ponovo.");
    		}
    	}
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resource) {
    	terminalsButton.setOnAction(e -> setPane(AnchorType.TERMINALS));
    	
    	userButton.setOnAction(e -> {
    		usersListView.getItems().setAll(RESTUtility.getInstance().getAllUsers());
    		setPane(AnchorType.USERS);
    	});
    	
    	warrantPassengersButton.setOnAction(e -> {
    		setPane(AnchorType.WARRANT_PASSENGERS);
    		warrantPassengersListView.getItems().clear();
			warrantPassengersListView.getItems().setAll(FXCollections.observableArrayList(RESTUtility.getInstance().getOnWarrantDetectedPassengers()));
    	});
    	
    	passengerIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Passenger, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Passenger, String> column) {
                return new SimpleObjectProperty<String>(column.getValue().getId());
            }
        });
    	passengerCrossingTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Passenger, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Passenger, String> column) {
                return new SimpleObjectProperty<String>(column.getValue().getAccessedTime());
            }
        });
    	warrantPassengersListView.setStyle("-fx-font-family: 'Arial';" + " -fx-font-size: 15;" + "-fx-text-fill: #20232a;");
    	passengersTableView.setStyle("-fx-font-family: 'Arial';" + " -fx-font-size: 15;" + "-fx-text-fill: #20232a;");
    	usersListView.setStyle("-fx-font-family: 'Arial';" + " -fx-font-size: 15;" + "-fx-text-fill: #20232a;");
    	
    	newPasswordLabel.setVisible(false);
    	newPasswordField.setVisible(false);
    	
    	passengersButton.setOnAction(e -> {
    		setPane(AnchorType.PASSENGERS);
    		passengersTableView.getItems().setAll(SOAPUtility.getInstance().getPassengers());
    	});
    	changePasswordButton.setVisible(false);
    }
  
    public AdministratorMainPageController(Stage stage) {
    	super(stage, "AdministratorMainPage.fxml", "Administratorska aplikacija");
    }
    
    private void setPane(AnchorType type) {
    	if(type != null) {
    		switch (type) {
			case TERMINALS:
				terminalsButton.setStyle(BLUE_BACKGROUND);
				terminalsPane.setVisible(true);
				userButton.setStyle(RED_BACKGROUND);
				userPane.setVisible(false);
				warrantPassengersButton.setStyle(RED_BACKGROUND);
				warrantPassengersPane.setVisible(false);
				passengersButton.setStyle(RED_BACKGROUND);
                passengersPane.setVisible(false);
				break;
			case USERS:
				terminalsButton.setStyle(RED_BACKGROUND);
				terminalsPane.setVisible(false);
				userButton.setStyle(BLUE_BACKGROUND);
				userPane.setVisible(true);
				warrantPassengersButton.setStyle(RED_BACKGROUND);
				warrantPassengersPane.setVisible(false);
				passengersButton.setStyle(RED_BACKGROUND);
                passengersPane.setVisible(false);
				break;
			case WARRANT_PASSENGERS:
				terminalsButton.setStyle(RED_BACKGROUND);
				terminalsPane.setVisible(false);
				userButton.setStyle(RED_BACKGROUND);
				userPane.setVisible(false);
				warrantPassengersButton.setStyle(BLUE_BACKGROUND);
				warrantPassengersPane.setVisible(true);
				passengersButton.setStyle(RED_BACKGROUND);
                passengersPane.setVisible(false);
				break;
			case PASSENGERS:
				terminalsButton.setStyle(RED_BACKGROUND);
				terminalsPane.setVisible(false);
				userButton.setStyle(RED_BACKGROUND);
				userPane.setVisible(false);
				warrantPassengersButton.setStyle(RED_BACKGROUND);
				warrantPassengersPane.setVisible(false);
				passengersButton.setStyle(BLUE_BACKGROUND);
                passengersPane.setVisible(true);
			default:
				break;
		}
    	}
    }
}

enum AnchorType {
	TERMINALS,
	USERS,
	WARRANT_PASSENGERS,
	PASSENGERS;
}
