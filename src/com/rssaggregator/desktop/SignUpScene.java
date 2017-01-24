package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rssaggregator.desktop.network.RssApi;
import com.rssaggregator.desktop.network.event.SignUpEvent;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.UiUtils;
import com.rssaggregator.desktop.view.ConnectionController;
import com.rssaggregator.desktop.view.SignUpController;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controller of the Sign Up Scene.
 * 
 * @author Irina
 *
 */
public class SignUpScene {

	// View attributes.
	private BorderPane rootView;
	private Stage signUpStage;
	private Stage primaryStage;

	// Controllers.
	private ConnectionController connectionController;
	private SignUpController signUpController;

	// Network
	private EventBus eventBus;
	private RssApi rssApi;

	// Scene
	private SignUpScene instance;

	// User variables.
	private static String userEmail;
	private static String userPassword;

	/**
	 * Constructor.
	 * 
	 * @param connectionController
	 */
	public SignUpScene(ConnectionController connectionController) {
		this.primaryStage = MainApp.getStage();
		this.connectionController = connectionController;

		this.instance = this;

		this.rssApi = MainApp.getRssApi();
		this.eventBus = MainApp.getEventBus();

		// Check if the EventBus attribute is initialized.
		if (this.eventBus == null) {
			System.out.println("Event Bus Null");
			this.eventBus = new EventBus();
			this.rssApi.setEventBus(this.eventBus);
		}

		this.eventBus.register(this.instance);
	}

	/**
	 * Launches the Sign Up View loading the FXML.
	 */
	public void launchSignUpView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.SIGNUP_VIEW));
			this.rootView = (BorderPane) loader.load();

			this.signUpStage = new Stage();
			this.signUpStage.initOwner(this.primaryStage);

			Scene scene = new Scene(this.rootView);
			this.signUpStage.setTitle(Globals.APP_NAME);
			this.signUpStage.setResizable(false);
			this.signUpStage.getIcons().add(new Image(Globals.RSS_LOGO_LINK));
			this.signUpStage.setScene(scene);

			this.signUpStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					eventBus.unregister(instance);
				}
			});

			this.signUpController = loader.getController();
			this.signUpController.setStage(this.signUpStage);
			this.signUpController.setSignUpScene(this);

			this.signUpStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Signs up the user to the database of the API server.
	 * 
	 * @param userEmail
	 *            email of the user
	 * @param userPassword
	 *            password of the user.
	 */
	public void signUp(String userEmail, String userPassword) {
		// Starts Loading View
		this.signUpController.showLoading();

		SignUpScene.userEmail = userEmail;
		SignUpScene.userPassword = userPassword;

		this.rssApi.signUp(userEmail, userPassword);
	}

	/**
	 * Closes the Sign Up stage and updates the fields in the Connection View.
	 * 
	 * @param userEmail
	 * @param userPassword
	 */
	private void closeStage(String userEmail, String userPassword) {
		if (this.signUpStage != null && this.signUpStage.isShowing()) {
			this.signUpStage.close();
		}
		this.connectionController.updateFields(userEmail, userPassword);
	}

	public void closeStage() {
		this.eventBus.unregister(this.instance);
		if (this.signUpStage != null && this.signUpStage.isShowing()) {
			this.signUpStage.close();
		}
	}

	//
	//
	// Event methods.
	//
	//
	/**
	 * Event after the api returns the result of the signUp request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleSignUp(SignUpEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				signUpController.stopLoading();
			}
		});

		if (event.isSuccess()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					System.out.println("Close the door");
					eventBus.unregister(instance);
					closeStage(userEmail, userPassword);
				}
			});
		} else {
			String errorMessage;
			if (event.getThrowable().getMessage() != null) {
				errorMessage = event.getThrowable().getMessage();
			} else {
				errorMessage = "Error";
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					UiUtils.showErrorDialog(signUpStage, "Error", errorMessage);
				}
			});
		}
	}
}
