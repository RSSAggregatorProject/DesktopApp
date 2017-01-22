package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.model.TmpCategory;
import com.rssaggregator.desktop.model.TmpChannel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RowCategoryController {

	@FXML
	private Label nameLb;
	@FXML
	private Label unreadArticlesLb;

	private TmpCategory category;

	public RowCategoryController(TmpCategory category) {
		this.category = category;
	}

	@FXML
	private void initialize() {
		this.nameLb.setText(this.category.getName());
		int unreadArticles = getUnreadArticles(this.category);
		this.unreadArticlesLb.setText(String.valueOf(unreadArticles));
	}

	@FXML
	private void handleOnClickCategory() {
		System.out.println("Category: " + this.category.getName());
	}

	private int getUnreadArticles(TmpCategory category) {
		int count = 0;

		if (category.getChannels() != null) {
			for (TmpChannel channel : category.getChannels()) {
				count += channel.getUnreadArticles();
			}
		}
		return count;
	}
}
