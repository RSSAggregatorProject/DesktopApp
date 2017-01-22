package com.rssaggregator.desktop.view;

import java.util.Optional;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.rssaggregator.desktop.model.TmpCategory;
import com.rssaggregator.desktop.model.TmpChannel;
import com.rssaggregator.desktop.utils.Globals;
import com.rssaggregator.desktop.utils.UiUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class AddFeedController {

	private ObservableList<TmpCategory> categories;
	private ObservableList<String> categoriesStr = FXCollections.observableArrayList();

	@FXML
	private JFXTextField rssLinkTf;
	@FXML
	private JFXComboBox<String> categoriesCb;

	private MainViewController mainViewController;
	private Stage stage;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCategories(ObservableList<TmpCategory> categories) {
		this.categories = categories;
		for (TmpCategory category : this.categories) {
			this.categoriesStr.add(category.getName());
		}
		this.categoriesCb.setItems(this.categoriesStr);
	}

	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}

	@FXML
	private void initialize() {
	}

	@FXML
	private void handleAddFeed() {
		String rssLink = this.rssLinkTf.getText();
		String categoryStr = this.categoriesCb.getValue();

		if (rssLink.length() == 0) {
			UiUtils.showErrorDialog(this.stage, "Invalid Inputs", "The RSS Link Field is empty!");
			return;
		}

		if (categoryStr == null || categoryStr.length() == 0) {
			UiUtils.showErrorDialog(this.stage, "Invalid Inputs", "Please select a category or create a new one!");
			return;
		}

		TmpChannel newChannel = new TmpChannel();
		newChannel.setName(rssLink);

		this.mainViewController.updateNewChannel(searchCategoryByName(categoryStr), newChannel);
		if (this.stage.isShowing()) {
			this.stage.close();
		}
	}

	@FXML
	private void handleAddCategory() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.initOwner(this.stage);
		dialog.setHeaderText(Globals.CREATE_CATEGORY_TITLE_NAME);
		dialog.setTitle(Globals.CREATE_CATEGORY_TITLE_NAME);
		dialog.setContentText("Please enter a category name:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String newCategoryName = result.get();
			if (newCategoryName.length() < 2) {
				UiUtils.showErrorDialog(this.stage, "Invalid Inputs", "The Category Name field is invalid!");
			} else {

				if (isAlreadyCreated(newCategoryName)) {
					UiUtils.showErrorDialog(this.stage, "Invalid Inputs", "This category already exists!");
					return;
				}
				this.categoriesStr.add(newCategoryName);
				this.categoriesCb.setItems(categoriesStr);
				TmpCategory newCategory = new TmpCategory();
				newCategory.setName(newCategoryName);
				this.categories.add(newCategory);
				this.mainViewController.updateNewCategory(newCategory);
			}
		}
	}

	@FXML
	private void handleCloseDialog() {
		if (this.stage.isShowing()) {
			this.stage.close();
		}
	}

	private boolean isAlreadyCreated(String categoryName) {
		for (TmpCategory category : this.categories) {
			if (category.getName().equals(categoryName)) {
				return true;
			}
		}
		return false;
	}

	private TmpCategory searchCategoryByName(String categoryName) {
		for (TmpCategory category : this.categories) {
			if (category.getName().equals(categoryName)) {
				return category;
			}
		}
		return null;
	}
}
