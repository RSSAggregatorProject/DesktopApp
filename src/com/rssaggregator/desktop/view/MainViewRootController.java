package com.rssaggregator.desktop.view;

import com.google.common.eventbus.EventBus;
import com.rssaggregator.desktop.ConnectionScene;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.event.RefreshCategoriesEvent;
import com.rssaggregator.desktop.network.event.LogOutEvent;

import javafx.fxml.FXML;

/**
 * View Controller of the MainView Root.
 * 
 * @author Irina
 *
 */
public class MainViewRootController {

	@FXML
	private void initialize() {
	}

	/**
	 * Handles action when user logs out.
	 */
	@FXML
	private void handleLogOut() {
		EventBus eventBus = MainApp.getEventBus();
		eventBus.post(new LogOutEvent());

		ConnectionScene scene = new ConnectionScene();
		scene.launchConnectionView();
	}

	/**
	 * Handles action when user refreshs categories
	 */
	@FXML
	private void handleRefreshCategories() {
		EventBus eventBus = MainApp.getEventBus();
		eventBus.post(new RefreshCategoriesEvent());
	}

	/**
	 * Handles action when user closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}
}
