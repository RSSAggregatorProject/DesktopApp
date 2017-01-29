package com.rssaggregator.desktop.view;

import java.util.Date;

import com.jfoenix.controls.JFXToggleButton;
import com.rssaggregator.desktop.ArticleDetailsScene;
import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.model.Item;
import com.rssaggregator.desktop.utils.FormatterTime;
import com.rssaggregator.desktop.utils.UiUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * View Controller of the Article Details View.
 * 
 * @author Irina
 *
 */
public class ArticleDetailsController {

	// Views.
	private Stage loadingStage;

	@FXML
	private Label titleLb;
	@FXML
	private Label channelNameLb;
	@FXML
	private Label pubDateLb;
	@FXML
	private ImageView starIconIv;
	@FXML
	private Label starTextLb;
	@FXML
	private JFXToggleButton readTg;
	@FXML
	private WebView descriptionWv;

	private Item item;

	private MainViewController mainViewController;
	private ArticleDetailsScene articleDetailsScene;

	/**
	 * Constructor
	 * 
	 * @param item
	 *            Item
	 */
	public ArticleDetailsController(Item item) {
		this.item = item;
	}

	/**
	 * Sets the data
	 * 
	 * @param articleDetailsScene
	 *            ArticleDetailsScene
	 * @param mainViewController
	 *            MainViewController
	 */
	public void setData(ArticleDetailsScene articleDetailsScene, MainViewController mainViewController) {
		this.articleDetailsScene = articleDetailsScene;
		this.mainViewController = mainViewController;
	}

	@FXML
	private void initialize() {
		final WebEngine webEngine = descriptionWv.getEngine();

		String title = this.item.getTitle();
		String channelName = this.item.getChannelName();
		Date pubDate = this.item.getPubDate();
		String description = this.item.getDescription();

		if (title != null) {
			this.titleLb.setText(title);
		}

		if (channelName != null) {
			this.channelNameLb.setText(channelName);
		}

		if (pubDate != null) {
			this.pubDateLb.setText(FormatterTime.formattedAsTimeAgo(pubDate));
		}

		if (description != null) {
			webEngine.loadContent(description);
		}

		if (this.item.isRead()) {
			this.readTg.setSelected(true);
			this.readTg.setText("Read");
		} else {
			this.readTg.setSelected(false);
			this.readTg.setText("Unread");
		}

		this.readTg.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (readTg.isSelected()) {
					articleDetailsScene.changeReadState(true);
				} else {
					articleDetailsScene.changeReadState(false);
				}
			}
		});

		Image image;
		if (!this.item.isStarred()) {
			image = new Image("file:resources/images/ic_star_border.png");
			this.item.setStarred(false);
			this.starTextLb.setText("Star the article");
			this.starIconIv.setImage(image);
		} else {
			image = new Image("file:resources/images/ic_star.png");
			this.starTextLb.setText("Unstar the article");
			this.item.setStarred(true);
			this.starIconIv.setImage(image);
		}
	}

	/**
	 * Handles action: Start browser.
	 */
	@FXML
	private void handleVisitWebsite() {
		String link = this.item.getLink();
		if (link != null) {
			MainApp.getMainApp().getHostServices().showDocument(link);
		}
	}

	@FXML
	private void handleStarItem() {
		if (this.item.isStarred()) {
			this.articleDetailsScene.changeStarState(false);
		} else {
			this.articleDetailsScene.changeStarState(true);
		}
	}

	//
	//
	// Update view after API calls.
	//
	//
	/**
	 * Updates view when the user reads or unreads the item.
	 */
	public void updateRead() {
		if (this.item.isRead()) {
			this.item.setRead(false);
			this.readTg.setSelected(false);
			this.readTg.setText("Unread");
		} else {
			this.item.setRead(true);
			this.readTg.setSelected(true);
			this.readTg.setText("Read");
		}
		this.mainViewController.updateStateSingleItem(this.item, "UNREAD");
	}

	/**
	 * Updates view when the user stars or unstars the item.
	 */
	public void updateStar() {
		Image image;
		if (this.item.isStarred()) {
			image = new Image("file:resources/images/ic_star_border.png");
			this.item.setStarred(false);
			this.starTextLb.setText("Star the article");
			this.starIconIv.setImage(image);
		} else {
			image = new Image("file:resources/images/ic_star.png");
			this.starTextLb.setText("Unstar the article");
			this.item.setStarred(true);
			this.starIconIv.setImage(image);
		}
		this.mainViewController.updateStateSingleItem(this.item, "STAR");
	}

	/**
	 * Update view in case of read error.
	 */
	public void updateReadError() {
		if (this.readTg.isSelected()) {
			this.readTg.setSelected(false);
		} else {
			this.readTg.setSelected(true);
		}
	}

	public void updateStarError() {

	}

	/**
	 * Shows a loading dialog.
	 */
	public void showLoading() {
		this.loadingStage = null;
		this.loadingStage = UiUtils.createLoadingDialog(MainApp.getStage());
		if (this.loadingStage != null) {
			this.loadingStage.show();
		}
	}

	/**
	 * Stops the loading dialog.
	 */
	public void stopLoading() {
		if (this.loadingStage != null && this.loadingStage.isShowing()) {
			this.loadingStage.close();
		}
	}
}
