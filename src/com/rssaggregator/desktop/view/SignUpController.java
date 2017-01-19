package com.rssaggregator.desktop.view;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.rssaggregator.desktop.SignUpScene;
import com.rssaggregator.desktop.utils.UiUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controller View for the Sign Up View.
 * 
 * @author Irina
 *
 */
public class SignUpController {

	private Stage signUpStage;
	private SignUpScene scene;

	@FXML
	private JFXTextField userEmailTf;
	@FXML
	private JFXPasswordField userPasswordPf;
	@FXML
	private JFXPasswordField userRetypePasswordPf;

	@FXML
	private void initialize() {
	}

	public void setStage(Stage stage) {
		this.signUpStage = stage;
	}

	public void setScene(SignUpScene scene) {
		this.scene = scene;
	}

	/**
	 * Handles the Sign Up Action. Signs Up the user.
	 * 
	 * @param event
	 */
	@FXML
	private void handleSignUp(ActionEvent event) {
		String userEmail = userEmailTf.getText();
		String userPassword = userPasswordPf.getText();
		String userRetypePassword = userRetypePasswordPf.getText();

		if (userEmail.length() == 0) {
			UiUtils.showErrorDialog(this.signUpStage, "Invalid Inputs", "The Email field is empty!");
			return;
		}
		if (userPassword.length() == 0) {
			UiUtils.showErrorDialog(this.signUpStage, "Invalid Inputs", "The Password field is empty!");
			return;
		}

		if (userRetypePassword.length() == 0) {
			UiUtils.showErrorDialog(this.signUpStage, "Invalid Inputs", "The Retype Password field is empty!");
			return;
		}

		if (!userPassword.equals(userRetypePassword)) {
			UiUtils.showErrorDialog(this.signUpStage, "Invalid Inputs",
					"The Password and the Retype Password fields are not the same.");
			return;
		}

		this.scene.closeStage(userEmail, userPassword);
	}

	/**
	 * Cancels the registration.
	 */
	@FXML
	private void handleCancelSignUp() {
		if (this.signUpStage.isShowing()) {
			this.signUpStage.close();
		}
	}

	/**
	 * Closes the application.
	 * 
	 * @param event
	 */
	@FXML
	private void handleExit(ActionEvent event) {
		System.exit(0);
	}
}
