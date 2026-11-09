import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 
 * JavaFX GUI for interacting with BinaryTree-based book database.
 * Allows users to add, remove, find, load, save and look at tree 
 * with buttons and ComboBox with pre-defined books.
 * 
 * Books are shown as colorful cards with their cover image. Cover images
 * are read from the "books" folder (png, jpg, gif or bmp). If a cover
 * file is missing, a colored placeholder cover is drawn instead.
 * 
 */
public class DataBaseGui extends Application {

	private DataBaseAccess db = new DataBaseAccess();

	private ComboBox<Books> bookSelector;

	private ListView<Books> bookList;

	private TextArea diagramArea;

	private Label statusLabel;

	private Button treeDiagram;

	// sizes of the cover pictures
	private static final double COVER_W = 64;
	private static final double COVER_H = 96;

	// gradient colors used for placeholder covers
	private static final String[][] PALETTE = {
			{ "#ff6b6b", "#feca57" },
			{ "#48dbfb", "#5f27cd" },
			{ "#1dd1a1", "#10ac84" },
			{ "#ff9ff3", "#f368e0" },
			{ "#feca57", "#ff9f43" },
			{ "#54a0ff", "#2e86de" } };

	/**
	 * Starts the JavaFx application and builds GUI layout.
	 * 
	 * @param primaryStage the main application window 
	 * 
	 */
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Book DataBase - BinaryTree");

		// ---------- header banner ----------
		Label title = new Label("Book Database");
		title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;");
		Label subtitle = new Label("stored in a self-balancing binary tree");
		subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.85);");

		VBox header = new VBox(2, title, subtitle);
		header.setAlignment(Pos.CENTER);
		header.setPadding(new Insets(18));
		header.setStyle("-fx-background-color: linear-gradient(to right, #ff6b6b, #c44dff, #4d79ff);");
		header.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.4)));

		// ---------- ComboBox for selecting a book to operate on ----------
		bookSelector = new ComboBox<>();

		bookSelector.getItems().addAll(

				new Books("Diary of a Wimpy Kid: Rodrick Rules", "Jeff Kinney", 2008),
				new Books("1984", "George Orwell", 1937),
				new Books("The Hunger Games", "Suzaine Collins", 2008),
				new Books("The Martian", "Andy Weir", 2014));
		bookSelector.setPromptText("Select a book");
		bookSelector.setPrefWidth(520);
		bookSelector.setStyle("-fx-font-size: 14px; -fx-background-radius: 10; "
				+ "-fx-background-color: white; -fx-border-color: #c44dff; "
				+ "-fx-border-radius: 10; -fx-border-width: 2;");
		bookSelector.setCellFactory(lv -> makeComboCell(false));
		bookSelector.setButtonCell(makeComboCell(true));

		HBox input = new HBox(10, bookSelector);
		input.setAlignment(Pos.CENTER);
		input.setPadding(new Insets(5, 15, 0, 15));

		// ---------- buttons that are used for operations ----------
		Button addButton = styledButton("Add", "#2ecc71", "#1e9e57");
		Button removeButton = styledButton("Remove", "#ff6b6b", "#d63031");
		Button findButton = styledButton("Find Book", "#48a9ff", "#1e6fd9");
		Button saveData = styledButton("Save", "#ffb142", "#e58e26");
		Button loadData = styledButton("Load", "#a55eea", "#7d3fc4");
		treeDiagram = styledButton("Show Diagram", "#1dd1a1", "#0a9b76");

		// ---------- book list (cards with cover pictures) ----------
		bookList = new ListView<>();
		bookList.setStyle("-fx-background-color: transparent; "
				+ "-fx-control-inner-background: transparent; -fx-background-insets: 0;");
		bookList.setCellFactory(lv -> new BookCell());
		bookList.setPlaceholder(styledText("No books yet - pick one above and press Add!"));

		// ---------- diagram view (hidden until button is pressed) ----------
		diagramArea = new TextArea();
		diagramArea.setEditable(false);
		diagramArea.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 13px;");
		diagramArea.setVisible(false);

		StackPane center = new StackPane(bookList, diagramArea);
		center.setPadding(new Insets(5, 10, 5, 10));
		VBox.setVgrow(center, Priority.ALWAYS);

		// ---------- status bar ----------
		statusLabel = new Label("Welcome! Pick a book and press Add.");
		statusLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");

		// adding a book
		addButton.setOnAction(e -> {

			Books selected = bookSelector.getValue();
			if (selected != null)
			{
				db.addBook(selected);
				showBooks();
				setStatus("Added: " + selected.getTitle(), "#7bed9f");
			}
			else
			{
				setStatus("Please select a book first", "#ffeaa7");
			}
		});

		// removing book 
		removeButton.setOnAction(e -> {
			Books selected = bookSelector.getValue();
			if (selected != null)
			{
				db.remove(selected);
				showBooks();
				setStatus("Removed: " + selected.getTitle(), "#ff9f9f");
			}
			else
			{
				setStatus("Please select a book first", "#ffeaa7");
			}
		});

		// Find a book 
		findButton.setOnAction(e -> {
			Books selected = bookSelector.getValue();

			if (selected != null)
			{
				Books found = db.find(selected);

				if (found == null)
				{
					setStatus("Book not found", "#ff9f9f");
				}
				else
				{
					showBooks();
					bookList.getSelectionModel().select(found);
					bookList.scrollTo(found);
					setStatus("Found: " + found, "#7bed9f");
				}
			}
			else
			{
				setStatus("Please select a book first", "#ffeaa7");
			}
		});

		// save and loading the database
		saveData.setOnAction(e -> {
			db.saveDataBase();
			setStatus("Database saved", "#7bed9f");
		});
		loadData.setOnAction(e -> {

			db.loadDataBase();
			showBooks();
			setStatus("Database loaded", "#7bed9f");

		});

		// toggles between the book cards and the tree diagram
		treeDiagram.setOnAction(e -> {

			if (diagramArea.isVisible())
			{
				showBooks();
			}
			else
			{
				diagramArea.setText(db.getTreeDiagram());
				diagramArea.setVisible(true);
				bookList.setVisible(false);
				treeDiagram.setText("Show Books");
				setStatus("Showing tree diagram", "#dfe6e9");
			}

		});

		HBox buttons = new HBox(10, addButton, removeButton, findButton, saveData, loadData);
		buttons.setAlignment(Pos.CENTER);
		buttons.setPadding(new Insets(5, 15, 5, 15));

		HBox bottom = new HBox(20, treeDiagram, statusLabel);
		bottom.setAlignment(Pos.CENTER_LEFT);
		bottom.setPadding(new Insets(12, 15, 15, 15));
		bottom.setStyle("-fx-background-color: rgba(0,0,0,0.25);");

		VBox layout = new VBox(12, header, input, buttons, center, bottom);
		layout.setStyle("-fx-background-color: linear-gradient(to bottom, #1e1e50, #3a1c71 60%, #5b2a86);");

		showBooks();

		// warn if a cover is a .webp file (JavaFX can't display those)
		for (Books b : bookSelector.getItems())
		{
			if (hasOnlyWebp(b.getTitle()))
			{
				setStatus("Some covers are .webp - convert them to .png (JavaFX can't show webp)", "#ffeaa7");
				break;
			}
		}

		Scene scene = new Scene(layout, 700, 760);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Refreshes the book cards from the tree (in sorted order) and
	 * switches the view back to the book list.
	 */
	private void showBooks()
	{
		bookList.getItems().setAll(db.getAllBooks());
		bookList.setVisible(true);
		diagramArea.setVisible(false);
		treeDiagram.setText("Show Diagram");
	}

	/**
	 * Shows a colored message in the status bar
	 * 
	 * @param message text to show
	 * @param color the text color (css color)
	 */
	private void setStatus(String message, String color)
	{
		statusLabel.setText(message);
		statusLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 13px; -fx-font-weight: bold;");
	}

	private Label styledText(String text)
	{
		Label l = new Label(text);
		l.setStyle("-fx-text-fill: rgba(255,255,255,0.7); -fx-font-size: 15px;");
		return l;
	}

	/**
	 * Creates a rounded, colorful button with a hover effect
	 */
	private Button styledButton(String text, String color1, String color2)
	{
		Button b = new Button(text);
		b.setStyle("-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + "); "
				+ "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; "
				+ "-fx-background-radius: 20; -fx-padding: 8 18 8 18; -fx-cursor: hand;");
		b.setEffect(new DropShadow(6, Color.rgb(0, 0, 0, 0.35)));
		b.setOnMouseEntered(e -> {
			b.setScaleX(1.07);
			b.setScaleY(1.07);
		});
		b.setOnMouseExited(e -> {
			b.setScaleX(1.0);
			b.setScaleY(1.0);
		});
		return b;
	}

	/**
	 * Cell used by the ComboBox (small cover next to the title)
	 */
	private ListCell<Books> makeComboCell(boolean isButtonCell)
	{
		return new ListCell<Books>() {
			protected void updateItem(Books b, boolean empty)
			{
				super.updateItem(b, empty);
				if (empty || b == null)
				{
					setGraphic(null);
					setText(isButtonCell ? "Select a book" : null);
				}
				else
				{
					setText(b.toString());
					setGraphic(makeCover(b, 28, 42));
				}
			}
		};
	}

	// folder that holds the cover pictures (next to src)
	private static final String COVER_FOLDER = "books";

	// picture types JavaFX can load (WebP is NOT supported by JavaFX)
	private static final String[] EXTENSIONS = { ".png", ".jpg", ".jpeg", ".gif", ".bmp" };

	/**
	 * Returns the file names (without extension) to look for in the books folder
	 * for the given title.
	 */
	private static String[] coverBaseNames(String title)
	{
		String lower = title.toLowerCase();
		String generic = lower.replaceAll("[^a-z0-9]+", "_").replaceAll("^_+|_+$", "");
		String mapped = generic;

		if (lower.equals("1984")) mapped = "1984png";
		else if (lower.equals("the hunger games")) mapped = "hungerGames";
		else if (lower.equals("the martian")) mapped = "Martian";
		else if (lower.startsWith("diary of a wimpy kid")) mapped = "png1";

		return new String[] { mapped, generic };
	}

	/**
	 * Finds the cover picture file for a title in the books folder.
	 * 
	 * @return the file, or null if there is no usable picture
	 */
	private static File findCoverFile(String title)
	{
		for (String base : coverBaseNames(title))
		{
			for (String ext : EXTENSIONS)
			{
				File f = new File(COVER_FOLDER, base + ext);
				if (f.exists()) return f;
			}
		}
		return null;
	}

	/**
	 * @return true if the title only has a .webp picture (which JavaFX can't show)
	 */
	private static boolean hasOnlyWebp(String title)
	{
		if (findCoverFile(title) != null) return false;
		for (String base : coverBaseNames(title))
		{
			if (new File(COVER_FOLDER, base + ".webp").exists()) return true;
		}
		return false;
	}

	/**
	 * Builds the cover picture for a book. Uses the picture from the books folder if the
	 * file exists, otherwise draws a colored placeholder with the title on it.
	 */
	private Node makeCover(Books b, double w, double h)
	{
		StackPane cover = new StackPane();
		cover.setMinSize(w, h);
		cover.setPrefSize(w, h);
		cover.setMaxSize(w, h);

		File file = findCoverFile(b.getTitle());

		if (file != null)
		{
			Image img = new Image(file.toURI().toString(), w, h, true, true);
			if (!img.isError())
			{
				cover.getChildren().add(new ImageView(img));
				cover.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.45)));
				return cover;
			}
		}

		// placeholder cover
		String[] colors = PALETTE[Math.floorMod(b.getTitle().hashCode(), PALETTE.length)];
		cover.setStyle("-fx-background-radius: 6; -fx-background-color: linear-gradient(to bottom right, "
				+ colors[0] + ", " + colors[1] + ");");

		if (h > 60)
		{
			Label t = new Label(b.getTitle());
			t.setWrapText(true);
			t.setMaxWidth(w - 8);
			t.setAlignment(Pos.CENTER);
			t.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; "
					+ "-fx-text-alignment: center;");
			cover.getChildren().add(t);
		}
		cover.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.45)));
		return cover;
	}

	/**
	 * A card in the list: cover picture on the left, details on the right
	 */
	private class BookCell extends ListCell<Books> {

		private static final String CARD = "-fx-background-color: white; -fx-background-radius: 14;";
		private static final String CARD_SELECTED = "-fx-background-color: #fff3c4; -fx-background-radius: 14; "
				+ "-fx-border-color: #ff9f43; -fx-border-width: 3; -fx-border-radius: 14;";

		private HBox card;

		protected void updateItem(Books b, boolean empty)
		{
			super.updateItem(b, empty);
			setStyle("-fx-background-color: transparent; -fx-padding: 4 6 4 6;");

			if (empty || b == null)
			{
				card = null;
				setGraphic(null);
				setText(null);
				return;
			}

			Label bookTitle = new Label(b.getTitle());
			bookTitle.setWrapText(true);
			bookTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #2d1b69;");

			Label author = new Label("by " + b.getAuthor());
			author.setStyle("-fx-font-size: 14px; -fx-text-fill: #636e72;");

			Label year = new Label(String.valueOf(b.getYear()));
			year.setStyle("-fx-background-color: linear-gradient(to right, #ff6b6b, #c44dff); "
					+ "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px; "
					+ "-fx-background-radius: 12; -fx-padding: 3 12 3 12;");

			VBox info = new VBox(6, bookTitle, author, year);
			info.setAlignment(Pos.CENTER_LEFT);

			card = new HBox(16, makeCover(b, COVER_W, COVER_H), info);
			card.setAlignment(Pos.CENTER_LEFT);
			card.setPadding(new Insets(10));
			card.setStyle(isSelected() ? CARD_SELECTED : CARD);
			card.setEffect(new DropShadow(6, Color.rgb(0, 0, 0, 0.3)));

			setText(null);
			setGraphic(card);
		}

		public void updateSelected(boolean selected)
		{
			super.updateSelected(selected);
			if (card != null)
			{
				card.setStyle(selected ? CARD_SELECTED : CARD);
			}
		}
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