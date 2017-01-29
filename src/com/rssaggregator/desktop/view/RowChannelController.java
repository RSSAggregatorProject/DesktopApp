package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.model.Channel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Controller view of a row of channel pane.
 * 
 * @author Irina
 *
 */
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

	private MainViewController mainViewController;

	/**
	 * Constructor
	 * 
	 * @param channel
	 *            Channel
	 * @param mainViewController
	 *            MainViewController.
	 */
	public RowChannelController(Channel channel, MainViewController mainViewController) {
		this.channel = channel;
		this.mainViewController = mainViewController;
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
		}
	}

	@FXML
	private void handleRowClicked() {
		this.mainViewController.handleChannelItemsPaneClicked(this.channel);
	}
}
