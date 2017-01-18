package com.rssaggregator.desktop.view;

import java.io.IOException;

import com.rssaggregator.desktop.MainApp;
import com.rssaggregator.desktop.model.TmpArticle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

public class ArticleListViewCell extends ListCell<TmpArticle> {

	@FXML
	private Label title;
	@FXML
	private Label pubDate;
	@FXML
	private AnchorPane anchorPane;

	private FXMLLoader loader;
	private double parentWidth;

	public ArticleListViewCell(double parentWidth) {
		// TODO Auto-generated constructor stub
		this.parentWidth = parentWidth;
	}

	@Override
	protected void updateItem(TmpArticle article, boolean empty) {
		super.updateItem(article, empty);

		if (empty || article == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (loader == null) {
				loader = new FXMLLoader(getClass().getResource("Row_Article.fxml"));
				loader.setController(this);

				try {
					loader.load();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("ERORORORORORER");
				}
			}

			anchorPane.setPrefWidth(parentWidth);

			System.out.println(String.valueOf(this.parentWidth));
			System.out.println("article name" + article.getTitle());

			title.setText(article.getTitle().toString());
			pubDate.setText("Il y a 30 mins.");

			setText(null);
			setGraphic(anchorPane);
		}
	}

}
