package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.ConnectionScene;
import com.rssaggregator.desktop.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConnectionController {

	private ConnectionScene scene;
	private Stage stage;

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

		this.scene.logIn(userEmail, userPassword);
	}
	
	public void updateFields(String userEmail, String userPassword) {
		userEmailTf.setText(userEmail);
		userPasswordPf.setText(userPassword);
	}

	public void showLoading() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Dialog_Loading.fxml"));
			AnchorPane pane = (AnchorPane) loader.load();

			Scene scene = new Scene(pane);
			this.stage = new Stage();
			stage.setResizable(false);
			stage.initOwner(MainApp.getMainApp().getPrimaryStage());
			stage.centerOnScreen();
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopLoading() {
		if (this.stage.isShowing()) {
			this.stage.close();
		}
	}

	public void errorLogin(String errorMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(MainApp.getMainApp().getPrimaryStage());
		alert.setTitle("Error");
		alert.setContentText(errorMessage);

		alert.showAndWait();
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
		this.scene.launchSignUpView();
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
