package com.rssaggregator.desktop.utils;

public class Globals {

	/**
	 * Application Name.
	 */
	public static final String APP_NAME = "RSS Aggregator";

	/**
	 * Windows Names.
	 */
	public static final String ADD_FEED_TITLE_NAME = "Add a feed";
	public static final String CREATE_CATEGORY_TITLE_NAME = "Create a category";
	public static final String ARTICLE_DETAILS_TITLE_NAME = "Article details";

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
	public static final String ARTICLE_DETAILS_VIEW = "view/Layout_ArticleDetails.fxml";
	public static final String DIALOG_LOADING_VIEW = "view/Dialog_Loading.fxml";
	public static final String DIALOG_ADD_FEED_VIEW = "view/Dialog_AddFeed.fxml";
	public static final String ROW_CHANNEL_VIEW = "view/Row_Channel.fxml";
	public static final String ROW_CATEGORY_VIEW = "view/Row_Category.fxml";
	public static final String ROW_ARTICLE_VIEW = "Row_Article.fxml";
	public static final String ROW_ARTICLE_EXTENDED_VIEW = "Row_Article_Extended.fxml";

	/**
	 * Image links
	 */
	public static final String RSS_LOGO_LINK = "file:resources/images/icon_rss.png";

	/**
	 * Time
	 */
	private static final int SECONDS = 1000;
	public static final int SPLASH_SCREEN_TIME = 3 * SECONDS;
}
