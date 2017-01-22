package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.model.TmpArticle;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.view.ArticleDetailsController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ArticleDetailsScene {

	private AnchorPane rootView;
	private Stage stage;

	private TmpArticle article;

	public ArticleDetailsScene(TmpArticle article) {
		this.article = article;
	}

	public void launchArticleDetailsView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.ARTICLE_DETAILS_VIEW));

			ArticleDetailsController articleDetailsController = new ArticleDetailsController(this.article);
			loader.setController(articleDetailsController);

			this.rootView = (AnchorPane) loader.load();

			Scene scene = new Scene(this.rootView);

			this.stage = new Stage();
			this.stage.initOwner(MainApp.getStage());
			this.stage.setTitle(Globals.ARTICLE_DETAILS_TITLE_NAME);
			this.stage.setResizable(false);
			this.stage.getIcons().add(new Image(Globals.RSS_LOGO_LINK));
			this.stage.setScene(scene);

			this.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
