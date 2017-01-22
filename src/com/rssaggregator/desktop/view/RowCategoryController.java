package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.utils.CategoriesUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for a Category row view (TitledPane on the left).
 * 
 * @author Irina
 *
 */
public class RowCategoryController {

	// Data
	private Category category;

	@FXML
	private Label nameLb;
	@FXML
	private Label unreadArticlesLb;

	public RowCategoryController(Category category) {
		this.category = category;
	}

	@FXML
	private void initialize() {
		if (this.category != null) {
			this.nameLb.setText(this.category.getName());
			// TODO Can be replace by category.getUnread()
			int unreadArticles = CategoriesUtils.getUnreadArticles(this.category);
			this.unreadArticlesLb.setText(String.valueOf(unreadArticles));
		}
	}

	/**
	 * Handles a click when the user selects a category.
	 */
	@FXML
	private void handleOnClickCategory() {
		System.out.println("Handle On Click Categories");
	}
}
