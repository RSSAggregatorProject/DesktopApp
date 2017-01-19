package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;

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
			this.mainApp.getPrimaryStage().setResizable(false);
			this.mainApp.getPrimaryStage().getIcons().add(new Image("file:resources/images/icon_rss.png"));
			this.mainApp.getPrimaryStage().show();

			waitAndRedirect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void waitAndRedirect() {
		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep(Globals.SPLASH_SCREEN_TIME);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				String apiToken = PreferencesUtils.getApiToken();
				if (apiToken == null || apiToken.length() == 0) {
					launchConnectionScene();
				} else {
					launchMainScene();
				}
			}
		});
		new Thread(sleeper).start();
	}

	private void launchConnectionScene() {
		ConnectionScene scene = new ConnectionScene(this.mainApp);
		scene.launchConnectionView();
	}

	private void launchMainScene() {
		MainViewScene scene = new MainViewScene(this.mainApp);
		scene.launchMainView();
	}
}
