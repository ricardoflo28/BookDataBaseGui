import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A self balancing AVL Binary Tree implementation that stores elements
 * of the type E.
 * Supports insertion, removal, searching and two different style of tree printing.
 * All data types that are stored are comparable.
 * 
 * 
 * @param <E> represents the data stored in the tree and must extend Comparable.
 */
public class BinaryTree <E extends Comparable<E>> implements Serializable{
	private static long serialVersionUID = 1L;
	
	private BinaryTreeNode<E> root;
	
	
	/**
	 * Empty constructor for BinaryTree class that sets root to null.
	 * 
	 */
	public BinaryTree()
	{
		root = null;
	}
	
	/**
	 * Returns the height of given node
	 * 
	 * @param n the node who's height is being checked 
	 * @return height of node or 0 if null
	 */
	private int height(BinaryTreeNode<E> n)
	{
		return n == null ? 0 : n.height;
	}
	
	/**
	 * Calculates balance factor of node
	 * 
	 * @param n is the node to evaluate
	 * @return height difference between left and right subtrees
	 */
	private int getBalance(BinaryTreeNode<E> n)
	{
		return n == null ? 0 : height(n.left) - height(n.right);
	}
	
	/**
	 * Performs the right rotation of given subtree root
	 * 
	 * @param y is the node at which it performs the rotation
	 * @return new root after rotation
	 */
	private BinaryTreeNode <E> rightRotation(BinaryTreeNode <E> y)
	{
		BinaryTreeNode <E> x = y.left; //x becomes new root
		BinaryTreeNode <E> T2 = x.right; //T2 stores the subtree
		
		//performing rotation
		x.right = y;
		y.left = T2;
		
		//updates height
		y.height = Math.max(height(y.left), height(y.right)) + 1;
		x.height = Math.max(height(x.left), height(x.right)) + 1;
		
		return x;
	}
	
	/**
	 * Performs the left rotation of given subtree root
	 * 
	 * @param x is the node at which it performs the rotation
	 * @return new root after rotation
	 */
	private BinaryTreeNode <E> leftRotation(BinaryTreeNode <E> x)
	{
		BinaryTreeNode <E> y = x.right; //y becomes new root  
		BinaryTreeNode <E> T2 = y.left; //T2 stores subtree
		
		//performing rotation
		y.left = x;
		x.right = T2;
		
		//updates following heights
		x.height = Math.max(height(x.left), height(x.right)) + 1;
		y.height = Math.max(height(y.left), height(y.right)) + 1;
		
		return y;
	}
	
	/**
	 * Inserts piece of data to binary tree
	 * 
	 * @param data value that will be inserted
	 */
	public void addData(E data)
	{
		root = addNode(root, data);  
	} 
	
	/**
	 * Using recursion to insert new node into subtree and performs
	 * AVL balancing/
	 * 
	 * @param node the current root of subtree
	 * @param data the data that will be inserted
	 * @return updated subtree root
	 */
	private BinaryTreeNode<E> addNode(BinaryTreeNode<E> node, E data)
	{
		if(node == null)
		{
			return new BinaryTreeNode<>(data);
		}
		
		//compares to decide left or right
		int compare = data.compareTo(node.getData());
		
		if (compare < 0)
		{
			//inserts left subtree
			node.setLeft(addNode(node.getLeft(), data));
		}
		else if(compare > 0)
		{
			//inserts right subtree
			node.setRight(addNode(node.getRight(), data));
		}
		else return node;
		
		//updates height for AVL
		node.height = 1 + Math.max(height(node.left), height(node.right));
		
		//checks balance
		int balance = getBalance(node);
		
		//if value went to left -> left rotation it rotates right
		if(balance > 1 && data.compareTo(node.left.data) < 0)
		{
			return rightRotation(node);
		}
		
		//if value went to right -> right rotation it rotates left
		if(balance < -1 && data.compareTo(node.right.data) > 0)
		{
			return leftRotation(node);
		}
		
		//if value when left -> right rotation it fixes child then rotates right
		if(balance > 1 && data.compareTo(node.left.data) > 0)
		{
			node.left = leftRotation(node.left);
			return rightRotation(node);
		}
		
		//if value went right -> left rotation it fixes child then rotates left
		if(balance < -1 && data.compareTo(node.right.data) < 0)
		{
			node.right = rightRotation(node.right);
			return leftRotation(node);
		}
		
		return node;
	}
	
	/**
	 * Removes value from binary tree if it exists
	 * 
	 * @param data is the value to be removed
	 */
	public void remove(E data)
	{
		root = removeNodeWithData(root, data);
	}
	
	/**
	 * Using recursion to remove a node containing given value and
	 * performs the AVL rebalancing when it has to
	 * 
	 * @param node the subtree root
	 * @param data the value that will be removed 
	 * @return updated subtree root
	 */
	private BinaryTreeNode <E> removeNodeWithData(BinaryTreeNode <E> node, E data)
	{
		if(node == null) return null;
		
		//searches for value
		int compare = data.compareTo(node.getData());
		
		if(compare < 0)
		{
			node.setLeft(removeNodeWithData(node.left, data));
		}
		else if (compare > 0)
		{
			node.setRight(removeNodeWithData(node.right, data));
		}
		else 
		{
			if(node.left == null) return node.right;
			if(node.right == null) return node.left;
			
			BinaryTreeNode <E> min = findMin(node.right);
			node.data = min.getData();
			node.setRight(removeNodeWithData(node.right, min.data));
		}
		
		node.height = Math.max(height(node.left), height(node.right)) + 1;
		
		int balance = getBalance(node);
		
		//if its left heavy and left subtree is stable
		//rotates right
		if(balance > 1 && getBalance(node.left) >= 0)
		{
			return rightRotation(node);
		}
		
		//if left heavy and subtree leans right, rotates right
		if(balance > 1 && getBalance(node.left) < 0)
		{
			node.left = leftRotation(node.left);
			return rightRotation(node);
		}
		
		//if right heavy and right subtree stable, rotates left
		if(balance < -1 && getBalance(node.right) <= 0)
		{
			return leftRotation(node);
		}
		
		/*
		 * if right heavy and right subtree leans left, it fixes child
		then rotates left
		*/
		if(balance < -1 && getBalance(node.right) > 0)
		{
			node.right = rightRotation(node.right);
			return leftRotation(node);
		}
		
		return node;
	}
	
	/**
	 * Finds node with minimum value in subtree
	 * 
	 * @param n the subtree root
	 * @return node that contains minimum value
	 */ 
	private BinaryTreeNode <E> findMin(BinaryTreeNode<E> n)
	{
		while(n.left != null)
		{
			n = n.left;
		}
		return n;
	}
	
	/**
	 * Searches for given target value in subtree
	 * 
	 * @param target is the value to search for
	 * @return the value, or null if not found
	 */
	public E find(E target)
	{
		BinaryTreeNode<E> n = findNodeWithData(root, target);
		
		return (n == null) ? null : n.data;
	}
	
	/**
	 * Recursively searches for the node that has the target value
	 * @param node the subtree root
	 * @param target is the value to search for
	 * @return the node that has the value, if not it returns null
	 */
	private BinaryTreeNode <E> findNodeWithData(BinaryTreeNode <E> node, E target)
	{
		if(node == null)
		{
			return null;
		}
		
		//compares target to current
		int compare = target.compareTo(node.data);
		
		if(compare == 0) return node;
		
		else if(compare < 0) return findNodeWithData(node.left, target);
		else return findNodeWithData(node.right, target);
	}
	
	/**
	 * Performs an in-order traversal of tree and connects or appends each node
	 * to the StringBuilder.
	 * 
	 * @param node the current subtree root
	 * @param sb represents the StringBuilder that stores the results
	 * @param depth is the indentation for the format
	 */
	private void inOrderTraversal(BinaryTreeNode <E> node, StringBuilder sb, int depth)
	{
		if(node == null) return;
		
		//left subtree
		inOrderTraversal(node.left, sb, depth + 1);
		
		//prints node with indentation
		sb.append(" ".repeat(depth)).append(" - ").append(node.data).append("\n");
		
		//right subtree
		inOrderTraversal(node.right, sb, depth + 1);
	}
	
	/**
	 * Returns an in-order string of the tree's contents
	 * 
	 * @return the formatted string
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		inOrderTraversal(root, sb, 0);
		
		return sb.toString();
	}
	
	/**
	 * Returns diagram format of tree structure
	 * 
	 * @return the diagram of tree, if not it returns empty
	 */
	public String printTree()
	{
		if (root == null) return "tree is empty";
		
		return printTreeDiagram(root);
	}
	
	/**
	 * Builds the printable diagram of tree using a line-based format
	 * 
	 * @param node the root node of tree
	 * @return a formatted string diagram of the tree
	 */
	private String printTreeDiagram(BinaryTreeNode<E> node)
	{
		List<String> lines = buildLines(node);
		StringBuilder sb = new StringBuilder();
		
		//makes lines into one string
		for(String line : lines)
		{
			sb.append(line).append("\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Using recursion to construct the line segments that represent the tree
	 * by connecting them to title of book.
	 * 
	 * @param node represents current node
	 * @return a list of string representing each tree level
	 */
	private List<String> buildLines(BinaryTreeNode<E> node)
	{
		if(node == null) return List.of("");
		
		//only prints out title
		String textNode = ((Books) node.getData()).getTitle();
		
		if(node.left == null && node.right == null)
		{
			return List.of(textNode);   
		}
		
		//recursively builds child diagrams
		List<String> left = buildLines(node.left);
		List<String> right = buildLines(node.right);
		
		//sets widths used for spacing
		int leftWidth = left.get(0).length();
		int rightWidth = right.get(0).length();
		
		//root row
		String root = " ".repeat(leftWidth + 1) + textNode;
		
		//string that represents diagonal connections "/" and "\"
		String leftSlash = " ";
		if(node.left != null)
		{ 
			//moves to horizontal position and prints"/"
			leftSlash = " ".repeat(leftWidth) + "/";
		}
		
		String rightSlash = " ";
		if(node.right != null)
		{
			
			rightSlash = "\\";
		}
		
		//creates space between slashes which is the width of text at root
		String slash = leftSlash + " ".repeat(textNode.length()) + rightSlash;
		
		//merges left and right subtrees 
		int max = Math.max(left.size(), right.size());
		List<String> merged = new ArrayList<>();
		
		for(int i = 0; i < max; i++)
		{
			//for uneven heights, it fills missing lines with space
			String L = i < left.size() ? left.get(i) : " ".repeat(leftWidth);
			String R = i < right.size() ? right.get(i) : " ".repeat(rightWidth);
			
			//adds spacing between L, left children, and R, right children
			merged.add(L + " ".repeat(textNode.length() + 3) + R);
		}
		
		List<String> tree = new ArrayList<>();
		
		//adds following to tree
		tree.add(root);
		tree.add(slash);
		tree.addAll(merged);
		
		return tree;
		
	}

}
