package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.view.ConnectionController;
import com.rssaggregator.desktop.view.SignUpController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Controller of the Sign Up Scene.
 * 
 * @author Irina
 *
 */
public class SignUpScene {

	private Stage primaryStage;
	private ConnectionController connectionController;
	private SignUpController signUpController;

	private BorderPane rootView;
	private Stage signUpStage;

	/**
	 * Constructor.
	 * 
	 * @param connectionController
	 */
	public SignUpScene(ConnectionController connectionController) {
		this.primaryStage = MainApp.getStage();
		this.connectionController = connectionController;
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

			this.signUpController = loader.getController();
			this.signUpController.setStage(this.signUpStage);
			this.signUpController.setScene(this);

			this.signUpStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Closes the Sign Up stage.
	 * 
	 * @param userEmail
	 * @param userPassword
	 */
	public void closeStage(String userEmail, String userPassword) {
		if (this.signUpStage.isShowing()) {
			this.signUpStage.close();
		}
		this.connectionController.updateFields(userEmail, userPassword);
	}
}
