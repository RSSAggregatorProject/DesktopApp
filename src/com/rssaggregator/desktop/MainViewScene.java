package com.rssaggregator.desktop;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.rssaggregator.desktop.model.ApiError;
import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.ItemsWrapper;
import com.rssaggregator.desktop.model.StarItemsWrapper;
import com.rssaggregator.desktop.model.TmpArticle;
import com.rssaggregator.desktop.model.TmpCategory;
import com.rssaggregator.desktop.model.TmpChannel;
import com.rssaggregator.desktop.network.RestService;
import com.rssaggregator.desktop.network.RssApi;
import com.rssaggregator.desktop.network.event.FetchCategoriesEvent;
import com.rssaggregator.desktop.network.event.LogOutEvent;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;
import com.rssaggregator.desktop.utils.UiUtils;
import com.rssaggregator.desktop.view.MainViewController;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Controller for the Main View.
 * 
 * @author Irina
 *
 */
public class MainViewScene {

	// View attributes.
	private Stage primaryStage;
	private BorderPane rootView;

	// Controllers.
	private MainViewController mainViewController;

	// Network
	private EventBus eventBus;
	private RssApi rssApi;

	// Scene
	private MainViewScene instance;

	// Network attributes.
	private OkHttpClient client;
	private Retrofit retrofit;
	private RestService restService;
	private String apiToken;

	// Data
	private ObservableList<Category> categories;
	private ObservableList<TmpCategory> tmpCategories;

	public MainViewScene() {
		this.primaryStage = MainApp.getStage();
		this.apiToken = PreferencesUtils.getApiToken();
		this.categories = FXCollections.observableArrayList();

		this.instance = this;

		this.rssApi = MainApp.getRssApi();
		this.eventBus = MainApp.getEventBus();

		// Check if the EventBus attribute is initialized.
		if (this.eventBus == null) {
			System.out.println("Event Bus Null");
			this.eventBus = new EventBus();
			this.rssApi.setEventBus(this.eventBus);
		}

		this.eventBus.register(this.instance);
	}

	/**
	 * Launches the Main View by initializing the root layout and the main view.
	 */
	public void launchMainView() {
		createCategoryArray();
		initRootLayout();
		initMainView();
		loadCategories();
	}

	/**
	 * Loads the Root Layout.
	 */
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.MAIN_VIEW_ROOT));
			this.rootView = (BorderPane) loader.load();

			Scene scene = new Scene(this.rootView);

			this.primaryStage.setScene(scene);
			this.primaryStage.setResizable(true);
			this.primaryStage.setMinWidth(1200d);
			this.primaryStage.setMinHeight(840d);

			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
			gsonBuilder.excludeFieldsWithoutExposeAnnotation();
			Gson gson = gsonBuilder.create();
			CategoriesWrapper wrapper = gson.fromJson(
					new FileReader("C:\\Users\\Irina\\Documents\\DesktopApp\\resources\\jsons\\categories.json"),
					CategoriesWrapper.class);
			System.out.println("Wrapper: " + wrapper.getCategories().size() + " | "
					+ wrapper.getCategories().get(0).getChannels().size());

			this.primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class DateDeserializer implements JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2)
				throws JsonParseException {
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
	 * Loads the Main View.
	 */
	private void initMainView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.MAIN_VIEW));
			AnchorPane pane = (AnchorPane) loader.load();

			this.mainViewController = loader.getController();
			this.mainViewController.setMainViewScene(this);
			this.mainViewController.initMainCategories();

			this.rootView.setCenter(pane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void launchAddFeedView() {
		AddFeedScene addFeedScene = new AddFeedScene(this.tmpCategories, this.mainViewController);
		addFeedScene.launchAddFeedView();
	}

	public void launchArticleDetailsView(TmpArticle article) {
		ArticleDetailsScene articleDetailsScene = new ArticleDetailsScene(article);
		articleDetailsScene.launchArticleDetailsView();
	}

	/**
	 * Loads the categories of the user.
	 */
	private void loadCategories() {
		// Starts Loading View
		this.mainViewController.showLoading();

		this.rssApi.fetchCategories(this.apiToken);
	}

	public void loadChannels(boolean wantsStarred) {
		StarItemsWrapper wrapper = new StarItemsWrapper();
		wrapper.setStarred(wantsStarred);

		// Starts Loading view.
		this.mainViewController.showLoading();

		checkNetworkAttributes();

		restService.fetchChannels(this.apiToken/* , wrapper */).enqueue(new Callback<ItemsWrapper>() {
			@Override
			public void onFailure(Call<ItemsWrapper> call, Throwable e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						mainViewController.stopLoading();
						UiUtils.showErrorDialog(MainApp.getStage(), "Error", "Error");
					}
				});
			}

			@Override
			public void onResponse(Call<ItemsWrapper> call, Response<ItemsWrapper> response) {
				if (response.isSuccessful()) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							mainViewController.stopLoading();
							ItemsWrapper wrapper = response.body();
							// TODO Handle fetching categories
						}
					});
				} else {
					try {
						String json = response.errorBody().string();
						ApiError error = new Gson().fromJson(json, ApiError.class);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								mainViewController.stopLoading();
								/**
								 * TEMPORARY
								 */
								// TODO Delete this line
								try {
									GsonBuilder gsonBuilder = new GsonBuilder();
									gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
									gsonBuilder.excludeFieldsWithoutExposeAnnotation();
									Gson gson = gsonBuilder.create();
									ItemsWrapper wrapper = gson.fromJson(
											new FileReader(
													"C:\\Users\\Irina\\Documents\\DesktopApp\\resources\\jsons\\channels_all.json"),
											ItemsWrapper.class);
									System.out.println("Items: " + wrapper.getChannels().get(0).getItems().size());
								} catch (Exception e) {
									e.printStackTrace();
								}
								/**
								 * TEMPORARY
								 */
								// TODO Uncomment
								// UiUtils.showErrorDialog(MainApp.getStage(),
								// "Error", error.getError());
							}
						});
						System.out.println(error.getError());
					} catch (IOException e) {
						e.printStackTrace();
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								mainViewController.stopLoading();
								UiUtils.showErrorDialog(MainApp.getStage(), "Error", "Error");
							}
						});
					}
				}
			}
		});
	}

	private void checkNetworkAttributes() {
		if (this.client == null) {
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			this.client = builder.build();
		}
		if (this.retrofit == null) {
			this.retrofit = new Retrofit.Builder().baseUrl(Globals.API_SERVER_URL)
					.addConverterFactory(GsonConverterFactory.create()).client(this.client).build();
		}
	}

	/**
	 * TEMPORARY METHOD TO CREATE FAKE DATA.
	 */
	private void createCategoryArray() {
		this.tmpCategories = FXCollections.observableArrayList();
		TmpCategory category1 = new TmpCategory();
		category1.setId(0);
		category1.setName("Automobile");
		TmpCategory category2 = new TmpCategory();
		category2.setId(1);
		category2.setName("Sport");
		TmpCategory category3 = new TmpCategory();
		category3.setId(2);
		category3.setName("News");

		TmpChannel channel1 = new TmpChannel();
		channel1.setName("Turbo.fr");
		channel1.setUnreadArticles(0);
		TmpChannel channel2 = new TmpChannel();
		channel2.setName("Caradisiac");
		channel2.setUnreadArticles(10);
		TmpChannel channel3 = new TmpChannel();
		channel3.setName("Eurosport");
		channel3.setUnreadArticles(1);
		TmpChannel channel4 = new TmpChannel();
		channel4.setName("Europe 1");
		channel4.setUnreadArticles(26);
		TmpChannel channel5 = new TmpChannel();
		channel5.setName("L'Est Républicain");
		channel5.setUnreadArticles(156);

		ArrayList<TmpChannel> autoChannels = new ArrayList<>();
		autoChannels.add(channel1);
		autoChannels.add(channel2);

		ArrayList<TmpChannel> sportChannels = new ArrayList<>();
		sportChannels.add(channel3);

		ArrayList<TmpChannel> newsChannels = new ArrayList<>();
		newsChannels.add(channel4);
		newsChannels.add(channel5);

		category1.setChannels(autoChannels);
		category2.setChannels(sportChannels);
		category3.setChannels(newsChannels);

		this.tmpCategories.addAll(category1, category2, category3);
	}

	//
	//
	// Event methods.
	//
	//
	/**
	 * Event after the api returns the result of the fetchCategories request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleFetchCategories(FetchCategoriesEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				mainViewController.stopLoading();
			}
		});

		if (event.isSuccess()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					CategoriesWrapper wrapper = event.getData();
					if (wrapper.getCategories() != null) {
						categories.clear();
						categories.addAll(wrapper.getCategories());
					} else {
						categories = null;
					}
					mainViewController.setCategories(categories);
				}
			});
		} else {
			String errorMessage;
			if (event.getThrowable().getMessage() != null) {
				errorMessage = event.getThrowable().getMessage();
			} else {
				errorMessage = "Error";
			}
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					UiUtils.showErrorDialog(MainApp.getStage(), "Error", errorMessage);
				}
			});
		}
	}

	/**
	 * Event after log out.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleLogOut(LogOutEvent event) {
		if (this.eventBus != null) {
			System.err.println("Log OUT");
			this.eventBus.unregister(this.instance);
		}
	}
}
