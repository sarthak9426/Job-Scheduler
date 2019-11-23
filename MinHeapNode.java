package cop5536;

public class MinHeapNode {
	/**
	 * In the MinHeap node, we just keep the key which will be the executed time
	 * value and an object reference to the corresponding red black tree node
	 * 
	 * @author sartkapo
	 */

	public int key; 				// Executed time
	public RedBlackTreeNode rbNode; // Object reference of Red-Black Node

	public MinHeapNode(int k) {
		this.key = k;
	}
}
