package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.gson.Gson;
import com.rssaggregator.desktop.model.APIError;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.SignUpWrapper;
import com.rssaggregator.desktop.network.RestService;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.UiUtils;
import com.rssaggregator.desktop.view.ConnectionController;
import com.rssaggregator.desktop.view.SignUpController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
		Credentials credentials = new Credentials(userEmail, userPassword);

		// Starts Loading view.
		this.signUpController.showLoading();

		OkHttpClient client = MainApp.getOkHttpClient();
		if (client == null) {
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			client = builder.build();
		}

		Retrofit retrofit = MainApp.getRetrofit();
		if (retrofit == null) {
			System.out.println("Creates new Retrofit");
			retrofit = new Retrofit.Builder().baseUrl(Globals.API_SERVER_URL)
					.addConverterFactory(GsonConverterFactory.create()).client(client).build();
		}

		RestService restService = retrofit.create(RestService.class);
		restService.signUp(credentials).enqueue(new Callback<SignUpWrapper>() {

			@Override
			public void onFailure(Call<SignUpWrapper> call, Throwable e) {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						signUpController.stopLoading();
						UiUtils.showErrorDialog(MainApp.getStage(), "Error", "Error");
						System.out.println("Error OnFailure");
					}
				});
			}

			@Override
			public void onResponse(Call<SignUpWrapper> call, Response<SignUpWrapper> response) {
				// TODO Auto-generated method stub
				if (response.isSuccessful()) {
					System.out.println("Success API");
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							signUpController.stopLoading();
							SignUpWrapper wrapper = response.body();
							closeStage(userEmail, userPassword);
						}
					});
				} else {
					try {
						System.out.println("Error API");
						String json = response.errorBody().string();
						APIError error = new Gson().fromJson(json, APIError.class);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								signUpController.stopLoading();

								/**
								 * TEMPORARY
								 */
								// TODO: Delete this line
								closeStage(userEmail, userPassword);
								/**
								 * TEMPORARY
								 */
								// TODO Uncomment this line.
								// UiUtils.showErrorDialog(MainApp.getStage(),
								// "Error", error.getError());
							}
						});
						System.out.println(error.getError());
					} catch (IOException e) {
						e.printStackTrace();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								signUpController.stopLoading();
								UiUtils.showErrorDialog(MainApp.getStage(), "Error", "Error");
							}
						});
					}
				}
			}
		});

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
}
