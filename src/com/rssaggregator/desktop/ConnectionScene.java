package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.gson.Gson;
import com.rssaggregator.desktop.model.APIError;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.CredentialsWrapper;
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
public class ConnectionScene implements Callback<Credentials> {

	private static String userEmail;
	private static String userPassword;

	private Stage primaryStage;
	private BorderPane rootView;
	private ConnectionController controller;

	/**
	 * Constructor.
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

	public void logIn(String userEmail, String userPassword) {
		this.userEmail = userEmail;
		this.userPassword = userPassword;
		/*
		 * OkHttpClient.Builder builder = new OkHttpClient.Builder();
		 * OkHttpClient client = builder.build();
		 * 
		 * Retrofit retrofit = new
		 * Retrofit.Builder().baseUrl("http://api.comeon.io")
		 * .addConverterFactory(GsonConverterFactory.create()).client(client).
		 * build();
		 * 
		 * RestService restService = retrofit.create(RestService.class);
		 * CredentialsWrapper wrapper = new CredentialsWrapper();
		 * wrapper.setLogin(userEmail); wrapper.setPassword(userPassword);
		 * restService.logIn(wrapper).enqueue(this);
		 * 
		 * controller.showLoading();
		 */
		PreferencesUtils.setUserEmail(userEmail);
		PreferencesUtils.setUserPassword(userPassword);
		PreferencesUtils.setApiToken("Token");
		PreferencesUtils.setIsConnected(true);
		launchMainView();
	}

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

	@Override
	public void onFailure(Call<Credentials> call, Throwable arg1) {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.stopLoading();
				UiUtils.showErrorDialog(MainApp.getStage(), "Error", "Error");
				System.out.println("ERROR");
			}
		});
	}

	@Override
	public void onResponse(Call<Credentials> call, Response<Credentials> response) {
		// TODO Auto-generated method stub
		if (response.isSuccessful()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					controller.stopLoading();
					Credentials credentials = response.body();
					System.out.println(credentials.getApi_key());
					PreferencesUtils.setUserEmail(userEmail);
					PreferencesUtils.setUserPassword(userPassword);
					PreferencesUtils.setApiToken(credentials.getApi_key());
					PreferencesUtils.setIsConnected(true);
					launchMainView();
				}
			});
		} else {
			try {
				String json = response.errorBody().string();
				APIError error = new Gson().fromJson(json, APIError.class);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						controller.stopLoading();
						UiUtils.showErrorDialog(MainApp.getStage(), "Error", error.getError());
					}
				});
				System.out.println(error.getError());
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

}
