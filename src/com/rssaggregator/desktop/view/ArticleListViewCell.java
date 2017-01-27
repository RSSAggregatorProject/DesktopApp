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

public class ArticleListViewCell extends ListCell<Item> {

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

	// Views
	private FXMLLoader loader;
	private ListView<Item> itemsLv;

	// Data
	private Item item;

	private boolean isRead;

	public ArticleListViewCell(ListView<Item> itemsLv) {
		this.itemsLv = itemsLv;
		this.isRead = false;
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
				loader = new FXMLLoader(getClass().getResource(Globals.ROW_ARTICLE_VIEW));
				loader.setController(this);

				try {
					loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String title = this.item.getTitle();
			Date pubDate = this.item.getPubDate();
			String channelName = this.item.getChannelName();
			String link = this.item.getLink();

			if (title != null) {
				this.titleLb.setText(title);
			}
			if (pubDate != null) {
				this.pubDateLb.setText(FormatterTime.formattedAsTimeAgo(pubDate));
			}

			this.rootView.prefWidthProperty().bind(itemsLv.widthProperty());
			if (this.item.getTitle() != null) {
				this.titleLb.setText(this.item.getTitle());
			}

			if (channelName != null) {
				this.linkHl.setText(channelName);
			}

			if (link != null) {
				this.linkHl.setOnAction(t -> {
					MainApp.getMainApp().getHostServices().showDocument(link);
				});
			}

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
