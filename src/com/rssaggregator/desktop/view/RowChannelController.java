package com.rssaggregator.desktop.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class RowChannelController {

	@FXML
	private Label nameLb;
	@FXML
	private Label unreadArticlesLb;
	
	@FXML
	private AnchorPane pane;

	private String name;
	private int unreadArticles;

	public RowChannelController(String name, int unreadArticles) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.unreadArticles = unreadArticles;
	}

	@FXML
	private void initialize() {
		this.nameLb.setText(name);
		this.unreadArticlesLb.setText(String.valueOf(this.unreadArticles));
	}

	@FXML
	private void handleRowClicked() {
		System.out.println("Titre: " + this.name);
	}
}
