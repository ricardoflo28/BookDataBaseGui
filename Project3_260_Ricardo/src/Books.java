import java.io.Serializable;
/**
 * Class that represents books with title, author, and year
 * Implements Serializable so the books are able to be saved 
 * 
 * Implements comparable so books can be sorted alphabetically by title
 * 
 * 
 */
public class Books implements Serializable, Comparable<Books>{

	private String title;
	private String author;
	private int year;
	
	/**
	 * Constructor for new book objects with given title, author and year
	 * 
	 * 
	 * @param title of book 
	 * @param author of book
	 * @param year of book
	 */
	public Books(String title, String author, int year)
	{
		this.title = title;
		this.author = author;
		this.year = year;
	}
    
	/**
	 * Compares book to other book based on titles
	 * that is case-insensitive in alphabetical order.
	 * 
	 * @param b which represents the other book to compare
	 * @return negative integer if title comes before the other title,
	 * zero if equal and positive if afterS
	 * 
	 */
	public int compareTo(Books b)
	{
		return this.title.compareToIgnoreCase(b.title);
	}
	
	/**
	 * returns the title of book
	 * 
	 * 
	 * @return title of book
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Returns a string representation of the book
	 * with the title followed by author then the year it
	 * was made.
	 * 
	 * 
	 * @return formatted string that describes book
	 */
	public String toString()
	{
		return title + " by " + author + " (" + year + ")";
	}
}
