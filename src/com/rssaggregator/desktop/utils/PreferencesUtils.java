package com.rssaggregator.desktop.utils;

import java.util.prefs.Preferences;

import com.rssaggregator.desktop.MainApp;

public class PreferencesUtils {

	public static String getUserEmail() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		String userEmail = preferences.get(Globals.PREF_USER_EMAIL, "");
		return userEmail;
	}

	public static void setUserEmail(String userEmail) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		preferences.put(Globals.PREF_USER_EMAIL, userEmail);
	}

	public static String getUserPassword() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		String userPassword = preferences.get(Globals.PREF_USER_PASSWORD, "");
		return userPassword;
	}

	public static void setUserPassword(String userPassword) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		preferences.put(Globals.PREF_USER_PASSWORD, userPassword);
	}

	public static String getApiToken() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		String apiToken = preferences.get(Globals.PREF_API_TOKEN, "");
		return apiToken;
	}

	public static void setApiToken(String apiToken) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		preferences.put(Globals.PREF_API_TOKEN, apiToken);
	}

	public static Integer getUserId() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		Integer userId = preferences.getInt(Globals.PREF_USER_ID, -1);
		return userId;
	}

	public static void setUserId(Integer userId) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		preferences.putInt(Globals.PREF_USER_ID, userId);
	}

	public static boolean isConnected() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		boolean isConnected = preferences.getBoolean(Globals.PREF_IS_CONNECTED, false);
		return isConnected;
	}

	public static void setIsConnected(boolean isConnected) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		preferences.putBoolean(Globals.PREF_IS_CONNECTED, isConnected);
	}

	/**
	 * Resets the Preferences of the user each time the user goes to the
	 * connection view.
	 */
	public static void resetUserPreferences() {
		setUserEmail("");
		setUserPassword("");
		setApiToken("");
		setUserId(-1);
		setIsConnected(false);
	}

	public static void setUserConnected(String userEmail, String userPassword, String apiToken, Integer userId,
			boolean isConnected) {
		setUserEmail(userEmail);
		setUserPassword(userPassword);
		setApiToken(apiToken);
		setUserId(userId);
		setIsConnected(isConnected);
	}
}
