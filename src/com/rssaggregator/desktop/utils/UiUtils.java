package com.rssaggregator.desktop.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class UiUtils {

	public static void showErrorDialog(Stage owner, String title, String errorMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(owner);
		alert.setTitle(title);
		alert.setHeaderText("Error");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

}
