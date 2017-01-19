package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.SignUpScene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

	private Stage stage;
	private SignUpScene scene;

	@FXML
	private TextField userEmailTf;
	@FXML
	private PasswordField userPasswordPf;
	@FXML
	private PasswordField userRetypePasswordPf;

	@FXML
	private void initialize() {
		userEmailTf.setPromptText("User Email");
		userPasswordPf.setPromptText("Password");
		userRetypePasswordPf.setPromptText("Password");
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setScene(SignUpScene scene) {
		this.scene = scene;
	}

	@FXML
	private void handleSignUp(ActionEvent event) {
		String userEmail = userEmailTf.getText();
		String userPassword = userPasswordPf.getText();
		String userRetypePassword = userRetypePasswordPf.getText();

		if (userEmail.length() == 0) {
			startErrorDialog(this.stage, "Email empty");
			return;
		}
		if (userPassword.length() == 0) {
			startErrorDialog(this.stage, "Password empty");
			return;
		}

		if (userRetypePassword.length() == 0) {
			startErrorDialog(this.stage, "Retype password empty");
			return;
		}

		if (!userPassword.equals(userRetypePassword)) {
			startErrorDialog(this.stage, "Not the same password");
			return;
		}

		this.scene.closeStage(userEmail, userPassword);
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
	private void handleExit(ActionEvent event) {
		System.exit(0);
	}
}
