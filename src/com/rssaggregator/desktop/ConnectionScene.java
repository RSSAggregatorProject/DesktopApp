package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.gson.Gson;
import com.rssaggregator.desktop.model.APIError;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.CredentialsWrapper;
import com.rssaggregator.desktop.network.RestService;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;
import com.rssaggregator.desktop.view.ConnectionController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConnectionScene implements Callback<Credentials> {

	private MainApp mainApp;
	private BorderPane rootView;
	private ConnectionController controller;

	private static String userEmail;
	private static String userPassword;

	public ConnectionScene(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void launchConnectionView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.CONNECTION_VIEW));
			this.rootView = (BorderPane) loader.load();

			Scene scene = new Scene(this.rootView);
			this.mainApp.getPrimaryStage().setScene(scene);
			this.mainApp.getPrimaryStage().setResizable(false);

			controller = loader.getController();
			controller.setConnectionScene(this);

			this.mainApp.getPrimaryStage().show();

			resetUserPreferences();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logIn(String userEmail, String userPassword) {
		this.userEmail = userEmail;
		this.userPassword = userPassword;

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		OkHttpClient client = builder.build();

		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.comeon.io")
				.addConverterFactory(GsonConverterFactory.create()).client(client).build();

		RestService restService = retrofit.create(RestService.class);
		CredentialsWrapper wrapper = new CredentialsWrapper();
		wrapper.setLogin(userEmail);
		wrapper.setPassword(userPassword);
		restService.logIn(wrapper).enqueue(this);

		controller.showLoading();
	}

	public void launchMainView() {
		MainViewScene scene = new MainViewScene(this.mainApp);
		scene.launchMainView();
	}

	public void launchSignUpView() {
		SignUpScene scene = new SignUpScene(this.mainApp, controller);
		scene.launchSignUpView();
	}

	private void resetUserPreferences() {
		PreferencesUtils.setUserEmail("");
		PreferencesUtils.setUserPassword("");
		PreferencesUtils.setApiToken("");
		PreferencesUtils.setIsConnected(false);
	}

	@Override
	public void onFailure(Call<Credentials> call, Throwable arg1) {
		// TODO Auto-generated method stub
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.stopLoading();
				controller.errorLogin("Error");
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
						controller.errorLogin(error.getError());
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
						controller.errorLogin("Error");
					}
				});
			}
		}
	}

}
