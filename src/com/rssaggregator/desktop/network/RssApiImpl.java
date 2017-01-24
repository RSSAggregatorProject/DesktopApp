package com.rssaggregator.desktop.network;

import java.io.FileReader;
import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.model.ApiError;
import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.Credentials;
import com.rssaggregator.desktop.model.SignUpWrapper;
import com.rssaggregator.desktop.model.User;
import com.rssaggregator.desktop.network.event.FetchCategoriesEvent;
import com.rssaggregator.desktop.network.event.LogInEvent;
import com.rssaggregator.desktop.network.event.SignUpEvent;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;
import com.rssaggregator.desktop.utils.UiUtils;

import javafx.application.Platform;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RssApiImpl implements RssApi {
	private RestService restService;
	private EventBus eventBus;

	public RssApiImpl(RestService restService, EventBus eventBus) {
		this.restService = restService;
		this.eventBus = eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	private void initializeNetworkAttributes() {
		System.out.println("Initiliaze Attributes.");

		// Set OkHttpClient.
		OkHttpClient client = MainApp.getOkHttpClient();
		if (client == null) {
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			client = builder.build();
		}

		// Set Retrofit.
		Retrofit retrofit = MainApp.getRetrofit();
		if (retrofit == null) {
			retrofit = new Retrofit.Builder().baseUrl(Globals.API_SERVER_URL)
					.addConverterFactory(GsonConverterFactory.create()).client(client).build();
		}

		this.restService = retrofit.create(RestService.class);
	}

	@Override
	public void logIn(String userEmail, String userPassword) {
		Credentials credentials = new Credentials(userEmail, userPassword);

		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.logIn(credentials).enqueue(new Callback<User>() {

			@Override
			public void onFailure(Call<User> call, Throwable e) {
				eventBus.post(new LogInEvent(new Throwable("Error")));
			}

			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				if (response.isSuccessful()) {
					System.out.println("[LOGIN] Success from the Api");
					User user = response.body();
					PreferencesUtils.setUserConnected(userEmail, userPassword, user.getApiToken(), user.getUserId(),
							true);
					eventBus.post(new LogInEvent(user));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						System.out.println("[LOGIN] Error from the Api: " + error.getError());
						/**
						 * TEMPORARY
						 */
						// TODO Delete this line
						PreferencesUtils.setUserConnected(userEmail, userPassword, "Token", 0, true);
						eventBus.post(new LogInEvent(new User()));
						// TODO Uncomment
						// eventBus.post(new LogInEvent(new
						// Throwable(error.getError())));
					} catch (IOException e) {
						e.printStackTrace();
						eventBus.post(new LogInEvent(new Throwable("Error")));
					}
				}
			}
		});
	}

	@Override
	public void signUp(String userEmail, String userPassword) {
		Credentials credentials = new Credentials(userEmail, userPassword);

		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.signUp(credentials).enqueue(new Callback<SignUpWrapper>() {

			@Override
			public void onFailure(Call<SignUpWrapper> call, Throwable e) {
				eventBus.post(new SignUpEvent(new Throwable("Error")));
			}

			@Override
			public void onResponse(Call<SignUpWrapper> call, Response<SignUpWrapper> response) {
				if (response.isSuccessful()) {
					System.out.println("[SIGNUP] Success from the API");
					SignUpWrapper wrapper = response.body();
					eventBus.post(new SignUpEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						System.out.println("[SIGNUP] Error from the API : " + error.getError());
						/**
						 * TEMPORARY
						 */
						// TODO Delete this line
						eventBus.post(new SignUpEvent(new SignUpWrapper()));
						// TODO Uncomment this line
						// eventBus.post(new SignUpEvent(new
						// Throwable(error.getError())));
					} catch (IOException e) {
						e.printStackTrace();
						eventBus.post(new SignUpEvent(new Throwable("Error")));
					}
				}
			}
		});
	}

	@Override
	public void fetchCategories(String authorization) {
		if (this.restService == null) {
			initializeNetworkAttributes();
		}

		this.restService.fetchCategories(authorization).enqueue(new Callback<CategoriesWrapper>() {
			@Override
			public void onFailure(Call<CategoriesWrapper> call, Throwable e) {
				System.out.println("[FETCH CATEGORIES] Error\n");
				eventBus.post(new FetchCategoriesEvent(new Throwable("Error")));
			}

			@Override
			public void onResponse(Call<CategoriesWrapper> call, Response<CategoriesWrapper> response) {
				if (response.isSuccessful()) {
					System.out.println("[FETCH CATEGORIES] Success from the API");
					CategoriesWrapper wrapper = response.body();
					eventBus.post(new FetchCategoriesEvent(wrapper));
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						System.out.println("[FETCH CATEGORIES] Error from the API : " + error.getError());
						// TODO Delete this line
						try {
							Gson gson = new Gson();
							CategoriesWrapper wrapper = gson.fromJson(
									new FileReader(
											"C:\\Users\\Irina\\Documents\\DesktopApp\\resources\\jsons\\categories.json"),
									CategoriesWrapper.class);
							eventBus.post(new FetchCategoriesEvent(wrapper));
						} catch (Exception e) {
							e.printStackTrace();
						}
						// TODO Uncomment this line
						// eventBus.post(new FetchCategoriesEvent(new
						// Throwable(error.getError())));
					} catch (IOException e) {
						e.printStackTrace();
						eventBus.post(new FetchCategoriesEvent(new Throwable("Error")));
					}
				}
			}
		});
	}
}
