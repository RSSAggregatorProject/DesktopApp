package com.rssaggregator.desktop.utils;

public class Globals {

	/**
	 * Application Name.
	 */
	public static final String APP_NAME = "RSS Aggregator";

	/**
	 * API server address.
	 */
	public static final String API_SERVER_URL = "http://dreamteamrssfeader.ddns.net:8080/rssserver/api/";

	/**
	 * Windows Names.
	 */
	public static final String ADD_FEED_TITLE_NAME = "Add a feed";
	public static final String CREATE_CATEGORY_TITLE_NAME = "Create a category";
	public static final String ARTICLE_DETAILS_TITLE_NAME = "Article details";

	/**
	 * Other texts
	 */
	public static final String ENTER_CATEGORY_NAME = "Please enter a category name: ";

	/**
	 * Preferences Keys
	 */
	public static final String PREF_USER_EMAIL = "pref_user_email";
	public static final String PREF_USER_PASSWORD = "pref_user_password";
	public static final String PREF_API_TOKEN = "pref_api_token";
	public static final String PREF_USER_ID = "pref_user_id";
	public static final String PREF_IS_CONNECTED = "pref_is_connected";

	/**
	 * Layout views
	 */
	public static final String SPLASH_SCREEN_VIEW = "view/Layout_SplashScreen.fxml";
	public static final String CONNECTION_VIEW = "view/Layout_Connection.fxml";
	public static final String SIGNUP_VIEW = "view/Layout_SignUp.fxml";
	public static final String MAIN_VIEW_ROOT = "view/Layout_MainViewRoot.fxml";
	public static final String MAIN_VIEW = "view/Layout_MainView.fxml";
	public static final String ARTICLE_DETAILS_VIEW = "view/Layout_ArticleDetails.fxml";
	public static final String DIALOG_LOADING_VIEW = "view/Dialog_Loading.fxml";
	public static final String DIALOG_ADD_FEED_VIEW = "view/Dialog_AddFeed.fxml";
	public static final String ROW_CHANNEL_VIEW = "view/Row_Channel.fxml";
	public static final String ROW_CATEGORY_VIEW = "view/Row_Category.fxml";
	public static final String ROW_ARTICLE_VIEW = "Row_Article.fxml";
	public static final String ROW_ARTICLE_EXTENDED_VIEW = "Row_Article_Extended.fxml";

	/**
	 * Category titles
	 */
	public static final String STARRED_ITEMS_TITLED_PANE = "Starred Items";
	public static final String ALL_ITEMS_TITLED_PANE = "All Items";

	/**
	 * Image links
	 */
	public static final String RSS_LOGO_LINK = "file:resources/images/icon_rss.png";

	/**
	 * Time
	 */
	private static final int SECONDS = 1000;
	public static final int SPLASH_SCREEN_TIME = 3 * SECONDS;

	/**
	 * Error Messages
	 */
	public static final String ERROR_LOGIN_MESSAGE = "Error when logging";
	public static final String ERROR_SIGNUP_MESSAGE = "Error when signing up";
	public static final String ERROR_FETCH_CATEGORIES = "Error when fetching categories";
	public static final String ERROR_FETCH_ITEMS = "Error when fetching items";
	public static final String ERROR_DELETE_FEED = "Error when unsubscribing to the channel";
	public static final String ERROR_UPDATE_ITEM = "Error when updating item";
	public static final String ERROR_ADD_CATEGORY = "Error when adding category";
	public static final String ERROR_INVALID_INPUTS = "Invalid Inputs";
	public static final String ERROR_CATEGORY_FIELD_INVALID = "The Category Name field is invalid!";
	public static final String ERROR_CATEGORY_ALREADY_EXISTS = "This category already exists!";
	public static final String ERROR_RSS_LINK_FIELD_INVALID = "The RSS Link Field is empty!";

	/**
	 * List items Type
	 */
	public static final int LIST_ALL_ITEMS_TYPE = 0;
	public static final int LIST_STARRED_ITEMS_TYPE = 1;
	public static final int LIST_CATEGORY_ITEMS_TYPE = 2;
	public static final int LIST_CHANNEL_ITEMS_TYPE = 3;
}
