package gui;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The PackageCalculator class is the main class for the PackageCalculator application.
 * It sets up the GUI and initializes all the necessary components.
 *
 * The GUI is divided into several areas: 
 *      {@link ToolbarArea} Represents the toolbar area for accessing various functions.
 *      {@link CalculatorArea} Represents the area for calculating shipping costs based on user input.
 *      {@link InspectorArea} Represents the area for inspecting and modifying packaging details.
 *      {@link MessagesArea} Represents the area for displaying messages and notifications.
 *      {@link StatusArea} Represents the area for displaying the status of the application.
 * Each area is responsible for a specific functionality in the application.
 */

public class PackageCalculator extends Application {

	public final static String APPNAME = "PackageCalculator";
	
	// singleton
	private static PackageCalculator instance;
	public static PackageCalculator getInstance() {
		return instance;
	}
	
    // gui areas
	public ToolbarArea toolbarArea = new ToolbarArea();
	public CalculatorArea editorArea = new CalculatorArea();;
	public InspectorArea inspectorArea = new InspectorArea();
	public MessagesArea messagesArea = new MessagesArea();
	public StatusArea statusArea = new StatusArea();
	
	// remember stage for subwindows
	private Stage primaryStage;
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}


	/**
	 * Start the application
	 *
	 * @param primaryStage the stage to start the application
	 */
	@Override
	public void start(Stage primaryStage) {
		// remember singleton instance (instantiated by javafx)
		PackageCalculator.instance = this;
		
		// remember stage for subwindows
		this.primaryStage = primaryStage;
		
		// lr2SplitPane
		SplitPane lr2SplitPane = new SplitPane();
		lr2SplitPane.getItems().addAll(editorArea, inspectorArea);
		lr2SplitPane.setDividerPositions(0.8f, 0.2f);

		// tdSplitPane
		SplitPane tdSplitPane = new SplitPane();
		tdSplitPane.setOrientation(Orientation.VERTICAL);
		tdSplitPane.getItems().addAll(lr2SplitPane, messagesArea);
		tdSplitPane.setDividerPositions(0.9f, 0.1f);
		
		// add all areas
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(toolbarArea);
		mainPane.setCenter(tdSplitPane);
		mainPane.setBottom(statusArea);

		// show main pane
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		Scene scene = new Scene(mainPane, screenBounds.getWidth(), screenBounds.getHeight(), true);

		primaryStage.setTitle(APPNAME);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
