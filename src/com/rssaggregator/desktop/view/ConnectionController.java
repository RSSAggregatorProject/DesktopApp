package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.ConnectionScene;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.utils.PreferencesUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectionController {

	private ConnectionScene scene;

	@FXML
	private TextField userEmailTf;
	@FXML
	private PasswordField userPasswordPf;

	@FXML
	private void initialize() {
		userEmailTf.setPromptText("User Email");
		userPasswordPf.setPromptText("Password");
	}

	public void setConnectionScene(ConnectionScene scene) {
		this.scene = scene;
	}

	@FXML
	private void handleLogIn(ActionEvent event) {
		String userEmail = userEmailTf.getText();
		String userPassword = userPasswordPf.getText();

		if (userEmail.length() == 0) {
			startErrorDialog(MainApp.getMainApp().getPrimaryStage(), "Email empty");
			return;
		}
		if (userPassword.length() == 0) {
			startErrorDialog(MainApp.getMainApp().getPrimaryStage(), "Password empty");
			return;
		}

		PreferencesUtils.setUserEmail(userEmail);
		PreferencesUtils.setUserPassword(userPassword);
		PreferencesUtils.setApiToken("API TOKEN");

		this.scene.launchMainView();
	}

	private void startErrorDialog(Stage stage, String errorMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(stage);
		alert.setTitle("Invalid Fields");
		alert.setHeaderText("Please correct invalid fields");
		alert.setContentText(errorMessage);

		alert.showAndWait();
	}

	@FXML
	private void handleSignUp(ActionEvent event) {
		System.out.println("Start Inscription screen");
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
