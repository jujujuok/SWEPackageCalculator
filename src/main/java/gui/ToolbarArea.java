package gui;

import control.ProjectHandling;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The ToolbarArea class represents a UI component for creating a toolbar with buttons.
 * It extends the ToolBar class from JavaFX.
 * It's used to create a toolbar with buttons for the user to interact with the application.
 * This class can be customized to suit the specific needs of the application.
 *
 * @see ToolBar Represents a control that displays items in a horizontal or vertical row.
 * @see PackageCalculator The main class of the PackageCalculator application.
 */

public class ToolbarArea extends ToolBar {
	
	private void showInfoDialog() {
		Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox vbox = new VBox(20);
        Text infoText = new Text("Package Calculator v0.3 \n (c) 2020 I. Bogicevic, J. Buerk");
        vbox.getChildren().add(infoText);
        Scene dialogScene = new Scene(vbox, 400, 250);
        dialog.setScene(dialogScene);
        dialog.show();
	}
	
	public ToolbarArea() {
		// initialize buttons
		Button openProjectButton = new Button("Open Project");
		Button newFileButton = new Button("New File");
		Button saveFileButton = new Button("Save File");
		Button saveFileAsButton = new Button("Save File as");
		Button settingsButton = new Button("Settings");
		Button aboutButton = new Button("About");
		Button infoButton = new Button("Info");
		// actionlisteners
		openProjectButton.setOnAction(e -> ProjectHandling.openProject());
		newFileButton.setOnAction(e -> ProjectHandling.newFile());
		infoButton.setOnAction(e -> showInfoDialog());
		// add all buttons
		this.getItems().add(openProjectButton);
		this.getItems().add(newFileButton);
		this.getItems().add(saveFileButton);
		this.getItems().add(saveFileAsButton);
		this.getItems().add(new Separator());
		this.getItems().add(settingsButton);
		this.getItems().add(aboutButton);
		this.getItems().add(infoButton);
	}
}