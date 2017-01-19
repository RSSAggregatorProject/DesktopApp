package com.rssaggregator.desktop.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddFeedController {

	@FXML
	private TextField rssLinkTf;
	@FXML
	private ComboBox<String> categoriesCb;

	private Stage stage;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setMenuItems() {
	}

	@FXML
	private void initialize() {
		ObservableList<String> items = FXCollections.observableArrayList();
		items.add("Automobile");
		items.add("Sport");
		this.categoriesCb.setItems(items);
	}

	@FXML
	private void handleAddCategory() {
	}

	@FXML
	private void handleAddFeed() {
		System.out.println("CATEGORY: " + this.categoriesCb.getValue());
	}

	@FXML
	private void handleChangeCategory() {
	}

	@FXML
	private void handleCloseDialog() {
		this.stage.close();
	}

}
