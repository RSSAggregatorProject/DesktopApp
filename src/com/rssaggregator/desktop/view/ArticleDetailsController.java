package com.rssaggregator.desktop.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.rssaggregator.desktop.model.TmpArticle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ArticleDetailsController {

	private TmpArticle article;

	@FXML
	private Label titleLb;
	@FXML
	private Label channelNameLb;
	@FXML
	private Label pubDateLb;
	@FXML
	private ImageView pictureIg;
	@FXML
	private JFXButton starBt;
	@FXML
	private JFXToggleButton readTg;
	@FXML
	private Label descriptionLb;

	public ArticleDetailsController(TmpArticle article) {
		this.article = article;
	}

	@FXML
	private void initialize() {
	}

}
