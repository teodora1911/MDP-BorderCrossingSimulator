package tesi.application.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import tesi.application.ClientApplication;
import tesi.chat.client.ChatClient;
import tesi.chat.model.Message;
import tesi.chat.model.MessageType;
import tesi.model.User;
import tesi.rmi.fileserver.model.Passenger;
import tesi.util.GUIUtility;
import tesi.util.RESTUtility;
import tesi.util.SOAPUtility;
import tesi.util.controls.CustomsControlThread;
import tesi.util.controls.PoliceControlThread;

public class ClientMainPageController extends InitializableController {
	
	private static final String RED_BACKGROUND = "-fx-background-color: #a01d26";
	private static final String BLUE_BACKGROUND = "-fx-background-color: #20232a";
	
	// side buttons
	@FXML private Button messagesButton;
	@FXML private Button passengersButton;
	@FXML private Button passwordChangeButton;
	@FXML private Button logoutButton;
	
	// panes
	@FXML private AnchorPane messagePane;
	@FXML private AnchorPane passengersPane;
	@FXML private AnchorPane passwordPane;
	
	// chat
	private ChatClient chatThread;
	
	@FXML private TableView<Message> messagesTableView;
	@FXML private TableColumn<Message, String> senderMessageColumn;
	@FXML private TableColumn<Message, String> messageTimeColumn;
	@FXML private Label messageContentLabel;
	
	@FXML
	void showMessageContent(ActionEvent event) {
		Message selectedMessage = messagesTableView.getSelectionModel().getSelectedItem();
		if(selectedMessage != null)
			messageContentLabel.setText(selectedMessage.getContent());
	}
	
	@FXML private ComboBox<MessageType> messageTypeComboBox;
    @FXML private TextField terminalTextField;
    @FXML private TextField entryTextField;
    @FXML private ToggleGroup controlTypeGroup;
    @FXML private RadioButton customsRadioButton;
    @FXML private RadioButton policeRadioButton;
    @FXML private TextField messageContentTextField;
	
	@FXML
	void sendMessage(ActionEvent event) {
		MessageType type = messageTypeComboBox.getSelectionModel().getSelectedItem();
		String content = messageContentTextField.getText();
		if(type != null && !content.isEmpty()) {
			String terminal;
			int entry;
			boolean police;
			switch(type) {
				case BROADCAST:
					chatThread.sendBroadcastMessage(content);
					break;
				case MULTICAST:
					terminal = terminalTextField.getText();
					if(!terminal.isEmpty())
						chatThread.sendMulticastMessage(terminal, content);
					break;
				case UNICAST:
					terminal = terminalTextField.getText();
					try {
						entry = Integer.parseInt(entryTextField.getText());
						police = controlTypeGroup.getSelectedToggle().equals(policeRadioButton);
						chatThread.sendUnicastMessage(terminal, entry, police, content);
					} catch (Exception e) {
						ClientApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
					}
					break;
			}
			terminalTextField.clear();
			entryTextField.clear();
			messageContentTextField.clear();
		} else {
			GUIUtility.showAlert(AlertType.ERROR, "Niste unijeli ispravne podatke.");
		}
	}
	
	// passengers
	@FXML private TableView<Passenger> passengersTableView;
    @FXML private TableColumn<Passenger, String> passengerColumn;
    @FXML private TableColumn<Passenger, String> timeColumn;
    
 	@FXML private PasswordField currentPasswordField;
 	@FXML private PasswordField newPasswordField;
 	@FXML private PasswordField newConfirmationPasswordField;

    @FXML
    void changePassword(ActionEvent event) {
    	String currentPassword = currentPasswordField.getText();
    	String newPassword = newPasswordField.getText();
    	String newConfirmationPassword = newConfirmationPasswordField.getText();
    	
    	if(user.getPassword().equals(currentPassword) && !newPassword.equals(currentPassword) && newPassword.equals(newConfirmationPassword)) {
    		user.setNewPassword(newPassword);
    		if(RESTUtility.getInstance().changePassword(user)) {
    			user.setPassword(newPassword);
    			user.setNewPassword(null);
    			GUIUtility.showAlert(AlertType.CONFIRMATION, "Uspjesno ste promijenili lozinku.");
    		} else {
    			GUIUtility.showAlert(AlertType.ERROR, "Greska.\nNeuspjesna promjena lozinke.");
    		}
    	} else {
    		GUIUtility.showAlert(AlertType.ERROR, "Greska.\nNeuspjesna promjena lozinke.");
    	}
    }
    
	@FXML private Button terminalContinuationButton;
	
	@FXML
	void continuationOfTerminal(ActionEvent event) {
		terminalContinuationButton.setVisible(false);
		chatThread.notifyContinuation();
		temporaryStopTerminal(false);
	}
	
    private void logout() {
    	SOAPUtility.getInstance().logout(user.getTerminal());
    	chatThread.logout();
    }
    
    @FXML
    void logout(ActionEvent event) {
    	logout();
    	stage.close();
    }

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		messagesButton.setOnAction(e -> {
			setPane(AnchorType.MESSAGES);
		});
		passengersButton.setOnAction(e -> {
			List<Passenger> passengers = SOAPUtility.getInstance().getPassengers();
			passengersTableView.getItems().setAll(passengers);
			setPane(AnchorType.PASSENGERS);
		});
		passwordChangeButton.setOnAction(e -> setPane(AnchorType.CHANGE_PASSWORD));
		
		senderMessageColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> column) {
                return new SimpleObjectProperty<String>(column.getValue().getSender());
            }
        });
		messageTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Message, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Message, String> column) {
                return new SimpleObjectProperty<String>(column.getValue().getTimestamp());
            }
        });
		messageTypeComboBox.getItems().setAll(MessageType.values());
		terminalContinuationButton.setVisible(false);
		
		passengerColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Passenger, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Passenger, String> column) {
                return new SimpleObjectProperty<String>(column.getValue().getId());
            }
        });
		timeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Passenger, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Passenger, String> column) {
                return new SimpleObjectProperty<String>(column.getValue().getAccessedTime());
            }
        });
		
		messagesTableView.setStyle("-fx-font-family: 'Arial Narrow';" + " -fx-font-size: 15;" + "-fx-text-fill: #20232a;");
		passengersTableView.setStyle("-fx-font-family: 'Arial Narrow';" + " -fx-font-size: 15;" + "-fx-text-fill: #20232a;");
		
		stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> ClientMainPageController.this.logout());
	}
	
	public User user;
	private boolean stopped = false;
	private Object lock = new Object();
	
	public ClientMainPageController(Stage stage, User user) {
		super(stage, "ClientMainPage.fxml", "Klijentska aplikacija");
		this.user = user;
	}
	
	public Boolean isStopped() {
		return Boolean.valueOf(stopped);
	}
	
	public void temporaryStopTerminal(Boolean value) {
		synchronized(lock) {
			stopped = value.booleanValue();
			if(!stopped) {
				lock.notifyAll();
				SOAPUtility.getInstance().releaseTerminal(user.getTerminal().getName());
			} else {
				SOAPUtility.getInstance().stopTerminal(user.getTerminal().getName());
			}
		}
	}
	
	public void showMessage(String message) {
		Platform.runLater(() -> {
			GUIUtility.showAlert(AlertType.INFORMATION, message);
		});
	}
	
	@Override
	public void show() {
		Consumer<String> policeControlThreadNotificationFunc = message -> {
			temporaryStopTerminal(true);
			chatThread.notifyTemporaryStop();
			showMessage(message);
			terminalContinuationButton.setVisible(true);
		};
		
		Consumer<List<Message>> chatMessagesFunc = messages -> {
			Platform.runLater(() -> messagesTableView.getItems().setAll(messages));
		};
		
		try {
			chatThread = new ChatClient(user, this::temporaryStopTerminal, chatMessagesFunc, this::showMessage);
		} catch (Exception e) {
			ClientApplication.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			stage.close();
		}
		
		if(user.getTerminal().isPolice())
			new PoliceControlThread(user.getTerminal(), this::isStopped, lock, policeControlThreadNotificationFunc).start();
		else
			new CustomsControlThread(user.getTerminal(), this::isStopped, lock, this::showMessage).start();
		super.show();
	}
	
	private void setPane(AnchorType type) {
		if(type != null) {
			switch (type) {
				case MESSAGES:
					messagesButton.setStyle(RED_BACKGROUND);
					messagePane.setVisible(true);
					passengersButton.setStyle(BLUE_BACKGROUND);
					passengersPane.setVisible(false);
					passwordChangeButton.setStyle(BLUE_BACKGROUND);
					passwordPane.setVisible(false);
					logoutButton.setStyle(BLUE_BACKGROUND);
					break;
				case PASSENGERS:
					messagesButton.setStyle(BLUE_BACKGROUND);
					messagePane.setVisible(false);
					passengersButton.setStyle(RED_BACKGROUND);
					passengersPane.setVisible(true);
					passwordChangeButton.setStyle(BLUE_BACKGROUND);
					passwordPane.setVisible(false);
					logoutButton.setStyle(BLUE_BACKGROUND);
					break;
				case CHANGE_PASSWORD:
					messagesButton.setStyle(BLUE_BACKGROUND);
					messagePane.setVisible(false);
					passengersButton.setStyle(BLUE_BACKGROUND);
					passengersPane.setVisible(false);
					passwordChangeButton.setStyle(RED_BACKGROUND);
					passwordPane.setVisible(true);
					logoutButton.setStyle(BLUE_BACKGROUND);
					break;
				case LOGOUT:
					messagesButton.setStyle(BLUE_BACKGROUND);
					messagePane.setVisible(false);
					passengersButton.setStyle(BLUE_BACKGROUND);
					passengersPane.setVisible(false);
					passwordChangeButton.setStyle(BLUE_BACKGROUND);
					passwordPane.setVisible(false);
					logoutButton.setStyle(RED_BACKGROUND);
				default:
					break;
			}
		}
	}
}

enum AnchorType {
	MESSAGES, 
	PASSENGERS,
	CHANGE_PASSWORD,
	LOGOUT;
}