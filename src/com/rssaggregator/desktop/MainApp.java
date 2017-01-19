package com.rssaggregator.desktop;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main Class of the application. Launch the first window.
 * 
 * @author Irina
 *
 */
public class MainApp extends Application {

	private Stage primaryStage;

	private static MainApp instance;

	/**
	 * Starts the primary stage. Launches the Splash Screen Scene.
	 */
	@Override
	public void start(Stage primaryStage) {
		MainApp.instance = this;
		this.primaryStage = primaryStage;

		SplashScreenScene scene = new SplashScreenScene(this);
		scene.launchSplashScreen();
	}

	/**
	 * Gets the primary stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	public static Stage getStage() {
		return MainApp.instance.getPrimaryStage();
	}

	/**
	 * Gets the instance of the Main Class.
	 * 
	 * @return
	 */
	public static MainApp getMainApp() {
		return instance;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
