package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.utils.PreferencesUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainViewController {

	@FXML
	Label pseudoLb;

	@FXML
	private void initialize() {
		pseudoLb.setText(PreferencesUtils.getUserEmail());
	}
}
