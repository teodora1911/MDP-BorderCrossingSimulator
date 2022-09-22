package tesi.application.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tesi.model.User;
import tesi.model.Terminal;
import tesi.util.RESTUtility;
import tesi.util.SOAPUtility;

public class LoginPageController extends InitializableController {
	
	@FXML private TextField usernameTextField;
	@FXML private PasswordField passwordField;
	@FXML private Label bannerTextLabel;
	
	public Terminal terminal;

    @FXML
    void submit(ActionEvent event) {
    	User user = new User(usernameTextField.getText(), passwordField.getText(), terminal);
    	if(RESTUtility.getInstance().login(user)) {
    		ClientMainPageController controller = new ClientMainPageController(this.stage, user);
    		controller.user = user;
    		controller.show();
    	} else {
    		bannerTextLabel.setVisible(true);
    	}
    }
    
    public LoginPageController(Stage stage, Terminal terminal) {
    	super(stage, "LoginPage.fxml", "Klijentska aplikacija");
    }

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		bannerTextLabel.setVisible(false);
		stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> SOAPUtility.getInstance().logout(terminal));
	}
}
