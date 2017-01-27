package com.rssaggregator.desktop.view;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.jfoenix.controls.JFXToggleButton;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.MainViewScene;
import com.rssaggregator.desktop.event.RefreshCategoriesEvent;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.Channel;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.model.TmpArticle;
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
	private VBox channelsBox;

	private Stage loadingStage;

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

	private FXMLLoader loader;
	private MainViewScene mainViewScene;

	@FXML
	ListView<TmpArticle> articlesLv;

	private EventBus eventBus;

	private ObservableList<Item> itemsList;

	private ObservableList<TmpArticle> articlesObservableList;

	public MainViewController() {
		this.loader = new FXMLLoader();
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

		this.articlesLv.setItems(articlesObservableList);
		this.articlesLv.setCellFactory(articlesLv -> new ArticleListViewCell(articlesLv.getWidth(), "Channel"));
		articlesLv.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				TmpArticle selectedArticle = articlesLv.getSelectionModel().getSelectedItem();

				if (selectedArticle != null) {
					mainViewScene.launchArticleDetailsView(selectedArticle);
				}
			}
		});
	}

	private void initExpandableToggleButton() {
		this.expandableListTb.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (expandableListTb.isSelected()) {
					articlesLv.setCellFactory(
							articlesLv -> new ArticleExtendedListViewCell(articlesLv.getWidth(), "Channel"));
				} else {
					articlesLv.setCellFactory(articlesLv -> new ArticleListViewCell(articlesLv.getWidth(), "Channel"));
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
			this.loader.setController(new RowCategoryController(category));
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
			this.loader.setController(new RowChannelController(channel));
			AnchorPane channelPane = (AnchorPane) this.loader.load();

			this.channelsBox.getChildren().add(channelPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void handleAllItemPaneClicked(MouseEvent event) {
		System.out.println("All Items");

		// Remove no selected message.
		this.noSelectedMessageLb.setVisible(false);

		// Initialize view.
		this.categoryChannelTitleLb.setText(Globals.ALL_ITEMS_TITLED_PANE);

		this.mainViewScene.loadAllItems();
	}

	@FXML
	private void handleAddFeed() {
		this.mainViewScene.launchAddFeedView();
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

	@FXML
	private void handleRefresh() {
		TmpArticle article = new TmpArticle();
		article.setTitle("New Article");
		this.articlesObservableList.add(article);
	}

	/*
	 * HANDLE METHODS
	 */
	/**
	 * Handles click on the Starred Items category.
	 */
	@FXML
	private void handleClickStarredItems() {
		// Remove no selected message.
		this.noSelectedMessageLb.setVisible(false);

		// Initialize right view.
		this.categoryChannelTitleLb.setText(Globals.STARRED_ITEMS_TITLED_PANE);

		// Load the data

		this.articlesObservableList.clear();

		TmpArticle article = new TmpArticle();
		TmpArticle article2 = new TmpArticle();
		article.setTitle("Titre 3");
		article2.setTitle("Title 4");
		articlesObservableList.add(article);
		articlesObservableList.add(article2);

		this.articlesLv.setItems(articlesObservableList);
	}

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
