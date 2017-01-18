package com.rssaggregator.desktop.utils;

public class Globals {

	/**
	 * Application Name.
	 */
	public static final String APP_NAME = "RSS Aggregator";

	/**
	 * Preferences Keys
	 */
	public static final String PREF_USER_EMAIL = "pref_user_email";
	public static final String PREF_USER_PASSWORD = "pref_user_password";
	public static final String PREF_API_TOKEN = "pref_api_token";
	public static final String PREF_IS_CONNECTED = "pref_is_connected";

	/**
	 * Layout views
	 */
	public static final String SPLASH_SCREEN_VIEW = "view/Layout_SplashScreen.fxml";
	public static final String CONNECTION_VIEW = "view/Layout_Connection.fxml";
	public static final String SIGNUP_VIEW = "view/Layout_SignUp.fxml";
	public static final String MAIN_VIEW_ROOT = "view/Layout_MainViewRoot.fxml";
	public static final String MAIN_VIEW = "view/Layout_MainView.fxml";

	/**
	 * Time
	 */
	private static final int SECONDS = 1000;
	public static final int SPLASH_SCREEN_TIME = 3 * SECONDS;
}
