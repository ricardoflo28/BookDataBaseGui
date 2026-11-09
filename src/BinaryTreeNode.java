import java.io.Serializable;

/**
 * Class that represents a single node 
 * Stores a data value, and references left
 * and right nodes, and height value use for AVL
 * balancing
 * 
 * @param <E> type of data that's stored in the node, and must extend comparable
 */
public class BinaryTreeNode <E extends Comparable<E>> implements Serializable{
	private static long serialVersionUID = 1L;
	
	E data;
	BinaryTreeNode<E> left;
	BinaryTreeNode<E> right;
	int height;
	
	/**
	 * Creates constructor that contains given data
	 * The node starts with no children and height is set
	 * to 1.
	 * 
	 * @param data value is stored in this node
	 */
	public BinaryTreeNode(E data)
	{
		this.data = data;
		this.left = null;
		this.right = null;
		this.height = 1;
	}
	
	/**
	 * returns data stored in node 
	 * 
	 * @return the node's data value
	 */
	
	public E getData()
	{
		return data;
	}
	
	/**
	 * Returns left child of node
	 * 
	 * @return left child or null if none
	 */
	public BinaryTreeNode<E> getLeft()
	{
		return left;
	}
	
	/**
	 * Returns right child of node
	 * 
	 * @return right child or null if none
	 */
	public BinaryTreeNode<E> getRight(){
		return right;
	}
	
	/**
	 * sets left child of following
	 * node
	 * 
	 * @param left represents new left child
	 */
	public void setLeft( BinaryTreeNode<E> left)
	{
		this.left = left;
	}
	
	/**
	 * sets right child of following node
	 * 
	 * @param right represents new right child
	 */
	public void setRight( BinaryTreeNode<E> right)
	{
		this.right = right;
	}
	
	
}
