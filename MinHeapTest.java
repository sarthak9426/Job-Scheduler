import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MinHeap}: min ordering by executed time, tie-breaking by
 * building number, array growth, and extractMin behaviour.
 */
class MinHeapTest {

	/** Builds a heap node whose executed time is {@code key} and links a red-black node with building number {@code buildingNum}. */
	private static MinHeapNode node(int key, int buildingNum) {
		MinHeapNode heapNode = new MinHeapNode(key);
		RedBlackTreeNode rbNode = new RedBlackTreeNode(buildingNum);
		heapNode.rbNode = rbNode;
		rbNode.heapNode = heapNode;
		return heapNode;
	}

	@Test
	void extractMinReturnsSmallestExecutedTime() {
		MinHeap heap = new MinHeap();
		heap.insert(node(30, 1));
		heap.insert(node(10, 2));
		heap.insert(node(20, 3));

		assertEquals(10, heap.extractMin().key);
		assertEquals(20, heap.extractMin().key);
		assertEquals(30, heap.extractMin().key);
		assertEquals(0, heap.size);
	}

	@Test
	void extractMinBreaksTiesByLowerBuildingNumber() {
		MinHeap heap = new MinHeap();
		heap.insert(node(5, 40));
		heap.insert(node(5, 10));
		heap.insert(node(5, 25));

		// All share executed time 5, so the lowest building number wins each time.
		assertEquals(10, heap.extractMin().rbNode.key);
		assertEquals(25, heap.extractMin().rbNode.key);
		assertEquals(40, heap.extractMin().rbNode.key);
	}

	@Test
	void singleElementExtractLeavesEmptyHeap() {
		MinHeap heap = new MinHeap();
		heap.insert(node(7, 1));

		assertEquals(7, heap.extractMin().key);
		assertEquals(0, heap.size);
	}

	@Test
	void heapGrowsBeyondInitialCapacityAndKeepsOrder() {
		MinHeap heap = new MinHeap();
		// Insert descending keys well past the initial capacity of 1 to force doubling.
		for (int i = 50; i >= 1; i--) {
			heap.insert(node(i, i));
		}
		assertEquals(50, heap.size);

		// They must come out in ascending executed-time order.
		int previous = Integer.MIN_VALUE;
		while (heap.size > 0) {
			int current = heap.extractMin().key;
			org.junit.jupiter.api.Assertions.assertTrue(current >= previous,
					"extractMin returned out-of-order key: " + current + " after " + previous);
			previous = current;
		}
	}

	@Test
	void insertAtRootDoesNotTouchParentSlot() {
		// Regression: the first insert must not read a parent slot (index 0 has no parent).
		MinHeap heap = new MinHeap();
		heap.insert(node(1, 1));
		assertEquals(1, heap.size);
		assertEquals(1, heap.extractMin().key);
	}

	@Test
	void reinsertingAConstructedBuildingIsOrderedCorrectly() {
		// Mirrors risingCity re-inserting a building after its 5s window.
		MinHeap heap = new MinHeap();
		heap.insert(node(2, 5));
		MinHeapNode reinserted = node(6, 5);
		heap.insert(node(4, 9));
		heap.insert(reinserted);

		assertEquals(2, heap.extractMin().key);
		assertEquals(4, heap.extractMin().key);
		assertEquals(6, heap.extractMin().key);
		assertNull(safeExtract(heap));
	}

	private static MinHeapNode safeExtract(MinHeap heap) {
		return heap.size == 0 ? null : heap.extractMin();
	}
}
