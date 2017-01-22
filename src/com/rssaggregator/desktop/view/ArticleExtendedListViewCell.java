package com.rssaggregator.desktop.view;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.rssaggregator.desktop.model.TmpArticle;
import com.rssaggregator.desktop.utils.Globals;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ArticleExtendedListViewCell extends ListCell<TmpArticle> {

	private TmpArticle article;
	private String channelName;
	private boolean isRead;

	@FXML
	private AnchorPane rootView;
	@FXML
	private Label titleLb;
	@FXML
	private Label pubDateLb;
	@FXML
	private Hyperlink linkHl;
	@FXML
	private JFXButton starBt;
	@FXML
	private JFXToggleButton readTb;
	@FXML
	private ImageView pictureIv;
	@FXML
	private Label descriptionLb;

	private FXMLLoader loader;
	private double parentWidth;

	public ArticleExtendedListViewCell(double parentWidth, String channelName) {
		this.parentWidth = parentWidth;
		this.channelName = channelName;
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
				loader = new FXMLLoader(getClass().getResource(Globals.ROW_ARTICLE_EXTENDED_VIEW));
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
			this.descriptionLb.setText("Description de la night");

			this.readTb.setSelected(this.isRead);

			setText(null);
			setGraphic(rootView);
		}
	}

	@FXML
	private void handleReadArticle() {
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
