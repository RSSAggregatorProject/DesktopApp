package com.rssaggregator.desktop.utils;

import com.rssaggregator.desktop.MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
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

	public static Stage createLoadingDialog(Stage owner) {
		Stage stage = null;

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.DIALOG_LOADING_VIEW));
			AnchorPane pane = (AnchorPane) loader.load();

			Scene scene = new Scene(pane);
			stage = new Stage();
			stage.setResizable(false);
			stage.initOwner(owner);
			stage.centerOnScreen();
			stage.setScene(scene);
			return stage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
