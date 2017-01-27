package com.rssaggregator.desktop.view;

import java.util.Optional;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.rssaggregator.desktop.AddFeedScene;
import com.rssaggregator.desktop.model.Category;
import com.rssaggregator.desktop.model.CategoryAddedWrapper;
import com.rssaggregator.desktop.model.FeedAddedWrapper;
import com.rssaggregator.desktop.utils.CategoriesUtils;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.UiUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

/**
 * Controller for the Add Feed view.
 * 
 * @author Irina
 *
 */
public class AddFeedController {

	// Data
	private ObservableList<Category> categories = FXCollections.observableArrayList();
	private ObservableList<String> categoriesStr = FXCollections.observableArrayList();

	@FXML
	private JFXTextField rssLinkTf;
	@FXML
	private JFXComboBox<String> categoriesCb;

	// Controller
	private MainViewController mainViewController;

	// Views
	private AddFeedScene addFeedScene;
	private Stage addFeedStage;
	private Stage loadingStage;

	/**
	 * Sets some data.
	 * 
	 * @param addFeedScene
	 * @param addFeedStage
	 */
	public void setData(AddFeedScene addFeedScene, Stage addFeedStage) {
		this.addFeedScene = addFeedScene;
		this.addFeedStage = addFeedStage;
	}

	/**
	 * Sets categories and spinner data.
	 * 
	 * @param categories
	 */
	public void setCategories(ObservableList<Category> categories) {
		this.categories = categories;
		if (this.categories != null) {
			for (Category category : this.categories) {
				this.categoriesStr.add(category.getName());
			}
		}
		this.categoriesCb.setItems(this.categoriesStr);
	}

	/**
	 * Sets the Main View Controller.
	 * 
	 * @param mainViewController
	 */
	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}

	@FXML
	private void initialize() {
	}

	/**
	 * Handles action when user adds a feed.
	 */
	@FXML
	private void handleAddFeed() {
		String rssLink = this.rssLinkTf.getText().toString();
		String categoryStr = this.categoriesCb.getValue();

		if (rssLink.length() == 0) {
			UiUtils.showErrorDialog(this.addFeedStage, Globals.ERROR_INVALID_INPUTS, Globals.ERROR_INVALID_INPUTS,
					Globals.ERROR_RSS_LINK_FIELD_INVALID);
			return;
		}

		if (categoryStr == null || categoryStr.length() == 0) {
			UiUtils.showErrorDialog(this.addFeedStage, Globals.ERROR_INVALID_INPUTS, Globals.ERROR_INVALID_INPUTS,
					"Please select a category or create a new one!");
			return;
		}
		Category selectedCategory = CategoriesUtils.getCategoryByName(this.categories, categoryStr);

		if (selectedCategory == null) {
			UiUtils.showErrorDialog(this.addFeedStage, Globals.ERROR_INVALID_INPUTS, Globals.ERROR_INVALID_INPUTS,
					"Please select a valid category!");
			return;
		}

		this.addFeedScene.addFeed(selectedCategory.getCategoryId(), rssLink);
	}

	/**
	 * Handles action on Add Category button.
	 */
	@FXML
	private void handleAddCategory() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(this.addFeedStage);
		dialog.setHeaderText(Globals.CREATE_CATEGORY_TITLE_NAME);
		dialog.setTitle(Globals.CREATE_CATEGORY_TITLE_NAME);
		dialog.setContentText(Globals.ENTER_CATEGORY_NAME);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String newCategoryName = result.get();
			if (newCategoryName.length() < 2) {
				UiUtils.showErrorDialog(this.addFeedStage, Globals.ERROR_INVALID_INPUTS, Globals.ERROR_INVALID_INPUTS,
						Globals.ERROR_CATEGORY_FIELD_INVALID);
				return;
			} else {

				if (CategoriesUtils.isAlreadyCreated(this.categories, newCategoryName)) {
					UiUtils.showErrorDialog(this.addFeedStage, Globals.ERROR_INVALID_INPUTS,
							Globals.ERROR_INVALID_INPUTS, Globals.ERROR_CATEGORY_ALREADY_EXISTS);
					return;
				}
				this.addFeedScene.addCategory(newCategoryName);
			}
		}
	}

	/**
	 * Updates the view if adding a category succeed.
	 * 
	 * @param data
	 */
	public void updateCategoryAdded(CategoryAddedWrapper data) {
		this.categoriesStr.add(data.getName());
		this.categoriesCb.setItems(this.categoriesStr);

		Category newCategory = new Category();
		newCategory.setName(data.getName());
		newCategory.setCategoryId(data.getCategoryId());
		this.categories.add(newCategory);
		this.mainViewController.updateNewCategory();
	}

	/**
	 * Updates the view if adding a feed succeed.
	 * 
	 * @param data
	 */
	public void updateFeedAdded(FeedAddedWrapper data) {
		this.mainViewController.updateNewChannel();
		this.addFeedScene.closeStage();
	}

	/**
	 * Handles action on Close Dialog Button.
	 */
	@FXML
	private void handleCloseDialog() {
		this.addFeedScene.closeStage();
	}

	/**
	 * Shows a loading dialog.
	 */
	public void showLoading() {
		this.loadingStage = null;
		this.loadingStage = UiUtils.createLoadingDialog(this.addFeedStage);
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
