package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.network.RssApi;
import com.rssaggregator.desktop.network.event.CategoryAddedEvent;
import com.rssaggregator.desktop.network.event.FeedAddedEvent;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.UiUtils;
import com.rssaggregator.desktop.view.AddFeedController;
import com.rssaggregator.desktop.view.MainViewController;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AddFeedScene {

	// Data
	private ObservableList<Category> categories;

	private Stage addFeedStage;
	private MainViewController mainViewController;

	// Network attributes;
	private EventBus eventBus;
	private RssApi rssApi;

	// Scene
	private AddFeedScene instance;

	private AddFeedController addFeedController;

	public AddFeedScene(ObservableList<Category> categories, MainViewController mainViewController) {
		this.categories = categories;
		this.mainViewController = mainViewController;

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

	public void launchAddFeedView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.DIALOG_ADD_FEED_VIEW));
			AnchorPane rootView = (AnchorPane) loader.load();

			this.addFeedStage = new Stage();
			Scene scene = new Scene(rootView);
			this.addFeedStage.setTitle(Globals.ADD_FEED_TITLE_NAME);
			this.addFeedStage.initOwner(MainApp.getStage());
			this.addFeedStage.initModality(Modality.APPLICATION_MODAL);
			this.addFeedStage.setResizable(false);
			this.addFeedStage.getIcons().add(new Image(Globals.RSS_LOGO_LINK));
			this.addFeedStage.setScene(scene);

			this.addFeedStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					System.out.println("close");
					eventBus.unregister(instance);
				}
			});

			addFeedController = loader.getController();
			addFeedController.setData(instance, this.addFeedStage);
			addFeedController.setMainViewController(this.mainViewController);
			addFeedController.setCategories(this.categories);

			this.addFeedStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds a category thanks to the API server.
	 * 
	 * @param categoryName
	 */
	public void addCategory(String categoryName) {
		// Starts Loading view.
		this.addFeedController.showLoading();

		this.rssApi.addCategory(categoryName);
	}

	/**
	 * Adds a feed to a category thanks to the API server.
	 * 
	 * @param categoryId
	 * @param rssLink
	 */
	public void addFeed(Integer categoryId, String rssLink) {
		// Starts Loading view.
		this.addFeedController.showLoading();

		this.rssApi.addFeed(categoryId, rssLink);
	}

	/**
	 * Closes the stage and unregister to eventbus events.
	 */
	public void closeStage() {
		System.out.println("Close stage");
		this.eventBus.unregister(this.instance);
		if (this.addFeedStage != null && this.addFeedStage.isShowing()) {
			this.addFeedStage.close();
		}
	}

	//
	//
	// Event methods.
	//
	//
	/**
	 * Event after the api returns the result of the addCategory request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleAddCategory(CategoryAddedEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				addFeedController.stopLoading();
			}
		});

		if (event.isSuccess()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					addFeedController.updateCategoryAdded(event.getData());
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
					UiUtils.showErrorDialog(addFeedStage, Globals.ERROR_ADD_CATEGORY, Globals.ERROR_ADD_CATEGORY,
							errorMessage);
				}
			});
		}
	}

	/**
	 * Event after the api returns the result of the addFeed request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleAddFeed(FeedAddedEvent event) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				addFeedController.stopLoading();
			}
		});

		if (event.isSuccess()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					addFeedController.updateFeedAdded(event.getData());
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
					UiUtils.showErrorDialog(addFeedStage, Globals.ERROR_ADD_CATEGORY, Globals.ERROR_ADD_CATEGORY,
							errorMessage);
				}
			});
		}
	}

}
