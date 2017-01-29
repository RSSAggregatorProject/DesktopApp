package com.rssaggregator.desktop;

import java.util.Date;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rssaggregator.desktop.network.RestService;
import com.rssaggregator.desktop.network.RssApi;
import com.rssaggregator.desktop.network.RssApiImpl;
import com.rssaggregator.desktop.utils.DateDeserializer;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.TokenRequestInterceptor;

import javafx.application.Application;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main Class of the application. Launches the first window.
 * 
 * @author Irina
 *
 */
public class MainApp extends Application {

	private Stage primaryStage;

	/**
	 * Network attributes.
	 */
	private static MainApp instance;
	private static OkHttpClient okHttpClient;
	private static TokenRequestInterceptor tokenRequestInterceptor;
	private static Retrofit retrofit;
	private static EventBus eventBus;
	private static RssApi rssApi;

	/**
	 * Starts the primary stage. Launches the Splash Screen Scene.
	 */
	@Override
	public void start(Stage primaryStage) {
		MainApp.instance = this;
		MainApp.tokenRequestInterceptor = new TokenRequestInterceptor();

		// Set OkHttpClient
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.addInterceptor(interceptor);
		builder.addInterceptor(MainApp.tokenRequestInterceptor);
		okHttpClient = builder.build();

		// Set Retrofit
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
		gsonBuilder.excludeFieldsWithoutExposeAnnotation();
		Gson gson = gsonBuilder.create();

		Builder retrofitBuilder = new Retrofit.Builder();
		retrofitBuilder.baseUrl(Globals.API_SERVER_URL);
		retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson));
		retrofitBuilder.client(okHttpClient);
		MainApp.retrofit = retrofitBuilder.build();

		RestService restService = retrofit.create(RestService.class);

		// Set EventBus
		MainApp.eventBus = new EventBus();

		// Set RssApi
		MainApp.rssApi = new RssApiImpl(restService, MainApp.eventBus);

		this.primaryStage = primaryStage;

		SplashScreenScene scene = new SplashScreenScene(this);
		scene.launchSplashScreen();
	}

	/**
	 * Gets the primary stage.
	 * 
	 * @return Stage Primary stage.
	 */
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	/**
	 * Gets the stage from everywhere in the app.
	 * 
	 * @return Stage Primary Stage.
	 */
	public static Stage getStage() {
		return MainApp.instance.getPrimaryStage();
	}

	/**
	 * Gets the instance of the Main Class.
	 * 
	 * @return MainApp instance.
	 */
	public static MainApp getMainApp() {
		return instance;
	}

	/**
	 * Gets the instance of the OkHttpClient.
	 * 
	 * @return OkHttpClient instance.
	 */
	public static OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}

	/**
	 * Gets the instance of the Retrofit.
	 * 
	 * @return Retrofit instance.
	 */
	public static Retrofit getRetrofit() {
		return retrofit;
	}

	/**
	 * Gets the instance of the EventBus.
	 * 
	 * @return EventBus instance.
	 */
	public static EventBus getEventBus() {
		return eventBus;
	}

	/**
	 * Gets the instance of the RSS API.
	 * 
	 * @return RssApi instance.
	 */
	public static RssApi getRssApi() {
		return rssApi;
	}

	/**
	 * Gets the instance of the TokenRequestInterceptor.
	 * 
	 * @return TokenRequestInterceptor instance.
	 */
	public static TokenRequestInterceptor getTokenRequestInterceptor() {
		return tokenRequestInterceptor;
	}

	/**
	 * Main method. Launches the first stage.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
