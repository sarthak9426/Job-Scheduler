package cop5536;

import java.util.*;

/**
 * Red-Black Tree implementation using RedBalckTreeNode
 * 
 * @author sartkapo
 */
public class RedBlackTree {

	public RedBlackTreeNode dummy;
	public RedBlackTreeNode root;
	public boolean dupInsertFlag = false; //flag to determine if a duplicate node is inserted in the tree
	/**
     * Constructor of the class
     * @param null
     * @return null
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
     * @param p
     * @return boolean
     */
	public boolean insertNode(RedBlackTreeNode p) {
		//new node is always a red node
		p.colour = COLOUR.RED;
		if (root == dummy) {
			root = p;
			root.colour = COLOUR.BLACK;
			root.parent = dummy;
			return dupInsertFlag;
		}
		insertUtil(root, p);
		insertFix(p);
		return dupInsertFlag;
	}
	/**
     * Utility function to insert which is not the root node into the Red-Black Tree
     * Insertion takes place just as an insertion in a binary search tree
     * @param root,p
     * @return
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
			} else {
				insertUtil(root.right, p);
			}
		}
	}
	/**
     * Utility function to fix the Red Black tree if any of its properties have been violated.
     * Performs rotation, exchanging of colours if needed
     * @param p
     * @return
     */
	private void insertFix(RedBlackTreeNode p) {
		RedBlackTreeNode pp = dummy;		// Initialize parent of p to dummy node
		RedBlackTreeNode gpp = dummy;		// Initialize grandparent of p to dummy node
		
		//If newly inserted node is the root node, fix it by making it colour black 
		if (p.key == root.key) {
			p.colour = COLOUR.BLACK;
			return;
		}
		
		while (root.key != p.key && p.parent.colour == COLOUR.RED && p.colour != COLOUR.BLACK ) {
			pp = p.parent;
			gpp = pp.parent;
			if (pp == gpp.left) {
				//Insertion is in the left subtree
				RedBlackTreeNode uncle = gpp.right;
				if (uncle.colour == COLOUR.RED && uncle != dummy ) {
					// case when the grandparent's right child is red
					uncle.colour = COLOUR.BLACK;
					pp.colour = COLOUR.BLACK;
					gpp.colour = COLOUR.RED;
					// swapping of colours and percolating up till the root
					p = gpp;
				} else {
					//Case when grandparent's right child is black
					if (p==pp.right) {
						//if p is inserted as the right child of pp : Case LRb
						pp = leftRotate(pp);
						p = pp.left;
					}
					//case LLB if insertion was as the left child of pp
					rightRotate(gpp);
					exchangeColour(pp, gpp);
					p = pp;
				}
			} else if (pp == gpp.right) {
				RedBlackTreeNode uncle = gpp.left;
				//Case when the insertion is in the right subtree of grandparent 
				if (uncle != dummy && uncle.colour == COLOUR.RED) {
					//Case when uncle is red i.e re-colouring is needed
					uncle.colour = COLOUR.BLACK;
					pp.colour = COLOUR.BLACK;
					gpp.colour = COLOUR.RED;
					//percolate up
					p = gpp;
				} else {
					//Case when uncle is black
					if (p==pp.left) {
						//Case RLb
						pp = rightRotate(pp);
						p = pp.right;
					}
					//Case RRb
					leftRotate(gpp);
					exchangeColour(pp, gpp);
					p = pp;
				}
			}
		}
		root.colour = COLOUR.BLACK;
	}

	private RedBlackTreeNode leftRotate(RedBlackTreeNode a) {
		RedBlackTreeNode b = a.right;
		RedBlackTreeNode bl = b.left;
		a.right = bl;
		if (bl != dummy) {
			bl.parent = a;
		}
		b.parent = a.parent;
		if (a.parent == dummy) {
			root = b;
		} 
		else if (a.parent.left==a ) {
			a.parent.left = b;
		} 
		else {
			a.parent.right = b;
		}
		b.left = a;
		a.parent = b;
		return b;
	}

	private RedBlackTreeNode rightRotate(RedBlackTreeNode a) {
		RedBlackTreeNode b = a.left;
		RedBlackTreeNode br = b.right;
		a.left = br;
		if (br != dummy) {
			br.parent = a;
		}
		b.parent = a.parent;

		if (a.parent == dummy) {
			root = b;
		} else if ( a.parent.left==a ) {
			a.parent.left = b;
		} else {
			a.parent.right = b;
		}
		b.right = a;
		a.parent = b;
		return b;
	}

	private void levelUp(RedBlackTreeNode a, RedBlackTreeNode b) {
		if (a.parent == dummy) {
			root = b;
		} else if (a == a.parent.left) {
			a.parent.left = b;
		} else {
			a.parent.right = b;
		}
		b.parent = a.parent;
	}

	public RedBlackTreeNode search(int key) {
		return searchUtil(root, key);
	}

	private RedBlackTreeNode searchUtil(RedBlackTreeNode root, int key) {
		if (root == dummy) {
			return null;
		}
		if (root.key == key) {
			return root;
		} else if (key < root.key) {
			return searchUtil(root.left, key);
		} else {
			return searchUtil(root.right, key);
		}
	}

	public boolean delete(RedBlackTreeNode p) {
		return delete(p.key);
	}

	public boolean delete(int key) {
		RedBlackTreeNode y = searchUtil(root, key);
		if (y == null) {
			return false;
		}
		RedBlackTreeNode v;
		RedBlackTreeNode temp = y;
		COLOUR origCOLOUR = y.colour;
		if (y.left == dummy) {
			v = y.right;
			levelUp(y, y.right);
		} else if (y.right == dummy) {
			v = y.left;
			levelUp(y, y.left);
		} else {
			temp = getMin(y.right);
			origCOLOUR = temp.colour;
			v = temp.right;
			if (temp.parent == y) {
				v.parent = temp;
			} else {
				levelUp(temp, temp.right);
				temp.right = y.right;
				temp.right.parent = temp;
			}
			levelUp(y, temp);
			temp.left = y.left;
			temp.left.parent = temp;
			temp.colour = y.colour;
		}
		if (origCOLOUR == COLOUR.BLACK) {
			deleteFix(v);
		}
		return true;
	}

	private void deleteFix(RedBlackTreeNode py) {

		while (py != root && py.colour == COLOUR.BLACK) {
			if (py == py.parent.left) {
				RedBlackTreeNode v = py.parent.right;

				if (v.colour == COLOUR.RED) {
					v.colour = COLOUR.BLACK;
					py.parent.colour = COLOUR.RED;
					leftRotate(py.parent);
					v = py.parent.right;
				}
				if (v.left.colour == COLOUR.BLACK && v.right.colour == COLOUR.BLACK) {
					v.colour = COLOUR.RED;
					py = py.parent;
					continue;
				} else if (v.right.colour == COLOUR.BLACK) {
					v.left.colour = COLOUR.BLACK;
					v.colour = COLOUR.RED;
					rightRotate(v);
					v = py.parent.right;
				}
				if (v.right.colour == COLOUR.RED) {
					v.colour = py.parent.colour;
					py.parent.colour = COLOUR.BLACK;
					v.right.colour = COLOUR.BLACK;
					leftRotate(py.parent);
					py = root;
				}
			} else {
				RedBlackTreeNode v = py.parent.left;
				if (v.colour == COLOUR.RED) {
					v.colour = COLOUR.BLACK;
					py.parent.colour = COLOUR.RED;
					rightRotate(py.parent);
					v = py.parent.left;
				}

				if (v.right.colour == COLOUR.BLACK && v.left.colour == COLOUR.BLACK) {
					v.colour = COLOUR.RED;
					py = py.parent;
					continue;
				} else if (v.left.colour == COLOUR.BLACK) {
					v.right.colour = COLOUR.BLACK;
					v.colour = COLOUR.RED;
					leftRotate(v);
					v = py.parent.left;
				}
				if (v.left.colour == COLOUR.RED) {
					v.colour = py.parent.colour;
					py.parent.colour = COLOUR.BLACK;
					v.left.colour = COLOUR.BLACK;
					rightRotate(py.parent);
					py = root;
				}
			}
		}
		py.colour = COLOUR.BLACK;
	}

	public List<RedBlackTreeNode> searchInRange(int key1, int key2) {
		List<RedBlackTreeNode> list = new LinkedList<RedBlackTreeNode>();
		searchInRange(root, list, key1, key2);
		return list;
	}

	private void searchInRange(RedBlackTreeNode root, List<RedBlackTreeNode> list, int key1, int key2) {
		if (root == dummy) {
			return;
		}
		if (key1 < root.key) {
			searchInRange(root.left, list, key1, key2);
		}
		if (key1 <= root.key && key2 >= root.key) {
			list.add(root);
		}
		if (key2 > root.key) {
			searchInRange(root.right, list, key1, key2);
		}
	}

	private RedBlackTreeNode getMin(RedBlackTreeNode root) {
		while (root.left != dummy) {
			root = root.left;
		}
		return root;
	}

	private void exchangeColour(RedBlackTreeNode pp, RedBlackTreeNode gpp) {
		COLOUR temp = pp.colour;
		pp.colour = gpp.colour;
		gpp.colour = temp;
	}

}