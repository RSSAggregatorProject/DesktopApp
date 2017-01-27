package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.model.Channel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class RowChannelController {

	// Data
	private Channel channel;

	@FXML
	private AnchorPane rootView;
	@FXML
	private Label nameLb;
	@FXML
	private Label unreadArticlesLb;
	@FXML
	private ImageView iconIg;

	public RowChannelController(Channel channel) {
		this.channel = channel;
	}

	@FXML
	private void initialize() {
		if (this.channel != null) {
			this.nameLb.setText(this.channel.getName());

			if (this.channel.getUnread() != null) {
				this.unreadArticlesLb.setText(String.valueOf(this.channel.getUnread()));
			} else {
				this.unreadArticlesLb.setText("0");
			}

			// TODO Fix that (Image not working with web url)
			if (this.channel.getFaviconUri() != null
					&& !this.channel.getFaviconUri().equals("favicon_not_implemented")) {
				// TODO Implement Favicon when working.
			}
		}
	}

	@FXML
	private void handleRowClicked() {
		System.out.println("Handle Row Clicked");
	}
}
