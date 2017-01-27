package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.model.AccessToken;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the Splash Screen Scene.
 * 
 * @author Irina
 *
 */
public class SplashScreenScene {

	private MainApp mainApp;
	private AnchorPane rootView;

	/**
	 * Constructor.
	 * 
	 * @param mainApp
	 */
	public SplashScreenScene(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Starts Splash Screen scene by loading FXML.
	 */
	public void launchSplashScreen() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.SPLASH_SCREEN_VIEW));
			this.rootView = (AnchorPane) loader.load();

			Scene scene = new Scene(this.rootView);

			this.mainApp.getPrimaryStage().setScene(scene);
			this.mainApp.getPrimaryStage().setTitle(Globals.APP_NAME);
			this.mainApp.getPrimaryStage().setResizable(false);
			this.mainApp.getPrimaryStage().getIcons().add(new Image(Globals.RSS_LOGO_LINK));
			this.mainApp.getPrimaryStage().show();

			waitAndRedirect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Waits during splash screen and redirect to the good scene. If the user is
	 * already connected, the MainView is started. If not, the Connection View
	 * is started.
	 */
	private void waitAndRedirect() {
		/**
		 * Starts a task to wait some seconds.
		 */
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
					launchConnectionView();
				} else {
					AccessToken accessToken = new AccessToken();
					accessToken.setApiToken(apiToken);
					MainApp.getTokenRequestInterceptor().setAccessToken(accessToken);
					launchMainView();
				}
			}
		});
		new Thread(sleeper).start();
	}

	/**
	 * Launches the Connection View.
	 */
	private void launchConnectionView() {
		ConnectionScene scene = new ConnectionScene();
		scene.launchConnectionView();
	}

	/**
	 * Launches the Main View.
	 */
	private void launchMainView() {
		MainViewScene scene = new MainViewScene();
		scene.launchMainView();
	}
}
