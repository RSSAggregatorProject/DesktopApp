package com.rssaggregator.desktop;

import java.io.IOException;
import java.util.ArrayList;

import com.rssaggregator.desktop.model.TmpArticle;
import com.rssaggregator.desktop.model.TmpCategory;
import com.rssaggregator.desktop.model.TmpChannel;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.view.MainViewController;

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

	private Stage primaryStage;
	private BorderPane rootView;

	private MainViewController mainViewController;

	private ObservableList<TmpCategory> categories;

	public MainViewScene() {
		this.primaryStage = MainApp.getStage();
	}

	/**
	 * Launches the Main View by initializing the root layout and the main view.
	 */
	public void launchMainView() {
		createCategoryArray();
		initRootLayout();
		initMainView();
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
			this.mainViewController.setCategories(this.categories);
			this.mainViewController.setMainViewScene(this);
			this.mainViewController.initCategoriesView();

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

	private void createCategoryArray() {
		this.categories = FXCollections.observableArrayList();
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

		this.categories.addAll(category1, category2, category3);
	}
}
