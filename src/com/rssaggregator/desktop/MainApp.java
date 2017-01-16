package com.rssaggregator.desktop;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;

	private static MainApp instance;

	@Override
	public void start(Stage primaryStage) {
		MainApp.instance = this;
		this.primaryStage = primaryStage;

		SplashScreenScene scene = new SplashScreenScene(this);
		scene.launchSplashScreen();
	}

	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	public static MainApp getMainApp() {
		return instance;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
