package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.ConnectionScene;
import com.rssaggregator.desktop.MainApp;

import javafx.fxml.FXML;

public class MainViewRootController {

	@FXML
	private void initialize() {
	}

	@FXML
	private void handleLogOut() {
		ConnectionScene scene = new ConnectionScene(MainApp.getMainApp());
		scene.launchConnectionView();
	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
