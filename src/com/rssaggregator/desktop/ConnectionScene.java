package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.view.ConnectionController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class ConnectionScene {

	private MainApp mainApp;
	private AnchorPane splashScreenRootView;

	public ConnectionScene(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void startConnectionScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Layout_Connection.fxml"));
			this.splashScreenRootView = (AnchorPane) loader.load();

			Scene scene = new Scene(this.splashScreenRootView);
			this.mainApp.getPrimaryStage().setScene(scene);

			ConnectionController controller = loader.getController();
			controller.setConnectionScene(this);

			this.mainApp.getPrimaryStage().show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changeScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Layout_MainView.fxml"));
			this.splashScreenRootView = (AnchorPane) loader.load();

			Scene scene = new Scene(this.splashScreenRootView);
			this.mainApp.getPrimaryStage().setScene(scene);

			this.mainApp.getPrimaryStage().show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
