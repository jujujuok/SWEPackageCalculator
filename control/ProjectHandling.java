package control;

import java.io.File;

import gui.PackageCalculator;
import javafx.stage.DirectoryChooser;

public class ProjectHandling {

	static public void openProject(String rootPath) {
		// update window title
		PackageCalculator.getInstance().getPrimaryStage().setTitle(PackageCalculator.APPNAME + " – " + rootPath);
		// load tree in explorer
		PackageCalculator.getInstance().explorerArea.loadNewTree(rootPath);
		// remember open project
		PackageCalculator.getInstance().rootPath = rootPath;
	}

	static public void openProject() {
		final DirectoryChooser directoryChooser = new DirectoryChooser();
		File projectDirectory;
		projectDirectory = directoryChooser.showDialog(PackageCalculator.getInstance().getPrimaryStage());
		if (projectDirectory != null) {
			projectDirectory.getAbsolutePath();
		}
		//openProject(projectDirectory.getAbsolutePath());
	}
	
	static public void newFile() {
		
	}

}
