package com.rssaggregator.desktop.view;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.model.TmpArticle;
import com.rssaggregator.desktop.utils.Globals;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ArticleListViewCell extends ListCell<TmpArticle> {

	@FXML
	AnchorPane rootView;
	@FXML
	private JFXButton starBt;
	@FXML
	private Label titleLb;
	@FXML
	private Label pubDateLb;
	@FXML
	private Hyperlink linkHl;

	private FXMLLoader loader;
	private double parentWidth;

	private TmpArticle article;
	private String categoryName;
	private boolean isRead;

	public ArticleListViewCell(double parentWidth, String categoryName) {
		this.parentWidth = parentWidth;
		this.categoryName = categoryName;
		this.isRead = false;
	}

	@Override
	protected void updateItem(TmpArticle article, boolean empty) {
		super.updateItem(article, empty);
		this.article = article;

		if (empty || article == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (loader == null) {
				loader = new FXMLLoader(getClass().getResource(Globals.ROW_ARTICLE_VIEW));
				loader.setController(this);

				try {
					loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			this.rootView.setPrefWidth(this.parentWidth);
			this.titleLb.setText(this.article.getTitle());
			this.pubDateLb.setText("Il y a 30 mins.");
			this.linkHl.setText(categoryName);
			this.linkHl.setOnAction(t -> {
				MainApp.getMainApp().getHostServices().showDocument("https://www.google.com");
			});

			setText(null);
			setGraphic(this.rootView);
		}
	}

	@FXML
	private void handleStarredArticle() {
		ImageView image;
		if (!isRead) {
			image = new ImageView("file:resources/images/ic_star.png");
			this.isRead = true;
		} else {
			image = new ImageView("file:resources/images/ic_star_border.png");
			this.isRead = false;
		}
		image.setFitWidth(30);
		image.setFitHeight(30);
		this.starBt.setGraphic(image);
	}

}
