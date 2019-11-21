public class MinHeap {
	/**
	 * Min-Heap Implementation using the MinHeapNode
	 * 
	 * @author sartkapo
	 */
	public int max_size = 2000;
	public int size = 0;
	public MinHeapNode[] heap;

	public MinHeap() {
		heap = new MinHeapNode[1];
	}

	private int getRightChildIndex(int i) {
		return i * 2 + 2;
	}

	private int getLeftChildIndex(int i) {
		return i * 2 + 1;
	}

	private int getParentIndex(int i) {
		return (i - 1) / 2;
	}

	public void insert(int key) {
		MinHeapNode p = new MinHeapNode(key);
		insertUtil(p);
	}

	public void insert(MinHeapNode p) {
		insertUtil(p);
	}

	private void insertUtil(MinHeapNode p) {
		if (size > 0 && size == heap.length) {
			increaseCapacity();
		}
		int pos = size;
		size += 1;
		heap[pos] = p;
		if(heap[getParentIndex(pos)].key==heap[pos].key)
		{
			while (pos != 0 && (heap[getParentIndex(pos)].key > heap[pos].key)&&(heap[getParentIndex(pos)].rbNode.key > heap[pos].rbNode.key)) {
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

	private void increaseCapacity() {
		int newSize = heap.length * 2;
		MinHeapNode[] newArray = new MinHeapNode[newSize];
		System.arraycopy(heap, 0, newArray, 0, heap.length);
		heap = newArray;
	}

	/**
	 * Heapify function to keep the minHeap property intact
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

	public MinHeapNode extractMin() {
		if (size == 1) {
			MinHeapNode min = heap[0];
			size -= 1;
			heap[0] = null;
			return min;
		}

		MinHeapNode min = heap[0];
		heap[0] = heap[size - 1];
		heap[size - 1] = null;
		size -= 1;
		heapify(0);
		return min;
	}

//	public static void main(String[] args) {
//		MinHeap heap = new MinHeap();
//		heap.insert(100);
//		heap.extractMin();
//		heap.insert(52);
//		heap.insert(332);
//		heap.insert(512);
//		heap.extractMin();
//		System.out.print(heap.extractMin().key);
//	}
}