package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.view.ConnectionController;
import com.rssaggregator.desktop.view.SignUpController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SignUpScene {

	private MainApp mainApp;
	private ConnectionController connectionController;
	private SignUpController controller;
	private Stage stage;
	private BorderPane rootView;

	public SignUpScene(MainApp mainApp, ConnectionController connectionController) {
		this.mainApp = mainApp;
		this.connectionController = connectionController;
	}

	public void launchSignUpView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.SIGNUP_VIEW));
			this.rootView = (BorderPane) loader.load();

			stage = new Stage();
			stage.initOwner(this.mainApp.getPrimaryStage());

			Scene scene = new Scene(this.rootView);
			stage.setScene(scene);

			controller = loader.getController();
			controller.setStage(stage);
			controller.setScene(this);

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeStage(String userEmail, String userPassword) {
		this.stage.close();
		this.connectionController.updateFields(userEmail, userPassword);
	}
}
