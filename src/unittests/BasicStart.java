package unittests;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import org.junit.Test;

import com.rssaggregator.desktop.MainApp;

public class BasicStart {

	@Test
	public void testA() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				new JFXPanel(); // Initializes the JavaFx Platform
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						new MainApp().start(new Stage()); // Create and
															// initialize
															// your app.

					}
				});
			}
		});
		thread.start();// Initialize the thread
		Thread.sleep(10000); // Time to use the app, with out this, the thread
								// will be killed before you can tell.
	}

}