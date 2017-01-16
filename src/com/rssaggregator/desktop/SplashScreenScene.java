package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;
import com.rssaggregator.desktop.view.ConnectionController;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class SplashScreenScene {

	private MainApp mainApp;
	private AnchorPane rootView;

	public SplashScreenScene(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void launchSplashScreen() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.SPLASH_SCREEN_VIEW));
			this.rootView = (AnchorPane) loader.load();

			Scene scene = new Scene(this.rootView);
			this.mainApp.getPrimaryStage().setScene(scene);
			this.mainApp.getPrimaryStage().setTitle(Globals.APP_NAME);
			this.mainApp.getPrimaryStage().getIcons().add(new Image("file:resources/images/icon_rss.png"));
			this.mainApp.getPrimaryStage().show();

			if (PreferencesUtils.getUserEmail() == null || PreferencesUtils.getUserEmail().length() == 0) {
				System.out.println("VIDE");
			} else {
				System.out.println(PreferencesUtils.getUserEmail());
			}
			switchScene();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void switchScene() {
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				changeScene();
			}
		});
		new Thread(sleeper).start();
	}

	public void changeScene() {
		ConnectionScene scene = new ConnectionScene(this.mainApp);
		scene.startConnectionScene();
	}
}
