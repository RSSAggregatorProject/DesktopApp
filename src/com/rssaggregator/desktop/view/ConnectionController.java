package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.ConnectionScene;
import com.rssaggregator.desktop.utils.PreferencesUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConnectionController {

	private ConnectionScene scene;

	@FXML
	private TextField userEmailTf;

	@FXML
	private PasswordField passwordTf;

	@FXML
	private void initialize() {
		PreferencesUtils.setUserEmail("");
		userEmailTf.setPromptText("User Email");
		passwordTf.setPromptText("Password");
	}

	public void setConnectionScene(ConnectionScene scene) {
		this.scene = scene;
	}

	@FXML
	private void handleConnection(ActionEvent event) {
		System.out.println("Connection");
		String userEmail = userEmailTf.getText();
		String password = passwordTf.getText();

		if (userEmail != null && userEmail.length() != 0 && password != null && password.length() != 0) {
			PreferencesUtils.setUserEmail(userEmail);
			scene.changeScene();
		} else {
			System.out.println("Error login");
		}
	}
}
