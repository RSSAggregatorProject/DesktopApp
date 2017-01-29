package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rssaggregator.desktop.network.RssApi;
import com.rssaggregator.desktop.network.event.LogInEvent;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;
import com.rssaggregator.desktop.utils.UiUtils;
import com.rssaggregator.desktop.view.ConnectionController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Controller for the Connection Scene.
 * 
 * @author Irina
 *
 */
public class ConnectionScene {

	// View attributes
	private Stage primaryStage;
	private BorderPane rootView;

	// Others
	private ConnectionController controller;

	// Network
	private EventBus eventBus;
	private RssApi rssApi;

	// Scene
	private ConnectionScene instance;

	/**
	 * Default constructor.
	 * 
	 * @param mainApp
	 */
	public ConnectionScene() {
		this.primaryStage = MainApp.getStage();
		this.instance = this;

		this.rssApi = MainApp.getRssApi();
		this.eventBus = MainApp.getEventBus();

		// Check if the EventBus attribute is initialized.
		if (this.eventBus == null) {
			this.eventBus = new EventBus();
			this.rssApi.setEventBus(this.eventBus);
		}

		this.eventBus.register(this.instance);
	}

	/**
	 * Launches the Connection View by loading the FXML.
	 */
	public void launchConnectionView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.CONNECTION_VIEW));
			this.rootView = (BorderPane) loader.load();

			Scene scene = new Scene(this.rootView);
			this.primaryStage.setScene(scene);
			setStageSize();

			controller = loader.getController();
			controller.setConnectionScene(this);

			this.primaryStage.show();

			PreferencesUtils.resetUserPreferences();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the size of the stage for the connection view.
	 */
	private void setStageSize() {
		this.primaryStage.setMinWidth(800d);
		this.primaryStage.setMinHeight(640d);
		this.primaryStage.setWidth(800d);
		this.primaryStage.setHeight(640d);
		this.primaryStage.setResizable(false);
	}

	/**
	 * Logs the user to the application by requesting the API token.
	 * 
	 * @param userEmail
	 *            email of the user.
	 * @param userPassword
	 *            password of the user.
	 */
	public void logIn(String userEmail, String userPassword) {
		// Starts Loading view.
		this.controller.showLoading();

		this.rssApi.logIn(userEmail, userPassword);
	}

	/**
	 * Launches the Main View.
	 */
	public void launchMainView() {
		MainViewScene scene = new MainViewScene();
		scene.launchMainView();
	}

	/**
	 * Launches the Sign Up View.
	 */
	public void launchSignUpView() {
		SignUpScene scene = new SignUpScene(controller);
		scene.launchSignUpView();
	}

	//
	//
	// Event methods.
	//
	//
	/**
	 * Event after the api returns the result of the login request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleLogIn(LogInEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.stopLoading();
			}
		});

		if (event.isSuccess()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					eventBus.unregister(instance);
					launchMainView();
				}
			});
		} else {
			String errorMessage;
			if (event.getThrowable() != null && event.getThrowable().getMessage() != null) {
				errorMessage = event.getThrowable().getMessage();
			} else {
				errorMessage = "Error";
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_LOGIN_MESSAGE,
							Globals.ERROR_LOGIN_MESSAGE, errorMessage);
				}
			});
		}
	}
}
