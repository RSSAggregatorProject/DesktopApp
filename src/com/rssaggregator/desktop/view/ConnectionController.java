package com.rssaggregator.desktop.view;

import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.rssaggregator.desktop.ConnectionScene;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.model.TestEvent;
import com.rssaggregator.desktop.utils.UiUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Controller View for the Connection View.
 * 
 * @author Irina
 *
 */
public class ConnectionController {

	private static final String ERROR_TITLE = "Invalid inputs";

	private ConnectionScene scene;
	private Stage loadingStage;

	@FXML
	private JFXTextField userEmailTf;
	@FXML
	private JFXPasswordField userPasswordPf;
	@FXML
	private JFXButton loginBt;
	@FXML
	private JFXButton signupBt;

	@FXML
	private void initialize() {
	}

	/**
	 * Sets the Connection Scene.
	 * 
	 * @param scene
	 */
	public void setConnectionScene(ConnectionScene scene) {
		this.scene = scene;
	}

	/**
	 * Handles the Log In Action. Logs the user.
	 * 
	 * @param event
	 */
	@FXML
	private void handleLogIn(ActionEvent event) {
		String userEmail = userEmailTf.getText();
		String userPassword = userPasswordPf.getText();

		if (userEmail.length() == 0) {
			UiUtils.showErrorDialog(MainApp.getStage(), ERROR_TITLE, "The Email field is empty!");
			return;
		}
		if (userPassword.length() == 0) {
			UiUtils.showErrorDialog(MainApp.getStage(), ERROR_TITLE, "The Password field is empty!");
			return;
		}
		this.scene.logIn(userEmail, userPassword);
	}

	/**
	 * Handles the Sign Up Action. Starts the Sign Up View.
	 * 
	 * @param event
	 */
	@FXML
	private void handleSignUp(ActionEvent event) {
		this.scene.launchSignUpView();
	}

	/**
	 * Shows a loading dialog.
	 */
	public void showLoading() {
		this.loadingStage = null;
		this.loadingStage = UiUtils.createLoadingDialog(MainApp.getStage());
		if (this.loadingStage != null) {
			this.loadingStage.show();
		}
	}

	/**
	 * Stops the loading dialog.
	 */
	public void stopLoading() {
		if (this.loadingStage != null && this.loadingStage.isShowing()) {
			this.loadingStage.close();
		}
	}

	/**
	 * Updates the fields after the user signed up.
	 * 
	 * @param userEmail
	 * @param userPassword
	 */
	public void updateFields(String userEmail, String userPassword) {
		userEmailTf.setText(userEmail);
		userPasswordPf.setText(userPassword);
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
