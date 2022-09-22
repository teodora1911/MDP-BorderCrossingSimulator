package tesi.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;

public final class GUIUtility {

	private static final int ALERT_WIDTH = 500;
	private static final int ALERT_HEIGHT = 100;

	private GUIUtility() { }
	
	public static void showAlert(AlertType type, String message) {
		Alert alert = new Alert(type, message);
		alert.setWidth(ALERT_WIDTH);
		alert.setHeight(ALERT_HEIGHT);
		alert.initStyle(StageStyle.TRANSPARENT);
		alert.show();
	}
}
