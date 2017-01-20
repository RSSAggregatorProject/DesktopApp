package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.model.TmpCategory;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.view.AddFeedController;
import com.rssaggregator.desktop.view.MainViewController;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddFeedScene {

	private ObservableList<TmpCategory> categories;
	private Stage stage;
	private MainViewController mainViewController;

	public AddFeedScene(ObservableList<TmpCategory> categories, MainViewController mainViewController) {
		this.categories = categories;
		this.mainViewController = mainViewController;
	}

	public void launchAddFeedView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.DIALOG_ADD_FEED_VIEW));
			AnchorPane rootView = (AnchorPane) loader.load();

			this.stage = new Stage();
			Scene scene = new Scene(rootView);
			this.stage.setTitle(Globals.ADD_FEED_TITLE_NAME);
			this.stage.initOwner(MainApp.getStage());
			this.stage.setResizable(false);
			this.stage.getIcons().add(new Image(Globals.RSS_LOGO_LINK));
			this.stage.setScene(scene);

			AddFeedController controller = loader.getController();
			controller.setStage(this.stage);
			controller.setMainViewController(this.mainViewController);
			controller.setCategories(this.categories);

			this.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
