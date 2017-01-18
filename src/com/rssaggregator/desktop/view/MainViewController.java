package com.rssaggregator.desktop.view;

import com.rssaggregator.desktop.model.TmpArticle;
import com.rssaggregator.desktop.utils.PreferencesUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;

public class MainViewController {

	@FXML
	Label pseudoLb;
	@FXML
	ListView<TmpArticle> articlesLv;
	@FXML
	RadioButton simpleList;
	@FXML
	RadioButton expandableList;

	private ObservableList<TmpArticle> articlesObservableList;

	public MainViewController() {
		articlesObservableList = FXCollections.observableArrayList();

		TmpArticle article = new TmpArticle();
		TmpArticle article2 = new TmpArticle();
		article.setTitle("Titre 1");
		article2.setTitle("Title 2");
		articlesObservableList.add(article);
		articlesObservableList.add(article2);
	}

	@FXML
	private void initialize() {
		final ToggleGroup group = new ToggleGroup();
		this.simpleList.setToggleGroup(group);
		this.simpleList.setUserData(0);
		this.expandableList.setToggleGroup(group);
		this.expandableList.setUserData(1);
		this.simpleList.setSelected(true);
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (group.getSelectedToggle() != null) {
					if ((int) new_toggle.getUserData() == 0) {
						test();
					} else {
						test2();
					}
				}
			}
		});

		pseudoLb.setText(PreferencesUtils.getUserEmail());
		this.articlesLv.setItems(articlesObservableList);
		this.articlesLv.setCellFactory(articlesLv -> new ArticleListViewCell(articlesLv.getWidth()));
		articlesLv.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				// TODO Auto-generated method stub
				System.out.println("Clicked on " + articlesLv.getSelectionModel().getSelectedItem().getTitle());
			}
		});
	}

	private void test() {
		this.articlesLv.setCellFactory(articlesLv -> new ArticleListViewCell(articlesLv.getWidth()));
	}

	private void test2() {
		this.articlesLv.setCellFactory(articlesLv -> new ArticleExtendedListViewCell(articlesLv.getWidth()));
	}
}
