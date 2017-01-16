package com.rssaggregator.desktop;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		SplashScreenScene scene = new SplashScreenScene(this);
		scene.launchSplashScreen();
	}

	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
