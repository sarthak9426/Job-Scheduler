public class RedBlackTreeNode {
	/**
	 * In the RedBlackTree node, we keep the key as building number and an object
	 * reference to the corresponding MinHeap node
	 * 
	 * @author sartkapo
	 */
	public int key; // Unique key as buildingNum

	public RedBlackTreeNode(int key) {
		this.key = key;
	}

	public int totalTime;

	// DummyNode to be used as failure node as well as default node
	public static final RedBlackTreeNode dummyNode = new RedBlackTreeNode(-1, RedBlackTree.COLOUR.BLACK);

	// Initialize pointers to dummyNode
	public RedBlackTreeNode parent = dummyNode;
	public RedBlackTreeNode left = dummyNode;
	public RedBlackTreeNode right = dummyNode;
	public RedBlackTree.COLOUR colour;

	public RedBlackTreeNode(int key, RedBlackTree.COLOUR colour) {
		this.key = key;
		this.colour = colour;
	}

	public MinHeapNode heapNode; // Reference to MinHeap Node

	public RedBlackTreeNode(int key, MinHeapNode heapNode) {
		this.key = key;
		this.heapNode = heapNode;
	}

}