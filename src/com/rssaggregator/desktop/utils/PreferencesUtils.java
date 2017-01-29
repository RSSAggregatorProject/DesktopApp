package com.rssaggregator.desktop.utils;

import java.util.prefs.Preferences;

import com.rssaggregator.desktop.MainApp;

/**
 * Utility class for setting and getting preferences of the user.
 * 
 * @author Irina
 *
 */
public class PreferencesUtils {

	/**
	 * Gets the Email User.
	 *
	 * @return String Email user.
	 */
	public static String getUserEmail() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		String userEmail = preferences.get(Globals.PREF_USER_EMAIL, "");
		return userEmail;
	}

	/**
	 * Sets the Email User.
	 *
	 * @param userEmail
	 *            email of the user.
	 */
	public static void setUserEmail(String userEmail) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		preferences.put(Globals.PREF_USER_EMAIL, userEmail);
	}

	/**
	 * Gets the Password User.
	 *
	 * @return String Password user.
	 */
	public static String getUserPassword() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		String userPassword = preferences.get(Globals.PREF_USER_PASSWORD, "");
		return userPassword;
	}

	/**
	 * Sets the Password User.
	 *
	 * @param userPassword
	 *            password of the user.
	 */
	public static void setUserPassword(String userPassword) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		preferences.put(Globals.PREF_USER_PASSWORD, userPassword);
	}

	/**
	 * Gets the API token of the user.
	 *
	 *
	 * @return String API token
	 */
	public static String getApiToken() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		String apiToken = preferences.get(Globals.PREF_API_TOKEN, "");
		return apiToken;
	}

	/**
	 * Sets API token of the user to the Preferences.
	 *
	 * @param apiToken
	 *            Api Token of the session
	 */
	public static void setApiToken(String apiToken) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		if (apiToken == null) {
			preferences.put(Globals.PREF_API_TOKEN, "");
		} else {
			preferences.put(Globals.PREF_API_TOKEN, apiToken);
		}
	}

	/**
	 * Gets the User Id
	 *
	 * @return Integer User Id.
	 */
	public static Integer getUserId() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		Integer userId = preferences.getInt(Globals.PREF_USER_ID, -1);
		return userId;
	}

	/**
	 * Sets the User ID.
	 * 
	 * @param activity
	 *            activity
	 * @param userId
	 *            id of the user.
	 */
	public static void setUserId(Integer userId) {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		preferences.putInt(Globals.PREF_USER_ID, userId);
	}

	/**
	 * Knows if the user is connected or not.
	 * 
	 * @return boolean is connected or not.
	 */
	public static boolean isConnected() {
		Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
		boolean isConnected = preferences.getBoolean(Globals.PREF_IS_CONNECTED, false);
		return isConnected;
	}

	/**
	 * Sets if the user is connected or not.
	 * 
	 * @param isConnected
	 *            boolean is connected or not.
	 */
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

	/**
	 * Sets the Preferences of the user each time the user connects to the API.
	 * 
	 * @param userEmail
	 *            email of the user
	 * @param userPassword
	 *            passsword of the user
	 * @param apiToken
	 *            API token of the user
	 * @param userId
	 *            id of the user
	 * @param isConnected
	 *            boolean to know if he is connected.
	 */
	public static void setUserConnected(String userEmail, String userPassword, String apiToken, Integer userId,
			boolean isConnected) {
		setUserEmail(userEmail);
		setUserPassword(userPassword);
		setApiToken(apiToken);
		setUserId(userId);
		setIsConnected(isConnected);
	}
}
