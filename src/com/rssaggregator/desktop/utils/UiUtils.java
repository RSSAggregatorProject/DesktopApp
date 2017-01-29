package com.rssaggregator.desktop.utils;

import com.rssaggregator.desktop.MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Utility class for UI methods.
 * 
 * @author Irina
 *
 */
public class UiUtils {

	/**
	 * Shows an error dialog.
	 * 
	 * @param owner
	 *            Owner stage
	 * @param title
	 *            title of the error
	 * @param header
	 *            header of the error
	 * @param errorMessage
	 *            message of the error
	 */
	public static void showErrorDialog(Stage owner, String title, String header, String errorMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(owner);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

	/**
	 * Creates a loading view.
	 * 
	 * @param owner
	 *            Owner stage
	 * 
	 * @return Stage, the loading view created.
	 */
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
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.centerOnScreen();
			stage.setScene(scene);
			return stage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
