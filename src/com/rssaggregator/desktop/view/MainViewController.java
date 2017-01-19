package com.rssaggregator.desktop.view;

import java.io.IOException;
import java.util.ArrayList;

import com.rssaggregator.desktop.MainApp;
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
	private TitledPane starredItemsTp;
	@FXML
	private Label unreadStarredItemsLb;
	@FXML
	private TitledPane allItemsTp;
	@FXML
	private Label unreadAllItemsLb;

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

	public void initCategoriesView() {
		initTitledPanes();
		for (TmpCategory category : this.categories) {
			initCategories(category);
		}
	}

	private void initTitledPanes() {
		this.starredItemsTp.setExpanded(false);
		this.starredItemsTp.setCollapsible(false);
		this.unreadStarredItemsLb.setText("0");
		this.allItemsTp.setExpanded(false);
		this.allItemsTp.setCollapsible(false);
		this.unreadAllItemsLb.setText("23");
	}

	private void initCategories(TmpCategory category) {
		this.channelsBox = new VBox(16);
		this.channelsBox.setPadding(Insets.EMPTY);

		for (TmpChannel channel : category.getChannels()) {
			addChannel(channel);
		}
		TitledPane newTitledPane = new TitledPane(category.getName(), channelsBox);
		Platform.runLater(() -> {
			newTitledPane.lookup(".arrow").setVisible(false);
			Pane title = (Pane) newTitledPane.lookup(".title");
			title.setPrefHeight(allItemsTp.getHeight());
			title.setPadding(new Insets(0, 0, 10, -6));
		});
		this.categoriesAc.getPanes().add(newTitledPane);
	}

	private void addChannel(TmpChannel channel) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.ROW_CHANNEL_VIEW));
			loader.setController(new RowChannelController(channel.getName(), channel.getUnreadArticles()));
			AnchorPane smallPane = (AnchorPane) loader.load();

			channelsBox.getChildren().add(smallPane);
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
		System.out.println("Add feed");
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Dialog_AddFeed.fxml"));
			AnchorPane rootView = (AnchorPane) loader.load();

			Scene scene = new Scene(rootView);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setResizable(false);

			AddFeedController controller = loader.getController();
			controller.setStage(stage);

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
