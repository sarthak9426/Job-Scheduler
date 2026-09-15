import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link RedBlackTree}: BST search, range search, duplicate
 * detection, and, most importantly, verification that the five red-black
 * invariants hold after arbitrary sequences of inserts and deletes.
 */
class RedBlackTreeTest {

	private static RedBlackTreeNode newNode(int key) {
		RedBlackTreeNode n = new RedBlackTreeNode(key);
		n.totalTime = key * 10;
		return n;
	}

	@Test
	void searchFindsInsertedKeysAndMissesAbsentOnes() {
		RedBlackTree tree = new RedBlackTree();
		for (int k : new int[] {5, 2, 9, 1, 7, 3}) {
			tree.insertNode(newNode(k));
		}
		assertNotNull(tree.search(7));
		assertEquals(7, tree.search(7).key);
		assertNull(tree.search(100));
	}

	@Test
	void duplicateInsertIsFlagged() {
		RedBlackTree tree = new RedBlackTree();
		assertFalse(tree.insertNode(newNode(5)), "first insert of 5 is not a duplicate");
		assertTrue(tree.insertNode(newNode(5)), "second insert of 5 must be flagged duplicate");
	}

	@Test
	void rangeSearchReturnsSortedInclusiveWindow() {
		RedBlackTree tree = new RedBlackTree();
		for (int k : new int[] {10, 20, 30, 40, 50}) {
			tree.insertNode(newNode(k));
		}
		List<RedBlackTreeNode> result = tree.searchInRange(20, 40);
		List<Integer> keys = new ArrayList<>();
		for (RedBlackTreeNode n : result) {
			keys.add(n.key);
		}
		assertEquals(List.of(20, 30, 40), keys);
	}

	@Test
	void rangeSearchOnEmptyWindowReturnsEmptyList() {
		RedBlackTree tree = new RedBlackTree();
		tree.insertNode(newNode(10));
		tree.insertNode(newNode(50));
		assertTrue(tree.searchInRange(20, 40).isEmpty());
	}

	@Test
	void deleteRemovesNodeAndReportsSuccess() {
		RedBlackTree tree = new RedBlackTree();
		for (int k : new int[] {5, 2, 9, 1, 7}) {
			tree.insertNode(newNode(k));
		}
		assertTrue(tree.delete(9));
		assertNull(tree.search(9));
		assertFalse(tree.delete(9), "deleting an absent key returns false");
	}

	@Test
	void invariantsHoldAfterSequentialInserts() {
		RedBlackTree tree = new RedBlackTree();
		for (int k = 1; k <= 100; k++) {
			tree.insertNode(newNode(k));
			assertRedBlackInvariants(tree);
		}
	}

	@Test
	void invariantsHoldUnderRandomizedInsertDeleteWorkload() {
		// Several seeds and a larger key set to exercise many rotation/recolour paths.
		for (long seed : new long[] {42L, 7L, 123L, 2024L, 99991L}) {
			Random rng = new Random(seed);
			RedBlackTree tree = new RedBlackTree();
			List<Integer> present = new ArrayList<>();

			List<Integer> keys = new ArrayList<>();
			for (int i = 1; i <= 500; i++) {
				keys.add(i);
			}
			Collections.shuffle(keys, rng);

			for (int k : keys) {
				tree.insertNode(newNode(k));
				present.add(k);
				assertRedBlackInvariants(tree);
			}

			Collections.shuffle(present, rng);
			for (int k : present) {
				assertTrue(tree.delete(k), "seed " + seed + ": expected to delete present key " + k);
				assertRedBlackInvariants(tree);
				assertNull(tree.search(k));
			}
			assertTrue(tree.root == tree.dummy,
					"seed " + seed + ": tree should be empty after deleting every key");
		}
	}

	// ---- Red-black invariant checker -------------------------------------

	/**
	 * Verifies the red-black properties on the whole tree:
	 * (1) the root is black, (2) no red node has a red child, (3) every
	 * root-to-leaf path has the same black height, and (4) the tree is a valid
	 * BST. Fails the test with a descriptive message otherwise.
	 */
	private static void assertRedBlackInvariants(RedBlackTree tree) {
		RedBlackTreeNode root = tree.root;
		RedBlackTreeNode dummy = tree.dummy;
		if (root == dummy) {
			return; // an empty tree trivially satisfies the invariants
		}
		assertEquals(RedBlackTree.COLOUR.BLACK, root.colour, "root must be black");
		checkNode(root, dummy, Integer.MIN_VALUE, Integer.MAX_VALUE);
		blackHeight(root, dummy); // throws via fail() on mismatch
	}

	/** Recursively checks the no-red-red property and BST ordering. */
	private static void checkNode(RedBlackTreeNode node, RedBlackTreeNode dummy, int low, int high) {
		if (node == dummy) {
			return;
		}
		assertTrue(node.key > low && node.key < high,
				"BST order violated at key " + node.key + " (bounds " + low + ".." + high + ")");
		if (node.colour == RedBlackTree.COLOUR.RED) {
			assertTrue(node.left.colour == RedBlackTree.COLOUR.BLACK,
					"red node " + node.key + " has red left child");
			assertTrue(node.right.colour == RedBlackTree.COLOUR.BLACK,
					"red node " + node.key + " has red right child");
		}
		checkNode(node.left, dummy, low, node.key);
		checkNode(node.right, dummy, node.key, high);
	}

	/** Returns the black height, failing the test if the two subtrees disagree. */
	private static int blackHeight(RedBlackTreeNode node, RedBlackTreeNode dummy) {
		if (node == dummy) {
			return 1; // dummy leaves are black
		}
		int left = blackHeight(node.left, dummy);
		int right = blackHeight(node.right, dummy);
		if (left != right) {
			fail("black-height mismatch at key " + node.key + ": left=" + left + " right=" + right);
		}
		return left + (node.colour == RedBlackTree.COLOUR.BLACK ? 1 : 0);
	}
}
