package com.rssaggregator.desktop;

import java.io.IOException;
import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rssaggregator.desktop.event.RefreshCategoriesEvent;
import com.rssaggregator.desktop.model.CategoriesWrapper;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.model.ItemReadStateWrapper;
import com.rssaggregator.desktop.model.ItemsWrapper;
import com.rssaggregator.desktop.network.RssApi;
import com.rssaggregator.desktop.network.event.FeedDeletedEvent;
import com.rssaggregator.desktop.network.event.FetchAllItemsEvent;
import com.rssaggregator.desktop.network.event.FetchCategoriesEvent;
import com.rssaggregator.desktop.network.event.FetchItemsByCategoryEvent;
import com.rssaggregator.desktop.network.event.FetchItemsByChannelEvent;
import com.rssaggregator.desktop.network.event.FetchStarredItemsEvent;
import com.rssaggregator.desktop.network.event.ItemsByChannelUpdatedEvent;
import com.rssaggregator.desktop.network.event.LogOutEvent;
import com.rssaggregator.desktop.utils.CategoriesUtils;
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

	/**
	 * Constructor.
	 */
	public MainViewScene() {
		this.primaryStage = MainApp.getStage();
		this.categories = FXCollections.observableArrayList();
		this.items = FXCollections.observableArrayList();

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

	/**
	 * Launches the Add Feed View.
	 */
	public void launchAddFeedView() {
		AddFeedScene addFeedScene = new AddFeedScene(this.categories, this.mainViewController);
		addFeedScene.launchAddFeedView();
	}

	/**
	 * Launches the Article Details of the corresponding item.
	 * 
	 * @param item
	 *            Item to display.
	 */
	public void launchArticleDetailsView(Item item) {
		ArticleDetailsScene articleDetailsScene = new ArticleDetailsScene(item);
		articleDetailsScene.setData(this.mainViewController);
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

	/**
	 * Loads the items from the STAR category.
	 */
	public void loadStarredItems() {
		// Starts loading view
		this.mainViewController.showLoading();

		this.rssApi.fetchStarredItems();
	}

	/**
	 * Loads the items from the CATEGORY category.
	 * 
	 * @param channelId
	 */
	public void loadItemsByCategory(Integer categoryId) {
		// Starts loading view.
		this.mainViewController.showLoading();

		this.rssApi.fetchItemsByCategory(categoryId);
	}

	/**
	 * Loads the items from the CHANNEL category.
	 * 
	 * @param channelId
	 */
	public void loadItemsByChannel(Integer channelId) {
		// Starts loading view.
		this.mainViewController.showLoading();

		this.rssApi.fetchItemsByChannel(channelId);
	}

	/**
	 * Unsubscribe to the channel.
	 * 
	 * @param channelId
	 */
	public void deleteFeed(Integer channelId) {
		// Starts loading view.
		this.mainViewController.showLoading();

		this.rssApi.deleteFeed(channelId);
	}

	/**
	 * Update the read state of an entire channel.
	 * 
	 * @param channelId
	 *            Id of the channel to update.
	 */
	public void updateStateItemByChannel(Integer channelId) {
		this.mainViewController.showLoading();

		ItemReadStateWrapper wrapper = new ItemReadStateWrapper(true);
		this.rssApi.updateItemStateByChannel(channelId, wrapper);
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
					updateViewWithData(wrapper, "");
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
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_FETCH_ITEMS, Globals.ERROR_FETCH_ITEMS,
							errorMessage);
				}
			});
		}
	}

	/**
	 * Event after the api returns the result of the fetchStarItems request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleFetchStarredItems(FetchStarredItemsEvent event) {
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
					updateViewWithData(wrapper, "STAR");
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
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_FETCH_ITEMS, Globals.ERROR_FETCH_ITEMS,
							errorMessage);
				}
			});
		}
	}

	/**
	 * Event after the api returns the result of the fetchItemsByCategory
	 * request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleFetchItemsByCategory(FetchItemsByCategoryEvent event) {
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
					updateViewWithData(wrapper, "");
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
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_FETCH_ITEMS, Globals.ERROR_FETCH_ITEMS,
							errorMessage);
				}
			});
		}
	}

	/**
	 * Event after the api returns the result of the fetchItemsByChannel
	 * request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleFetchItemsByChannel(FetchItemsByChannelEvent event) {
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
					updateViewWithData(wrapper, "");
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
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_FETCH_ITEMS, Globals.ERROR_FETCH_ITEMS,
							errorMessage);
				}
			});
		}
	}

	/**
	 * Event after the api returns the result of the UpdateReadStateByChannel
	 * request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleUpdateReadStateByChannel(ItemsByChannelUpdatedEvent event) {
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
					mainViewController.updateStateChannelItem();
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
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_UPDATE_ITEM, Globals.ERROR_UPDATE_ITEM,
							errorMessage);
				}
			});
		}
	}

	/**
	 * Updates the view with the data.
	 * 
	 * @param data
	 */
	private void updateViewWithData(ItemsWrapper data, String type) {
		List<Item> fetchedItems = CategoriesUtils.fillDataFromChannelList(data);
		items = FXCollections.observableArrayList();
		items.addAll(fetchedItems);
		mainViewController.updateDataView(fetchedItems, type);
	}

	/**
	 * Updates the view with the data.
	 * 
	 * @param data
	 */
	private void updateViewWithData(CategoriesWrapper data, String type) {
		List<Item> fetchedItems = CategoriesUtils.fillDataFromChannelList(data);
		items = FXCollections.observableArrayList();
		items.addAll(fetchedItems);
		mainViewController.updateDataView(fetchedItems, type);
	}

	/**
	 * Event after the api returns the result of the deleteFeed request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleDeleteFeed(FeedDeletedEvent event) {
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
					launchMainView();
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
					UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_DELETE_FEED, Globals.ERROR_DELETE_FEED,
							errorMessage);
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
