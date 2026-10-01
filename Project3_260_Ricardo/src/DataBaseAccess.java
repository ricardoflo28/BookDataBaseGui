import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * DataBaseAccess class serves as main interface for interacting
 * with binaryTree database. Supports adding, removing and searching,
 * printing and saving and loading Book data stored inside a binary tree.
 * 
 * Also, the tree is automatically loaded into file when class is created
 * and saved manually with the saveDataBase() method.
 * 
 * 
 */
public class DataBaseAccess {
	
	private BinaryTree<Books> tree;
	
	private static final String File_Name = "database.dat";
	
	/**
	 * Creates  a new DataBaseAccess object. Initializes a BinaryTree
	 * and attempts to load previous saved database from disk.
	 * 
	 */
	public DataBaseAccess()
	{
		tree = new BinaryTree<>();
		loadDataBase();
	}

	/**
	 * Method to add new book object to tree 
	 * 
	 * @param b represents book object to add
	 */
	public void addBook(Books b)
	{
		tree.addData(b);
	}
	
	/**
	 * 
	 * Adds a new Book object to tree
	 * 
	 * @param b represents new book to add
	 */
	public void remove(Books b)
	{
		tree.remove(b);
	}
	
	/**
	 * Searches the tree for matching book
	 * 
	 * @param b represents book to search for
	 * @return the book that matches (if found), else it
	 * returns null
	 */
	public Books find(Books b)
	{
		return tree.find(b);
	}
	
	/**
	 * Returns an in-order sorted string of all
	 * Books stored in tree
	 * 
	 * @return a sorted list of Books in string form
	 */
	public String treeToString()
	{
		return tree.toString();
	}
	
	/**
	 * 
	 * Returns a top-down diagram of current tree structure
	 *  
	 * @return a tree diagram as a string
	 */
	public String getTreeDiagram()
	{
		return tree.printTree();
	}
	
	/**
	 * Saves current binary tree to file using object serialization
	 * File name is "database.dat"
	 * 
	 */
	public void saveDataBase()
	{
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(File_Name))) {
			out.writeObject(tree);
			System.out.println("Database saved");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Error in saving file");
		}
				
				
	}
	
	/**
	 * 
	 * Loads the BinaryTree from the saved data base file
	 * if it exists, else if it doesn't it displays
	 * : "No existing database file".
	 * 
	 */
	public void loadDataBase()
	{
		File file = new File(File_Name);
		
		if(!file.exists())
		{
			System.out.println("No existing database file");
			return;
		}
		
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(File_Name))){
			
			tree = (BinaryTree<Books>) in.readObject();
			System.out.println("Database has been loaded");
		} catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Error in loading database");
		}
	}
}
