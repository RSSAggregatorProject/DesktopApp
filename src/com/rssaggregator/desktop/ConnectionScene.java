package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;
import com.rssaggregator.desktop.view.ConnectionController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class ConnectionScene {

	private MainApp mainApp;
	private BorderPane rootView;

	public ConnectionScene(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void launchConnectionView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.CONNECTION_VIEW));
			this.rootView = (BorderPane) loader.load();

			Scene scene = new Scene(this.rootView);
			this.mainApp.getPrimaryStage().setScene(scene);

			ConnectionController controller = loader.getController();
			controller.setConnectionScene(this);

			this.mainApp.getPrimaryStage().show();

			resetUserPreferences();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void launchMainView() {
		MainViewScene scene = new MainViewScene(this.mainApp);
		scene.launchMainView();
	}

	private void resetUserPreferences() {
		PreferencesUtils.setUserEmail("");
		PreferencesUtils.setUserPassword("");
		PreferencesUtils.setApiToken("");
		PreferencesUtils.setIsConnected(false);
	}

}
