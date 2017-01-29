package com.rssaggregator.desktop;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.model.ItemStateWrapper;
import com.rssaggregator.desktop.network.RssApi;
import com.rssaggregator.desktop.network.event.ItemUpdatedEvent;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.UiUtils;
import com.rssaggregator.desktop.view.ArticleDetailsController;
import com.rssaggregator.desktop.view.MainViewController;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Controller for the Article Details View.
 * 
 * @author Irina
 *
 */
public class ArticleDetailsScene {

	// View.
	private AnchorPane rootView;
	private Stage stage;

	// Controllers.
	private ArticleDetailsController articleDetailsController;
	private MainViewController mainViewController;

	// Network
	private EventBus eventBus;
	private RssApi rssApi;

	// Scene
	private ArticleDetailsScene instance;

	// Data
	private Item item;

	private boolean isReadChanged = false;

	/**
	 * Constructor
	 * 
	 * @param item
	 *            Item
	 */
	public ArticleDetailsScene(Item item) {
		this.item = item;

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
	 * Sets the data
	 * 
	 * @param mainViewController
	 *            MainViewController.
	 */
	public void setData(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}

	/**
	 * Launches the article details view by loading the FXML.
	 */
	public void launchArticleDetailsView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.ARTICLE_DETAILS_VIEW));

			this.articleDetailsController = new ArticleDetailsController(this.item);
			this.articleDetailsController.setData(this, this.mainViewController);
			loader.setController(this.articleDetailsController);

			this.rootView = (AnchorPane) loader.load();

			Scene scene = new Scene(this.rootView);

			this.stage = new Stage();
			this.stage.initOwner(MainApp.getStage());
			this.stage.initModality(Modality.APPLICATION_MODAL);
			this.stage.setTitle(Globals.ARTICLE_DETAILS_TITLE_NAME);
			this.stage.setResizable(false);
			this.stage.getIcons().add(new Image(Globals.RSS_LOGO_LINK));
			this.stage.setScene(scene);

			this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					eventBus.unregister(instance);
				}
			});

			this.stage.show();
			if (!this.item.isRead()) {
				this.changeReadState(true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Changes the read state of the item by calling the API.
	 * 
	 * @param readState
	 *            New Read state.
	 */
	public void changeReadState(boolean readState) {
		if (this.item != null) {
			ItemStateWrapper wrapper = new ItemStateWrapper(this.item.isStarred(), readState);

			this.isReadChanged = true;
			this.rssApi.updateItemState(this.item.getItemId(), wrapper);
		}
	}

	/**
	 * Changes the star state of the item by calling the API.
	 * 
	 * @param starState
	 */
	public void changeStarState(boolean starState) {
		if (this.item != null) {
			ItemStateWrapper wrapper = new ItemStateWrapper(starState, this.item.isRead());

			this.isReadChanged = false;
			this.rssApi.updateItemState(this.item.getItemId(), wrapper);
		}
	}

	//
	//
	// Event methods.
	//
	//
	/**
	 * Event after the api returns the result of the signUp request.
	 * 
	 * @param event
	 */
	@Subscribe
	public void handleReadState(ItemUpdatedEvent event) {
		if (event.isSuccess()) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (isReadChanged) {
						articleDetailsController.updateRead();
					} else {
						articleDetailsController.updateStar();
					}
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
					if (isReadChanged) {
						articleDetailsController.updateReadError();
					} else {
						articleDetailsController.updateStarError();
					}
					UiUtils.showErrorDialog(stage, Globals.ERROR_UPDATE_ITEM, Globals.ERROR_UPDATE_ITEM, errorMessage);
				}
			});
		}
	}
}
