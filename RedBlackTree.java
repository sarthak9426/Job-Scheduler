import java.util.*;

/**
 * Red-Black Tree implementation using RedBlackTreeNode
 * 
 * @author sartkapo
 */
public class RedBlackTree {

	public RedBlackTreeNode dummy;
	public RedBlackTreeNode root;
	public boolean dupInsertFlag = false; // flag to determine if a duplicate node is inserted in the tree

	/**
	 * Constructor of the class
	 */
	public RedBlackTree() {
		dummy = RedBlackTreeNode.dummyNode;
		root = dummy;
		root.left = dummy;
		root.right = dummy;
	}

	/**
	 * Enum used to store colouring info of the nodes in the tree
	 */
	public enum COLOUR {
		RED, BLACK
	}

	/**
	 * Function to insert a node into the Red-Black Tree
	 * 
	 * @param p
	 * @return
	 */
	public boolean insertNode(RedBlackTreeNode p) {
		// new node is always a red node
		p.colour = COLOUR.RED;
		if (root == dummy) {
			root = p;
			root.colour = COLOUR.BLACK;
			root.parent = dummy;
			return dupInsertFlag;
		}
		insertUtil(root, p);
		fixInsert(p);
		return dupInsertFlag;
	}

	/**
	 * Utility function to insert a node which is not the root node into the Red-Black Tree
	 * @param root
	 * @param p
	 */
	private void insertUtil(RedBlackTreeNode root, RedBlackTreeNode p) {
		// if root's key is equal to the newly entered node's key, mark duplicate
		if (p.key == root.key) {
			dupInsertFlag = true;
		} 
		else if (p.key < root.key) {
			if (root.left == dummy) {
				p.parent = root;
				root.left = p;
			} 
			else
				insertUtil(root.left, p);
		} 
		else {
			if (root.right == dummy) {
				p.parent = root;
				root.right = p;
			} 
			else
				insertUtil(root.right, p);
		}
	}

	/**
	 * Utility function to fix the Red Black tree if any of its properties have been
	 * violated. Performs rotation, exchanging of colours if needed
	 * 
	 * @param p
	 * @return
	 */
	private void fixInsert(RedBlackTreeNode p) {
		RedBlackTreeNode pp = dummy; // Initialize parent of p to dummy node
		RedBlackTreeNode gpp = dummy; // Initialize grandparent of p to dummy node

		// If newly inserted node is the root node, fix it by making it colour black
		if (p.key == root.key) {
			p.colour = COLOUR.BLACK;
			return;
		}
		while (root.key != p.key && p.parent.colour == COLOUR.RED && p.colour != COLOUR.BLACK) {
			pp = p.parent;
			gpp = pp.parent;
			if (gpp.left==pp) {
				// Insertion is in the left subtree
				RedBlackTreeNode uncle = gpp.right;
				if (uncle.colour == COLOUR.RED && uncle != dummy) {
					// case when the grandparent's right child is red
					uncle.colour = COLOUR.BLACK;
					pp.colour = COLOUR.BLACK;
					gpp.colour = COLOUR.RED;
					// swapping of colours and percolating up till the root
					p = gpp;
				} 
				else {
					// Case when grandparent's right child is black
					if (p == pp.right) {
						// if p is inserted as the right child of pp : Case LRb
						pp = leftRotate(pp);
						p = pp.left;
					}
					// case LLB if insertion was as the left child of pp
					rightRotate(gpp);
					exchangeColour(pp, gpp);
					p = pp;
				}
			} 
			else if (pp == gpp.right) {
				RedBlackTreeNode uncle = gpp.left;
				// Case when the insertion is in the right subtree of grandparent
				if (uncle != dummy && uncle.colour == COLOUR.RED) {
					// Case when uncle is red i.e re-colouring is needed
					uncle.colour = COLOUR.BLACK;
					pp.colour = COLOUR.BLACK;
					gpp.colour = COLOUR.RED;
					// percolate up
					p = gpp;
				} 
				else {
					// Case when uncle is black
					if (p == pp.left) {
						// Case RLb
						pp = rightRotate(pp);
						p = pp.right;
					}
					// Case RRb
					leftRotate(gpp);
					exchangeColour(pp, gpp);
					p = pp;
				}
			}
		}
		root.colour = COLOUR.BLACK;
	}

	/**
	 * Utility function to left rotate the tree taking the given node as the root
	 * @param node
	 * @return
	 */
	private RedBlackTreeNode leftRotate(RedBlackTreeNode node) {
		RedBlackTreeNode rightSubtree = node.right;
		RedBlackTreeNode rightLeftSubtree = rightSubtree.left;
		node.right = rightLeftSubtree;
		if (rightLeftSubtree != dummy) {
			rightLeftSubtree.parent = node;
		}
		rightSubtree.parent = node.parent;
		if (node.parent == dummy)
			root = rightSubtree; 
		else if (node.parent.left == node) 
			node.parent.left = rightSubtree;
		else 
			node.parent.right = rightSubtree;
		rightSubtree.left = node;
		node.parent = rightSubtree;
		return rightSubtree;
	}

	/**
	 * Utility function to right rotate the tree taking the given node as the root
	 * 
	 * @param node
	 * @return RedBlackTreeNode
	 */
	private RedBlackTreeNode rightRotate(RedBlackTreeNode node) {
		RedBlackTreeNode leftSubtree = node.left;
		RedBlackTreeNode leftRightSubtree = leftSubtree.right;
		node.left = leftRightSubtree;
		if (leftRightSubtree != dummy)
			leftRightSubtree.parent = node;
		leftSubtree.parent = node.parent;
		if (dummy == node.parent)
			root = leftSubtree;
		else if (node.parent.left == node)
			node.parent.left = leftSubtree;
		else
			node.parent.right = leftSubtree;
		leftSubtree.right = node;
		node.parent = leftSubtree;
		return leftSubtree;
	}

	/**
	 * Function to search for a node in the tree with the given key
	 * @param key
	 * @return
	 */
	public RedBlackTreeNode search(int key) {
		return searchUtil(root, key);
	}

	/**
	 * Utility function to search the node in the tree. Works exactly like search in
	 * binary search tree
	 * @param root
	 * @param key
	 * @return
	 */
	private RedBlackTreeNode searchUtil(RedBlackTreeNode root, int key) {
		if (root == dummy)
			return null;
		if (root.key == key)
			return root;
		else if (key < root.key)
			return searchUtil(root.left, key);
		else
			return searchUtil(root.right, key);
	}

	/**
	 * Function to search all the nodes in the tree whose key lies in the given range
	 * @param key1
	 * @param key2
	 * @return
	 */
	public List<RedBlackTreeNode> searchInRange(int key1, int key2) {
		List<RedBlackTreeNode> list = new LinkedList<RedBlackTreeNode>();
		searchRangeUtil(root, list, key1, key2);
		return list;
	}

	/**
	 * Utility function to search all the nodes in the tree whose key lies in the given range
	 * @param root
	 * @param list
	 * @param k1
	 * @param k2
	 */
	private void searchRangeUtil(RedBlackTreeNode root, List<RedBlackTreeNode> list, int k1, int k2) {
		if (root == dummy)
			return;
		if (root.key > k1)
			searchRangeUtil(root.left, list, k1, k2);
		if (k1 <= root.key && k2 >= root.key)
			list.add(root);
		if (root.key < k2)
			searchRangeUtil(root.right, list, k1, k2);
	}

	/**
	 * Function to delete a node in the tree with the given reference
	 * @param p
	 * @return
	 */
	public boolean delete(RedBlackTreeNode p) {
		return delete(p.key);
	}

	/**
	 * Utility function to delete a node in the tree with the given key
	 * @param key
	 * @return
	 */
	public boolean delete(int key) {
		RedBlackTreeNode y = searchUtil(root, key);
		// If node not in tree, return false
		if (y == null)
			return false;
		RedBlackTreeNode lastNode = y;
		RedBlackTreeNode v;
		COLOUR origCOLOUR = y.colour;
		// Check if left child is dummy or not
		if (dummy == y.left) {
			v = y.right;
			percolateUp(y, y.right);
		}
		// Check if right child is dummy or not
		else if (dummy == y.right) {
			v = y.left;
			percolateUp(y, y.left);
		} 
		else {
			// Case when both child are not null- 2 degree node
			lastNode = getLeftMostNode(y.right);
			origCOLOUR = lastNode.colour;
			v = lastNode.right;
			if (y == lastNode.parent)
				v.parent = lastNode;
			else {
				percolateUp(lastNode, lastNode.right);
				lastNode.right = y.right;
				lastNode.right.parent = lastNode;
			}
			percolateUp(y, lastNode);
			lastNode.left = y.left;
			lastNode.left.parent = lastNode;
			lastNode.colour = y.colour;
		}
		// if the node deleted was of colour, tree needs to be fixed to adhere to
		// red-black tree properties
		if (COLOUR.BLACK == origCOLOUR)
			fixDelete(v);
		return true;
	}

	/**
	 * Utility function to get the minimum key element(left most) in the tree
	 * @param root
	 * @return
	 */
	private RedBlackTreeNode getLeftMostNode(RedBlackTreeNode root) {
		while (root.left != dummy)
			root = root.left;
		return root;
	}

	/**
	 * Utility function to insert node2 as the child of node1
	 * @param node1
	 * @param node2
	 */
	private void percolateUp(RedBlackTreeNode node1, RedBlackTreeNode node2) {
		if (dummy == node1.parent)
			root = node2;
		else if (node1.parent.left == node1)
			node1.parent.left = node2;
		else
			node1.parent.right = node2;
		node2.parent = node1.parent;
	}

	/**
	 * Utility function to fix by (rotations,colour changes) to adhere to the
	 * red-black tree properties
	 * @param py
	 */
	private void fixDelete(RedBlackTreeNode py) {
		while (py.colour == COLOUR.BLACK && root != py) {
			if (py.parent.left == py) {
				RedBlackTreeNode v = py.parent.right;
				if (v.colour == COLOUR.RED) {
					// if v colour is red, left rotate
					py.parent.colour = COLOUR.RED;
					v.colour = COLOUR.BLACK;
					leftRotate(py.parent);
					v = py.parent.right;
				}
				if (v.right.colour == COLOUR.BLACK && v.left.colour == COLOUR.BLACK) {
					// recolour if both the children of v are black
					py = py.parent;
					v.colour = COLOUR.RED;
					continue;
				} 
				else if (COLOUR.BLACK == v.right.colour) {
					// if v colour is black, right rotate
					v.left.colour = COLOUR.BLACK;
					v.colour = COLOUR.RED;
					rightRotate(v);
					v = py.parent.right;
				}
				if (COLOUR.RED == v.right.colour) {
					v.colour = py.parent.colour;
					py.parent.colour = COLOUR.BLACK;
					v.right.colour = COLOUR.BLACK;
					leftRotate(py.parent);
					py = root;
				}
			} 
			else {
				// if it is a right child
				RedBlackTreeNode v = py.parent.left;
				if (v.colour == COLOUR.RED) {
					// if v is red, recolour and right rotate
					py.parent.colour = COLOUR.RED;
					v.colour = COLOUR.BLACK;
					rightRotate(py.parent);
					v = py.parent.left;
				}
				if (v.left.colour == COLOUR.BLACK && v.right.colour == COLOUR.BLACK) {
					// if both child are black, just recolour
					v.colour = COLOUR.RED;
					py = py.parent;
					continue;
				} 
				else if (v.left.colour == COLOUR.BLACK) {
					// symmetric case to the left rotate case in the case where v was the left child
					v.colour = COLOUR.RED;
					v.right.colour = COLOUR.BLACK;
					leftRotate(v);
					v = py.parent.left;
				}
				if (COLOUR.RED == v.left.colour) {
					py.parent.colour = COLOUR.BLACK;
					v.colour = py.parent.colour;
					v.left.colour = COLOUR.BLACK;
					rightRotate(py.parent);
					py = root;
				}
			}
		}
		py.colour = COLOUR.BLACK;
	}

	/**
	 * Utility function to exchange colours of two given nodes
	 * @param pp
	 * @param gpp
	 */
	private void exchangeColour(RedBlackTreeNode pp, RedBlackTreeNode gpp) {
		COLOUR temp = gpp.colour;
		gpp.colour = pp.colour;
		pp.colour = temp;
	}

}