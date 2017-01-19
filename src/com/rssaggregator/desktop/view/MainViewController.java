package com.rssaggregator.desktop.view;

import java.io.IOException;

import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.model.TmpArticle;
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

public class MainViewController {

	@FXML
	Label pseudoLb;
	@FXML
	ListView<TmpArticle> articlesLv;
	@FXML
	RadioButton simpleList;
	@FXML
	RadioButton expandableList;
	@FXML
	Accordion categories;
	@FXML
	TitledPane starredItems;
	@FXML
	TitledPane allItems;

	private ObservableList<TmpArticle> articlesObservableList;
	private VBox box;

	public MainViewController() {
		articlesObservableList = FXCollections.observableArrayList();

		TmpArticle article = new TmpArticle();
		TmpArticle article2 = new TmpArticle();
		article.setTitle("Titre 1");
		article2.setTitle("Title 2");
		articlesObservableList.add(article);
		articlesObservableList.add(article2);
	}

	@FXML
	private void initialize() {
		initCategories();

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

	private void test() {
		this.articlesLv.setCellFactory(articlesLv -> new ArticleListViewCell(articlesLv.getWidth()));
	}

	private void test2() {
		this.articlesLv.setCellFactory(articlesLv -> new ArticleExtendedListViewCell(articlesLv.getWidth()));
	}

	private void initCategories() {
		starredItems.setExpanded(false);
		starredItems.setCollapsible(false);
		allItems.setExpanded(false);
		allItems.setCollapsible(false);
		box = new VBox(8);
		box.setPadding(Insets.EMPTY);
		addChannels("Turbo", 10);
		addChannels("Caradisiac", 20);
		TitledPane category = new TitledPane("Automobile", box);
		Platform.runLater(() -> {
			category.lookup(".arrow").setVisible(false);
			Pane title = (Pane) category.lookup(".title");
			title.setPrefHeight(allItems.getHeight());
			title.setPadding(new Insets(0, 0, 10, -6));
		});
		categories.getPanes().add(category);
	}

	private void addChannels(String channelName, int unreadArticles) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Row_Channel.fxml"));
			loader.setController(new RowChannelController(channelName, unreadArticles));
			AnchorPane smallPane = (AnchorPane) loader.load();

			box.getChildren().add(smallPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
