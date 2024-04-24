package gui;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class ExplorerArea extends TabPane {

	private Tab explorerTab;
	private TreeView<FileItem> treeView;

	// wrapper class for a tree cell
	class FileItem {
		public File file;

		// constructors
		@SuppressWarnings("unused")
		private FileItem() {
		}
		public FileItem(File file) {
			this.file = file;
		}
		public FileItem(String str) {
			this.file = new File(str);
		}

		// redefine string name of node
		@Override
		public String toString() {
			// hide root folder name (is already shown in window title)
			if (file.getAbsolutePath().equals(PackageCalculator.getInstance().rootPath)) {
				return "..." + File.separator + file.getName();
			}
			// else return only filename
			return file.getName();
		}

		//@Override
		public ContextMenu getMenu() {
			return new ContextMenu(new MenuItem("test"));
		}
	}

	// ---------------------------------------------------------------------------------
	// code block originally from https://docs.oracle.com/javafx/2/api/javafx/scene/control/TreeItem.html
	// ---------------------------------------------------------------------------------
	private TreeView<FileItem> buildFileSystemBrowser(String rootPath) {
		TreeItem<FileItem> root = createNode(new FileItem(new File(rootPath)));
		return new TreeView<FileItem>(root);
	}
	// This method creates a TreeItem to represent the given File. It does this
	// by overriding the TreeItem.getChildren() and TreeItem.isLeaf() methods 
	// anonymously, but this could be better abstracted by creating a 
	// 'FileTreeItem' subclass of TreeItem. However, this is left as an exercise
	// for the reader.
	private TreeItem<FileItem> createNode(final FileItem fileItem) {
		return new TreeItem<FileItem>(fileItem) {
			// We cache whether the File is a leaf or not. A File is a leaf if
			// it is not a directory and does not have any files contained within
			// it. We cache this as isLeaf() is called often, and doing the 
			// actual check on File is expensive.
			private boolean isLeaf;
			// We do the children and leaf testing only once, and then set these
			// booleans to false so that we do not check again during this
			// run. A more complete implementation may need to handle more 
			// dynamic file system situations (such as where a folder has files
			// added after the TreeView is shown). Again, this is left as an
			// exercise for the reader.
			private boolean isFirstTimeChildren = true;
			private boolean isFirstTimeLeaf = true;
			@Override public ObservableList<TreeItem<FileItem>> getChildren() {
				if (isFirstTimeChildren) {
					isFirstTimeChildren = false;
					// First getChildren() call, so we actually go off and 
					// determine the children of the File contained in this TreeItem.
					super.getChildren().setAll(buildChildren(this));
				}
				return super.getChildren();
			}
			@Override public boolean isLeaf() {
				if (isFirstTimeLeaf) {
					isFirstTimeLeaf = false;
					FileItem f = (FileItem) getValue();
					isLeaf = f.file.isFile();
				}
				return isLeaf;
			}
			private ObservableList<TreeItem<FileItem>> buildChildren(TreeItem<FileItem> TreeItem) {
				FileItem f = TreeItem.getValue();
				if (f != null && f.file.isDirectory()) {
					File[] files = f.file.listFiles();
					if (files != null) {
						ObservableList<TreeItem<FileItem>> children = FXCollections.observableArrayList();
						for (File childFile : files) {
							// add only folders and txt-files
							if (childFile.isDirectory() || childFile.getName().toLowerCase().endsWith(".txt")) {
								children.add(createNode(new FileItem(childFile)));
							}
						}
						return children;
					}
				}
				return FXCollections.emptyObservableList();
			}
		};
	}
	// ---------------------------------------------------------------------------------

	private final class TreeCellImpl extends TreeCell<FileItem> {

		@Override
		public void updateItem(FileItem item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
				return;
			}
			setText(getItem() == null ? "" : getItem().toString());
			setGraphic(getTreeItem().getGraphic());
			ContextMenu contextMenu = new ContextMenu();
			if (getItem().file.isDirectory()) {
				// folder
				MenuItem newFileMenu = new MenuItem("New File");
				MenuItem newSubfolderMenu = new MenuItem("New Subfolder");
				contextMenu.getItems().addAll(newFileMenu, newSubfolderMenu);
			} else {
				// non-folder file
				MenuItem openMenu = new MenuItem("Open");
				MenuItem closeMenu = new MenuItem("Close");
				//contextMenuItem.setOnAction(new EventHandler() {...});
				contextMenu.getItems().addAll(openMenu, closeMenu);
			}
			MenuItem copyMenu = new MenuItem("Copy");
			MenuItem pasteMenu = new MenuItem("Paste");
			MenuItem deleteMenu = new MenuItem("Delete");
			contextMenu.getItems().addAll(new SeparatorMenuItem(), copyMenu, pasteMenu, deleteMenu);
			setContextMenu(contextMenu);
		}
	}

	@SuppressWarnings("unchecked")
	public void loadNewTree(String projectPath) {

		TreeItem<FileItem> root = new TreeItem<FileItem>();
		root.setExpanded(true);
		root.getChildren().addAll(
				new TreeItem<FileItem>(),
				new TreeItem<FileItem>(),
				new TreeItem<FileItem>()
				);
		treeView = new TreeView<FileItem>(root);

		this.treeView = buildFileSystemBrowser(projectPath);

		treeView.setCellFactory(new Callback<TreeView<FileItem>,TreeCell<FileItem>>(){
			@Override
			public TreeCell<FileItem> call(TreeView<FileItem> p) {
				return new TreeCellImpl();
			}
		});

		treeView.getRoot().setExpanded(true);

		explorerTab.setContent(treeView);		
	}

	public ExplorerArea() {
		// add one fix tab
		explorerTab = new Tab("Explorer");
		explorerTab.setClosable(false);
		this.getTabs().add(explorerTab);
	}


}
