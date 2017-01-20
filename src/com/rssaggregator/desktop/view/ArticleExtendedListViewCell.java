package com.rssaggregator.desktop.view;

import java.io.IOException;

import com.rssaggregator.desktop.model.TmpArticle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

public class ArticleExtendedListViewCell extends ListCell<TmpArticle> {

	@FXML
	private Label title;
	@FXML
	private Label description;
	@FXML
	private Label pubDate;
	@FXML
	private AnchorPane rootView;

	private FXMLLoader loader;
	private double parentWidth;

	public ArticleExtendedListViewCell(double parentWidth) {
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
				loader = new FXMLLoader(getClass().getResource("Row_Article_Extended.fxml"));
				loader.setController(this);

				try {
					loader.load();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("ERORORORORORER");
				}
			}

			rootView.setPrefWidth(parentWidth);

			title.setText(article.getTitle());
			description.setText(article.getDescription());
			pubDate.setText("Il y a 30 mins.");

			setText(null);
			setGraphic(rootView);
		}
	}

}
