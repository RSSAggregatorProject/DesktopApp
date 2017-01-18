package com.rssaggregator.desktop;

import java.io.IOException;

import com.rssaggregator.desktop.utils.Globals;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class MainViewScene {

	private MainApp mainApp;
	private BorderPane rootView;

	public MainViewScene(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void launchMainView() {
		initRootLayout();
		initMainView();
	}

	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.MAIN_VIEW_ROOT));
			this.rootView = (BorderPane) loader.load();

			/**
			 * I can use the old stage or a new one
			 */
			Scene scene = new Scene(this.rootView);

			this.mainApp.getPrimaryStage().setScene(scene);
			this.mainApp.getPrimaryStage().setResizable(true);
			this.mainApp.getPrimaryStage().setMinWidth(1200d);
			this.mainApp.getPrimaryStage().setMinHeight(800d);
			this.mainApp.getPrimaryStage().show();

			/**
			 * New stage
			 */
			/*
			 * Stage newStage = new Stage(); newStage.setScene(scene);
			 * newStage.setTitle(Globals.APP_NAME); newStage.getIcons().add(new
			 * Image("file:resources/images/icon_rss.png")); newStage.show();
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initMainView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource(Globals.MAIN_VIEW));
			AnchorPane pane = (AnchorPane) loader.load();

			this.rootView.setCenter(pane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
