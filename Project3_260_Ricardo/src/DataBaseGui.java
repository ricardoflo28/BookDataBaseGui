
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * JavaFX GUI for interacting with with BinaryTree-based book database.
 * Allows users to add, remove, find, load, save and look at tree 
 * with buttons and ComboBox with pre-defined books.
 * 
 */
public class DataBaseGui extends Application {
	
		private DataBaseAccess db = new DataBaseAccess();
	
		private ComboBox<Books> bookSelector;
	
		private TextArea treeArea;
		
		/**
		 * Starts the JavaFx application and builds GUI layout.
		 * 
		 * @param primaryStage the main application window 
		 * 
		 */
		public void start(Stage primaryStage) {

	
		primaryStage.setTitle("Book DataBase - BinaryTree");
		
		// ComboBox for selecting a book to operate on
		bookSelector = new ComboBox<>();
		
		bookSelector.getItems().addAll(

		new Books("Diary of a Wimpy Kid: Rodrick Rules", "Jeff Kinney", 2008),
		new Books("1984", "George Orwell", 1937),
		new Books("The Hunger Games", "Suzaine Collins", 2008),
		new Books("The Martian", "Andy Weir", 2014));
		bookSelector.setPromptText("Select a book");
		bookSelector.setPrefWidth(345);
		
		HBox input = new HBox(10, bookSelector);
		input.setPadding(new Insets(15));
		
		//buttons that are used for operations
		Button addButton = new Button("Add");
		Button removeButton = new Button("Remove");
		Button findButton = new Button("find book");
		Button saveData = new Button("Save");
		Button loadData = new Button("Load");
		Button treeDiagram = new Button("Show Diagram");
		
		//adding a book
		addButton.setOnAction(e -> {
			
		Books selected = bookSelector.getValue();
		if(selected!= null)
		{
			db.addBook(selected);
			updateTreeArea();
		}
		});
		
		//removing book 
		removeButton.setOnAction(e -> {
		Books selected = bookSelector.getValue();
		if(selected!= null)
		{
			db.remove(selected);
			updateTreeArea();
		}

			
		});
		
		//Find a book 
		findButton.setOnAction(e -> {
		Books selected = bookSelector.getValue();
			
			if(selected!= null)
			{
				Books found = db.find(selected);
				
				treeArea.setText(found == null ? "book not found " : found.toString());
			}
		});
		
		//save and loading the database
		saveData.setOnAction(e -> db.saveDataBase());
		loadData.setOnAction(e-> {
			
			db.loadDataBase();
			updateTreeArea();
			
		});
		
		//displays the tree diagram
		treeDiagram.setOnAction(e ->{
		
		treeArea.setText(db.getTreeDiagram());
		
		
		});
		
		HBox buttons = new HBox(10, addButton, removeButton, findButton, saveData, loadData);
		HBox buttons2 = new HBox(10, treeDiagram);
		buttons.setPadding(new Insets(15));
		buttons2.setPadding(new Insets(10));
		
		//displays area for tree output
		treeArea = new TextArea();
		treeArea.setEditable(false);
		treeArea.setPrefHeight(370);
		
		updateTreeArea();
		
		VBox layout = new VBox(10, input, buttons, treeArea, buttons2);
		
		Scene scene = new Scene(layout, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
		
		/**
		 * Updates the text area with the in-order sorted
		 * tree toString contents
		 */
		private void updateTreeArea()
		{
			treeArea.setText(db.treeToString());
		}
	
		/**
		 * main that launches the JavaFX application
		 * 
		 * @param args represents command-line arguments
		 */
		public static void main(String[] args)
		{
		launch(args);
		}
	
}
