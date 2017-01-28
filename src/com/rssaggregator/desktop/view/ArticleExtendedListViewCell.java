package com.rssaggregator.desktop.view;

import java.io.IOException;
import java.util.Date;

import com.jfoenix.controls.JFXButton;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.utils.FormatterTime;
import com.rssaggregator.desktop.utils.Globals;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ArticleExtendedListViewCell extends ListCell<Item> {

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
	private Label descriptionLb;

	private FXMLLoader loader;
	private ListView<Item> itemsLv;

	// Data
	private Item item;

	public ArticleExtendedListViewCell(ListView<Item> itemsLv) {
		this.itemsLv = itemsLv;
	}

	@Override
	protected void updateItem(Item item, boolean empty) {
		super.updateItem(item, empty);
		this.item = item;

		if (empty || item == null) {
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
			String title = this.item.getTitle();
			Date pubDate = this.item.getPubDate();
			String link = this.item.getLink();
			String channelName = this.item.getChannelName();
			String description = this.item.getDescription();

			if (title != null) {
				this.titleLb.setText(title);
			}

			if (pubDate != null) {
				this.pubDateLb.setText(FormatterTime.formattedAsTimeAgo(pubDate));
			}

			if (channelName != null) {
				this.linkHl.setText(channelName);
			}

			if (link != null) {
				this.linkHl.setOnAction(t -> {
					MainApp.getMainApp().getHostServices().showDocument(link);
				});
			}

			if (description != null) {
				this.descriptionLb.setText(description);
			}

			this.rootView.prefWidthProperty().bind(this.itemsLv.widthProperty());

			if (this.item.isRead()) {
				this.titleLb.setTextFill(Color.rgb(117, 117, 117));
			} else {
				this.titleLb.setTextFill(Color.rgb(33, 33, 33));
			}

			ImageView image;
			if (this.item.isStarred()) {
				image = new ImageView("file:resources/images/ic_star.png");
			} else {
				image = new ImageView("file:resources/images/ic_star_border.png");
			}
			image.setFitWidth(30);
			image.setFitHeight(30);
			this.starBt.setGraphic(image);

			setText(null);
			setGraphic(rootView);
		}
	}

	@FXML
	private void handleReadArticle() {
	}

	@FXML
	private void handleStarredArticle() {
	}

}
