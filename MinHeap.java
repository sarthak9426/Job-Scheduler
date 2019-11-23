package cop5536;

public class MinHeap {
	/**
	 * MinHeap Implementation using the MinHeapNode
	 * 
	 * @author sartkapo
	 */
	public int max_size = 2000;
	public int size = 0;
	public MinHeapNode[] heap;

	public MinHeap() {
		heap = new MinHeapNode[1];
	}
	/**
     * Helper function to get the right child index of the given index
     * @param i
     * @return int 
     */
	private int getRightChildIndex(int i) {
		return i * 2 + 2;
	}
	/**
     * Helper function to get the left child index of the given index
     * @param i
     * @return int 
     */
	private int getLeftChildIndex(int i) {
		return i * 2 + 1;
	}
	/**
     * Helper function to get the parent index of the given index
     * @param i
     * @return int
     */
	private int getParentIndex(int i) {
		return (i - 1) / 2;
	}
	/**
     * Function to insert a node that also handles the case of equal keys(executed time)
     * @param p
     */
	public void insert(MinHeapNode p) {
		if (size > 0 && size == heap.length) {
			increaseCapacity();
		}
		int pos = size;
		size += 1;
		heap[pos] = p;
		if(heap[getParentIndex(pos)].key==heap[pos].key)
		{
			while (pos != 0 && (heap[getParentIndex(pos)].key == heap[pos].key)&&(heap[getParentIndex(pos)].rbNode.key > heap[pos].rbNode.key)) {
				MinHeapNode temp = heap[pos];
				heap[pos] = heap[getParentIndex(pos)];
				heap[getParentIndex(pos)] = temp;
				pos = getParentIndex(pos);
			}
		}
		else
			{
			while (pos != 0 && (heap[getParentIndex(pos)].key > heap[pos].key)) {
			MinHeapNode temp = heap[pos];
			heap[pos] = heap[getParentIndex(pos)];
			heap[getParentIndex(pos)] = temp;
			pos = getParentIndex(pos);
			}
		}
	}

	/**
     * Function to increase the heap size by doubling strategy 
     * @param null
     */
	private void increaseCapacity() {
		MinHeapNode[] newArray = new MinHeapNode[heap.length * 2];
		System.arraycopy(heap, 0, newArray, 0, heap.length);
		heap = newArray;
	}

	/**
	 * Heapify function to keep the MinHeap property intact
	 * @param i
	 */
	private void heapify(int i) {

		int l = getLeftChildIndex(i);
		int r = getRightChildIndex(i);
		int smallest = i;

		if (l < size && (heap[l].key < heap[i].key || (heap[l].key==heap[i].key && heap[l].rbNode.key<heap[i].rbNode.key))) {
			smallest = l;
		}
		if (r < size && (heap[r].key < heap[smallest].key || (heap[r].key==heap[smallest].key && heap[r].rbNode.key<heap[smallest].rbNode.key))) {
			smallest = r;
		}
		if (smallest != i) {
			MinHeapNode temp = heap[i];
			heap[i] = heap[smallest];
			heap[smallest] = temp;
			heapify(smallest);
		}
	}
	/**
     * Utility to extract minimum element (root) from the heap
     * @param null
     * @return min
     */
	public MinHeapNode extractMin() {
		if (size == 1) {
			MinHeapNode min = heap[0];
			heap[0] = null;
			size -= 1;
			return min;
		}
		MinHeapNode min = heap[0];
		heap[0] = heap[size - 1];
		heap[size - 1] = null;
		size -= 1;
		heapify(0);
		return min;
	}
}