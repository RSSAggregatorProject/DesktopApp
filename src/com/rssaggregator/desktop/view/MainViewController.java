package com.rssaggregator.desktop.view;

import java.io.IOException;
import java.util.ArrayList;

import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.MainViewScene;
import com.rssaggregator.desktop.model.TmpArticle;
import com.rssaggregator.desktop.model.TmpCategory;
import com.rssaggregator.desktop.model.TmpChannel;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.PreferencesUtils;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller View of the Main View.
 * 
 * @author Irina
 *
 */
public class MainViewController {

	private ObservableList<TmpCategory> categories;
	private VBox channelsBox;

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

	private FXMLLoader loader;
	private MainViewScene scene;

	@FXML
	Label pseudoLb;
	@FXML
	ListView<TmpArticle> articlesLv;
	@FXML
	RadioButton simpleList;
	@FXML
	RadioButton expandableList;

	private ObservableList<TmpArticle> articlesObservableList;

	public MainViewController() {
		this.loader = new FXMLLoader();
		articlesObservableList = FXCollections.observableArrayList();

		TmpArticle article = new TmpArticle();
		TmpArticle article2 = new TmpArticle();
		article.setTitle("Titre 1");
		article2.setTitle("Title 2");
		articlesObservableList.add(article);
		articlesObservableList.add(article2);
	}

	public void setCategories(ObservableList<TmpCategory> categories) {
		this.categories = categories;
	}

	public void setMainViewScene(MainViewScene scene) {
		this.scene = scene;
	}

	@FXML
	private void initialize() {
		final ToggleGroup group = new ToggleGroup();
		this.simpleList.setToggleGroup(group);
		this.simpleList.setUserData(0);
		this.expandableList.setToggleGroup(group);
		this.expandableList.setUserData(1);
		this.simpleList.setSelected(true);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (group.getSelectedToggle() != null) {
					if ((int) new_toggle.getUserData() == 0) {
						test();
					} else {
						test2();
					}
				}
			}
		});

		pseudoLb.setText(PreferencesUtils.getUserEmail());
		this.articlesLv.setItems(articlesObservableList);
		this.articlesLv.setCellFactory(articlesLv -> new ArticleListViewCell(articlesLv.getWidth()));
		articlesLv.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				System.out.println("Clicked on " + articlesLv.getSelectionModel().getSelectedItem().getTitle());
			}
		});
	}

	/**
	 * Initializes the categories view (TitledPanes).
	 */
	public void initCategoriesView() {
		initTitledPanes();
		for (TmpCategory category : this.categories) {
			initCategories(category);
		}
	}

	/**
	 * Initializes the All Items and Starred Items categories.
	 */
	private void initTitledPanes() {
		this.starredItemsTp.setExpanded(false);
		this.starredItemsTp.setCollapsible(false);
		this.unreadStarredItemsLb.setText("0");
		this.allItemsTp.setExpanded(false);
		this.allItemsTp.setCollapsible(false);
		this.unreadAllItemsLb.setText("23");
	}

	/**
	 * Initializes one category view by adding channels in the titled pane.
	 * 
	 * @param category
	 *            Category to initialize
	 */
	private void initCategories(TmpCategory category) {
		this.channelsBox = new VBox(0);
		this.channelsBox.setPadding(Insets.EMPTY);

		if (category.getChannels() != null) {
			for (TmpChannel channel : category.getChannels()) {
				addChannel(channel);
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
	private void addChannel(TmpChannel channel) {
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

	private void test() {
		this.articlesLv.setCellFactory(articlesLv -> new ArticleListViewCell(articlesLv.getWidth()));
	}

	private void test2() {
		this.articlesLv.setCellFactory(articlesLv -> new ArticleExtendedListViewCell(articlesLv.getWidth()));
	}

	@FXML
	private void handleStarredItemsPaneClicked(MouseEvent event) {
		System.out.println("Starred Items");
		this.articlesObservableList.clear();

		TmpArticle article = new TmpArticle();
		TmpArticle article2 = new TmpArticle();
		article.setTitle("Titre 3");
		article2.setTitle("Title 4");
		articlesObservableList.add(article);
		articlesObservableList.add(article2);

		this.articlesLv.setItems(articlesObservableList);
	}

	@FXML
	private void handleAllItemPaneClicked(MouseEvent event) {
		System.out.println("All Items");

		this.articlesObservableList.clear();

		TmpArticle article = new TmpArticle();
		TmpArticle article2 = new TmpArticle();
		article.setTitle("Titre 1");
		article2.setTitle("Title 2");
		articlesObservableList.add(article);
		articlesObservableList.add(article2);

		this.articlesLv.setItems(articlesObservableList);
	}

	@FXML
	private void handleAddFeed() {
		this.scene.launchAddFeedView();
	}

	public void updateNewCategory(TmpCategory category) {
		initCategories(category);
	}

	public void updateNewChannel(TmpCategory category, TmpChannel channel) {
		for (int i = 0; i < this.categories.size(); i++) {
			if (this.categories.get(i).getName().equals(category.getName())) {
				ArrayList<TmpChannel> newChannels = this.categories.get(i).getChannels();
				if (newChannels == null) {
					newChannels = new ArrayList<>();
				}
				newChannels.add(channel);
				this.categories.get(i).setChannels(newChannels);
				System.out.println("SIZE: " + this.categories.size());
				this.categoriesAc.getPanes().clear();
				initCategoriesView();
				return;
			}
		}
	}
}
