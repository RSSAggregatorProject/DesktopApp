package com.rssaggregator.desktop.view;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.MainViewScene;
import com.rssaggregator.desktop.event.RefreshCategoriesEvent;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.Channel;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.UiUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller View of the Main View.
 * 
 * @author Irina
 *
 */
public class MainViewController {

	// Data
	private ObservableList<Category> categories;
	private ObservableList<Item> itemsList;
	private Channel selectedChannel;
	private Category selectedCategory;

	@FXML
	private Accordion categoriesAc;
	@FXML
	private Accordion defaultCategoriesAc;
	@FXML
	private TitledPane starredItemsTp;
	@FXML
	private Label unreadStarredItemsLb;
	@FXML
	private TitledPane allItemsTp;
	@FXML
	private Label unreadAllItemsLb;

	@FXML
	private Label noSelectedMessageLb;
	@FXML
	private Label categoryChannelTitleLb;
	@FXML
	private JFXToggleButton expandableListTb;
	@FXML
	private JFXButton unsubscribeBt;

	@FXML
	ListView<Item> itemsLv;

	// Views
	private FXMLLoader loader;
	private Stage loadingStage;
	private MainViewScene mainViewScene;
	private VBox channelsBox;

	// Network
	private EventBus eventBus;

	// Others
	private int LIST_ITEM_TYPE = 0;

	/**
	 * Constructor.
	 */
	public MainViewController() {
		this.loader = new FXMLLoader();
		this.categories = FXCollections.observableArrayList();
		this.itemsList = FXCollections.observableArrayList();
	}

	/**
	 * Sets Data.
	 * 
	 * @param scene
	 */
	public void setData(MainViewScene scene, EventBus eventBus) {
		this.mainViewScene = scene;
		this.eventBus = eventBus;
	}

	/**
	 * Sets the categories and set the categories to the view.
	 * 
	 * @param categories
	 */
	public void setCategories(ObservableList<Category> categories) {
		this.categories = categories;
		initCategoriesView();
	}

	@FXML
	private void initialize() {
		this.noSelectedMessageLb.setVisible(true);

		// Initialize expandable list view Toggle Button
		initExpandableToggleButton();

		this.itemsLv.setItems(itemsList);
		this.itemsLv.setCellFactory(itemsLv -> new ArticleListViewCell(itemsLv));
		itemsLv.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				/*
				 * Item selectedArticle =
				 * itemsLv.getSelectionModel().getSelectedItem();
				 * 
				 * if (selectedArticle != null) {
				 * mainViewScene.launchArticleDetailsView(selectedArticle); }
				 */
			}
		});
	}

	/**
	 * Initializes the toggle button.
	 */
	private void initExpandableToggleButton() {
		this.expandableListTb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (expandableListTb.isSelected()) {
					expandableListTb.setText("Expandable List");
					itemsLv.setCellFactory(itemsLv -> new ArticleExtendedListViewCell(itemsLv.getWidth(), "Channel"));
				} else {
					expandableListTb.setText("Simple List");
					itemsLv.setCellFactory(articlesLv -> new ArticleListViewCell(itemsLv));
				}
			}
		});
	}

	/**
	 * Initializes the All Items and Starred Items categories.
	 */
	public void initMainCategories() {
		this.starredItemsTp.setExpanded(false);
		this.starredItemsTp.setCollapsible(false);
		this.unreadStarredItemsLb.setText("0");
		this.allItemsTp.setExpanded(false);
		this.allItemsTp.setCollapsible(false);
		this.unreadAllItemsLb.setText("23");
	}

	/**
	 * Initializes the categories view (TitledPanes).
	 */
	private void initCategoriesView() {
		if (this.categories != null) {
			for (Category category : this.categories) {
				initCategoryView(category);
			}
		}
	}

	/**
	 * Initializes one category view by adding channels in the titled pane.
	 * 
	 * @param category
	 *            Category to initialize
	 */
	private void initCategoryView(Category category) {
		this.channelsBox = new VBox(0);
		this.channelsBox.setPadding(Insets.EMPTY);

		if (category.getChannels() != null) {
			for (Channel channel : category.getChannels()) {
				initChannelView(channel);
			}
		}

		try {
			this.loader = new FXMLLoader();
			this.loader.setLocation(MainApp.class.getResource(Globals.ROW_CATEGORY_VIEW));
			this.loader.setController(new RowCategoryController(category, this));
			TitledPane categoryTp = (TitledPane) this.loader.load();

			categoryTp.setContent(this.channelsBox);
			Platform.runLater(() -> {
				categoryTp.lookup(".arrow").setVisible(false);
			});
			this.categoriesAc.getPanes().add(categoryTp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add the channel view to the VBox category.
	 * 
	 * @param channel
	 */
	private void initChannelView(Channel channel) {
		try {
			this.loader = new FXMLLoader();
			this.loader.setLocation(MainApp.class.getResource(Globals.ROW_CHANNEL_VIEW));
			this.loader.setController(new RowChannelController(channel, this));
			AnchorPane channelPane = (AnchorPane) this.loader.load();

			this.channelsBox.getChildren().add(channelPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//
	//
	// Methods after api calls.
	//
	//
	/**
	 * Updates the view after fetching items.
	 * 
	 * @param data
	 */
	public void updateDataView(List<Item> data) {
		this.itemsList.clear();
		this.itemsList = FXCollections.observableArrayList();
		this.itemsList.addAll(data);
		this.itemsLv.setItems(this.itemsList);
	}

	/**
	 * Updates the view after a new category created.
	 */
	public void updateNewCategory() {
		this.eventBus.post(new RefreshCategoriesEvent());
	}

	/**
	 * Updates the view after a new channel created.
	 */
	public void updateNewChannel() {
		this.eventBus.post(new RefreshCategoriesEvent());
	}

	/*
	 * HANDLE METHODS
	 */
	/**
	 * Handles click on the Starred Items category.
	 */
	@FXML
	private void handleStarredItemsPaneClicked() {
		LIST_ITEM_TYPE = Globals.LIST_STARRED_ITEMS_TYPE;

		// Remove no selected message.
		this.noSelectedMessageLb.setVisible(false);

		// Initialize right view.
		this.categoryChannelTitleLb.setText(Globals.STARRED_ITEMS_TITLED_PANE);
		this.unsubscribeBt.setVisible(false);

		this.mainViewScene.loadStarredItems();
	}

	/**
	 * Handles click on the All Items category.
	 */
	@FXML
	private void handleAllItemsPaneClicked(MouseEvent event) {
		LIST_ITEM_TYPE = Globals.LIST_ALL_ITEMS_TYPE;

		// Remove no selected message.
		this.noSelectedMessageLb.setVisible(false);

		// Initialize view.
		this.categoryChannelTitleLb.setText(Globals.ALL_ITEMS_TITLED_PANE);
		this.unsubscribeBt.setVisible(false);

		this.mainViewScene.loadAllItems();
	}

	public void handleCategoryItemsPaneClicked(Category selectedCategory) {
		LIST_ITEM_TYPE = Globals.LIST_CATEGORY_ITEMS_TYPE;
		this.selectedCategory = selectedCategory;

		// Remove no selected message.
		this.noSelectedMessageLb.setVisible(false);

		// Initialize view.
		this.categoryChannelTitleLb.setText(this.selectedCategory.getName());
		this.unsubscribeBt.setVisible(false);
	}

	/**
	 * Handles click on a channel.
	 * 
	 * @param selectedChannel
	 */
	public void handleChannelItemsPaneClicked(Channel selectedChannel) {
		LIST_ITEM_TYPE = Globals.LIST_CHANNEL_ITEMS_TYPE;

		// Do nothing if the channel is already selected.
		if (this.selectedChannel != null) {
			if (this.selectedChannel.equals(selectedChannel)) {
				return;
			}
		}
		this.selectedChannel = selectedChannel;

		// Remove no selected message.
		this.noSelectedMessageLb.setVisible(false);

		// Initialize view.
		this.categoryChannelTitleLb.setText(selectedChannel.getName());
		this.unsubscribeBt.setVisible(true);

		this.mainViewScene.loadItemsByChannel(this.selectedChannel.getChannelId());
	}

	@FXML
	private void handleAddFeed() {
		this.mainViewScene.launchAddFeedView();
	}

	/**
	 * Handles action when user refresh items.
	 */
	@FXML
	private void handleRefresh() {
		switch (LIST_ITEM_TYPE) {
		case Globals.LIST_ALL_ITEMS_TYPE:
			this.mainViewScene.loadAllItems();
			break;
		case Globals.LIST_STARRED_ITEMS_TYPE:
			this.mainViewScene.loadStarredItems();
			break;
		case Globals.LIST_CATEGORY_ITEMS_TYPE:
			break;
		case Globals.LIST_CHANNEL_ITEMS_TYPE:
			this.mainViewScene.loadItemsByChannel(this.selectedChannel.getChannelId());
			break;
		}
	}

	/**
	 * Handles action when user unsubscribe to the channel.
	 */
	@FXML
	private void handleUnsubscribeChannel() {
		if (this.selectedChannel == null) {
			UiUtils.showErrorDialog(MainApp.getStage(), Globals.ERROR_DELETE_FEED, Globals.ERROR_DELETE_FEED,
					"Any channel is selected");
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Unsubscribe to the channel: " + this.selectedChannel.getName());
		alert.setHeaderText("Unsubscribe to the channel: " + this.selectedChannel.getName());
		alert.setContentText("Do you really want to unsubscribe to the channel " + this.selectedChannel.getName());

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			this.mainViewScene.deleteFeed(this.selectedChannel.getChannelId());
		}
	}

	//
	//
	// Dialog methods.
	//
	//
	/**
	 * Shows a loading dialog.
	 */
	public void showLoading() {
		this.loadingStage = null;
		this.loadingStage = UiUtils.createLoadingDialog(MainApp.getStage());
		if (this.loadingStage != null) {
			this.loadingStage.show();
		}
	}

	/**
	 * Stops the loading dialog.
	 */
	public void stopLoading() {
		if (this.loadingStage != null && this.loadingStage.isShowing()) {
			this.loadingStage.close();
		}
	}
}
