package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rssaggregator.desktop.event.RefreshCategoriesEvent;
import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.Channel;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.model.ItemsWrapper;
import com.rssaggregator.desktop.model.TmpArticle;
import com.rssaggregator.desktop.network.RssApi;
import com.rssaggregator.desktop.network.event.FetchAllItemsEvent;
import com.rssaggregator.desktop.network.event.FetchCategoriesEvent;
import com.rssaggregator.desktop.network.event.LogOutEvent;
import com.rssaggregator.desktop.utils.Globals;
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

	// Data
	private ObservableList<Category> categories;
	private ObservableList<Item> items;

	public MainViewScene() {
		this.primaryStage = MainApp.getStage();
		this.categories = FXCollections.observableArrayList();

		this.instance = this;

		this.rssApi = MainApp.getRssApi();
		this.eventBus = MainApp.getEventBus();

		// Check if the EventBus attribute is initialized.
		if (this.eventBus == null) {
			this.eventBus = new EventBus();
			this.rssApi.setEventBus(this.eventBus);
		}

		this.eventBus.register(this.instance);
	}

	/**
	 * Launches the Main View by initializing the root layout and the main view.
	 */
	public void launchMainView() {
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

			this.primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
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
			this.mainViewController.setData(this, this.eventBus);
			this.mainViewController.initMainCategories();

			this.rootView.setCenter(pane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void launchAddFeedView() {
		AddFeedScene addFeedScene = new AddFeedScene(this.categories, this.mainViewController);
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

		this.rssApi.fetchCategories();
	}

	/**
	 * Loads the items from the ALL category.
	 */
	public void loadAllItems() {
		// Starts Loading View
		this.mainViewController.showLoading();

		this.rssApi.fetchAllItems();
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
						categories = FXCollections.observableArrayList();
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
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_FETCH_CATEGORIES,
							Globals.ERROR_FETCH_CATEGORIES, errorMessage);
				}
			});
		}
	}

	/**
	 * Event after the api returns the result of the fetchAllItems request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleFetchAllItems(FetchAllItemsEvent event) {
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
					ItemsWrapper wrapper = event.getData();

					for (Channel channel : wrapper.getChannels()) {
						if (channel.getItems() != null) {
							for (Item item : channel.getItems()) {
								System.out.println(item.getTitle());
							}
						}
					}

					/*
					 * CategoriesWrapper wrapper = event.getData(); if
					 * (wrapper.getCategories() != null) { categories.clear();
					 * categories.addAll(wrapper.getCategories()); } else {
					 * categories = FXCollections.observableArrayList(); }
					 * mainViewController.setCategories(categories);
					 */
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
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_FETCH_CATEGORIES,
							Globals.ERROR_FETCH_CATEGORIES, errorMessage);
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

	/**
	 * Event after refreshing categories.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleRefreshCategories(RefreshCategoriesEvent event) {
		launchMainView();
	}
}
