package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.gson.Gson;
import com.rssaggregator.desktop.model.APIError;
import com.rssaggregator.desktop.model.ComeOn_Credentials;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.User;
import com.rssaggregator.desktop.network.RestService;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;
import com.rssaggregator.desktop.utils.UiUtils;
import com.rssaggregator.desktop.view.ConnectionController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Controller for the Connection Scene.
 * 
 * @author Irina
 *
 */
public class ConnectionScene {

	// View attributs
	private Stage primaryStage;
	private BorderPane rootView;

	// Others
	private ConnectionController controller;

	/**
	 * Default constructor.
	 * 
	 * @param mainApp
	 */
	public ConnectionScene() {
		this.primaryStage = MainApp.getStage();
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
		Credentials credentials = new Credentials(userEmail, userPassword);

		// Starts Loading view.
		this.controller.showLoading();

		OkHttpClient client = MainApp.getOkHttpClient();
		if (client == null) {
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			client = builder.build();
		}

		Retrofit retrofit = MainApp.getRetrofit();
		if (retrofit == null) {
			retrofit = new Retrofit.Builder().baseUrl(Globals.API_SERVER_URL)
					.addConverterFactory(GsonConverterFactory.create()).client(client).build();
		}

		RestService restService = retrofit.create(RestService.class);
		restService.logIn(credentials).enqueue(new Callback<User>() {

			@Override
			public void onFailure(Call<User> call, Throwable e) {
				// TODO Auto-generated method stub
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						controller.stopLoading();
						UiUtils.showErrorDialog(MainApp.getStage(), "Error", "Error");
						System.out.println("Error OnFailure");
					}
				});
			}

			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				// TODO Auto-generated method stub
				if (response.isSuccessful()) {
					System.out.println("Success API");
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							controller.stopLoading();
							User user = response.body();
							PreferencesUtils.setUserConnected(userEmail, userPassword, user.getApiToken(),
									user.getUserId(), true);
							launchMainView();
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
								controller.stopLoading();
								/**
								 * TEMPORARY
								 */
								// TODO Delete this line
								PreferencesUtils.setUserConnected(userEmail, userPassword, "Token", 0, true);
								launchMainView();
								/**
								 * TEMPORARY
								 */
								// TODO Uncomment
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
								controller.stopLoading();
								UiUtils.showErrorDialog(MainApp.getStage(), "Error", "Error");
							}
						});
					}
				}
			}
		});
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
}
