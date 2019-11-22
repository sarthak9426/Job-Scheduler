import java.util.*;

/**
 * Red-Black Tree implementation using RedBalckTreeNode
 * 
 * @author sartkapo
 */
public class RedBlackTree {

	public RedBlackTreeNode dummy;
	public RedBlackTreeNode root;
	public boolean dupInsertFlag=false;

	public RedBlackTree() {
		dummy = RedBlackTreeNode.dummyNode;
		root = dummy;
		root.left = dummy;
		root.right = dummy;
	}

	public enum COLOUR {
		RED, BLACK
	}

//	public void insert(int key) {
//		RedBlackTreeNode p = new RedBlackTreeNode(key);
//		insertNode(p);
//	}

	public boolean insertNode(RedBlackTreeNode p) {
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

	private void insertUtil(RedBlackTreeNode root, RedBlackTreeNode p) {
		if(p.key == root.key)
		{
			dupInsertFlag=true;
		}
		else if (p.key < root.key) {
			if (root.left == dummy) {
				root.left = p;
				p.parent = root;
			} else
				insertUtil(root.left, p);
		} else {
			if (root.right == dummy) {
				root.right = p;
				p.parent = root;
			} else {
				insertUtil(root.right, p);
			}
		}
	}

	private void insertFix(RedBlackTreeNode p) {
		RedBlackTreeNode pp = dummy;
		RedBlackTreeNode gp = dummy;
		if (p.key == root.key) {
			p.colour = COLOUR.BLACK;
			return;
		}
		while (root.key != p.key && p.colour != COLOUR.BLACK && p.parent.colour == COLOUR.RED) {
			pp = p.parent;
			gp = pp.parent;

			if (pp == gp.left) {
				RedBlackTreeNode u = gp.right;
				if (u != dummy && u.colour == COLOUR.RED) {
					gp.colour = COLOUR.RED;
					pp.colour = COLOUR.BLACK;
					u.colour = COLOUR.BLACK;
					p = gp;
				} else {
					if (pp.right == p) {
						pp = leftRotate(pp);
						p = pp.left;
					}
					rightRotate(gp);
					swapColour(pp, gp);
					p = pp;
				}
			} else if (pp == gp.right) {
				RedBlackTreeNode u = gp.left;
				if (u != dummy && u.colour == COLOUR.RED) {
					gp.colour = COLOUR.RED;
					pp.colour = COLOUR.BLACK;
					u.colour = COLOUR.BLACK;
					p = gp;
				} else {
					if (pp.left == p) {
						pp = rightRotate(pp);
						p = pp.right;
					}
					leftRotate(gp);
					swapColour(pp, gp);
					p = pp;
				}
			}
		}
		root.colour = COLOUR.BLACK;
	}

	private RedBlackTreeNode leftRotate(RedBlackTreeNode a) {
		RedBlackTreeNode b = a.right;
		RedBlackTreeNode al = a.left;
		RedBlackTreeNode bl = b.left;
		RedBlackTreeNode br = b.right;

		a.right = bl;
		if (bl != dummy) {
			bl.parent = a;
		}

		b.parent = a.parent;

		if (a.parent == dummy) {
			root = b;
		} else if (a == a.parent.left) {
			a.parent.left = b;
		} else {
			a.parent.right = b;
		}
		b.left = a;
		a.parent = b;
		return b;
	}

	private RedBlackTreeNode rightRotate(RedBlackTreeNode a) {

		RedBlackTreeNode b = a.left;
		RedBlackTreeNode ar = a.right;
		RedBlackTreeNode bl = b.left;
		RedBlackTreeNode br = b.right;

		a.left = br;
		if (br != dummy) {
			br.parent = a;
		}

		b.parent = a.parent;

		if (a.parent == dummy) {
			root = b;
		} else if (a == a.parent.left) {
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

	private void swapColour(RedBlackTreeNode pp, RedBlackTreeNode gp) {
		COLOUR temp = pp.colour;
		pp.colour = gp.colour;
		gp.colour = temp;
	}

//	public static void main(String[] args) {
//
//		RedBlackTree rb = new RedBlackTree();
//		rb.insert(1);
//		rb.insert(2);
//		rb.insert(3);
//		rb.insert(4);
//		System.out.println(rb.searchInRange(8,9));
//	}
}
