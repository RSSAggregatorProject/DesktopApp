package com.rssaggregator.desktop;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.rssaggregator.desktop.utils.Globals;

import javafx.application.Application;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main Class of the application. Launch the first window.
 * 
 * @author Irina
 *
 */
public class MainApp extends Application {

	private Stage primaryStage;

	private static MainApp instance;
	private static OkHttpClient okHttpClient;
	private static Retrofit retrofit;

	/**
	 * Starts the primary stage. Launches the Splash Screen Scene.
	 */
	@Override
	public void start(Stage primaryStage) {
		MainApp.instance = this;

		// Set OkHttpClient
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		MainApp.okHttpClient = builder.build();

		// Set Retrofit
		Gson gson = new GsonBuilder().setDateFormat("YYYY-MM-DD HH:mm:ss").excludeFieldsWithoutExposeAnnotation()
				.create();
		MainApp.retrofit = new Retrofit.Builder().baseUrl(Globals.API_SERVER_URL)
				.addConverterFactory(GsonConverterFactory.create(gson)).client(MainApp.okHttpClient).build();

		this.primaryStage = primaryStage;

		SplashScreenScene scene = new SplashScreenScene(this);
		scene.launchSplashScreen();
	}

	public class DateDeserializer implements JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
			String date = element.getAsString();
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	/**
	 * Gets the primary stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}

	/**
	 * Gets the stage from everywhere in the app.
	 * 
	 * @return
	 */
	public static Stage getStage() {
		return MainApp.instance.getPrimaryStage();
	}

	/**
	 * Gets the instance of the Main Class.
	 * 
	 * @return
	 */
	public static MainApp getMainApp() {
		return instance;
	}

	/**
	 * Gets the instance of the OkHttpClient.
	 * 
	 * @return
	 */
	public static OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}

	/**
	 * Gets the instance of the Retrofit.
	 * 
	 * @return
	 */
	public static Retrofit getRetrofit() {
		return retrofit;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
