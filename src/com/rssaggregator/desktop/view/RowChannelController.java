package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.model.TmpChannel;

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

	private TmpChannel channel;

	public RowChannelController(TmpChannel channel) {
		this.channel = channel;
	}

	@FXML
	private void initialize() {
		this.nameLb.setText(this.channel.getName());
		this.unreadArticlesLb.setText(String.valueOf(this.channel.getUnreadArticles()));
	}

	@FXML
	private void handleRowClicked() {
		System.out.println("Channel: " + this.channel.getName());
	}
}
