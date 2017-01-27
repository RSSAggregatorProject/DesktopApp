package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.model.Category;

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

	// Controller
	private MainViewController mainViewController;

	public RowCategoryController(Category category, MainViewController mainViewController) {
		this.category = category;
		this.mainViewController = mainViewController;
	}

	@FXML
	private void initialize() {
		if (this.category != null) {
			this.nameLb.setText(this.category.getName());
			// TODO Fix this when read state works.
			if (this.category.getUnread() != null) {
				this.unreadArticlesLb.setText(String.valueOf(this.category.getUnread()));
			} else {
				this.unreadArticlesLb.setText("0");
			}
		}
	}

	/**
	 * Handles a click when the user selects a category.
	 */
	@FXML
	private void handleCategoryClicked() {
		this.mainViewController.handleCategoryItemsPaneClicked(category);
	}
}
