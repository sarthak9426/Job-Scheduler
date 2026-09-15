import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class risingCity {

	private static String IN_FILE;
	private static final String OUT_FILE = "output_file.txt";
	private static final Pattern COMMAND_PATTERN = Pattern.compile("(^\\d+): ([a-zA-Z]+)\\((.+)\\)");
	private FileWriter fileWriter;

	private RedBlackTree tree = new RedBlackTree();
	private MinHeap heap = new MinHeap();
	private boolean debug = false; // Used for printing execution on console for debugging
	private int timer = 0; 			// global time counter
	private boolean duplicateFlag = false; // flag used to check if a duplicate node is entered
	private int currWindowEndTime = 0;
	private int currBuildingCompletionTime = 0;
	private MinHeapNode currBuilding = null;

	public static void main(String[] args) {
		if(args.length >0)
		{
			IN_FILE = args[0];
			risingCity sim = new risingCity();
			sim.startConstruction();
		}
		else
			return;
	}

	/**
	 * Utility function to read the input file, parse it and call the sub-routines accordingly
	 */
	private void startConstruction() {
		String absolutePath = new File(IN_FILE).getAbsolutePath();
		if (debug)
			System.out.println(absolutePath);

		try (BufferedReader buffReader = new BufferedReader(new FileReader(absolutePath));
				FileWriter localWriter = new FileWriter(OUT_FILE)) {
			this.fileWriter = localWriter;

			String inputLine;
			String[] params;

			while ((inputLine = buffReader.readLine()) != null) {
				if (debug)
					System.out.println("Time:" + timer);
				Matcher m = COMMAND_PATTERN.matcher(inputLine);
				if (debug)
					System.out.println(inputLine);

				if (m.find() == true) {
					// If pattern matches split the input line into different variables
					int executionTime = Integer.parseInt(m.group(1));
					String cmdType = m.group(2);
					String args = m.group(3);
					params = args.split(",");

					// Keep on looping until the global time counter becomes equal to the
					// executionTime
					while (timer >= 0 && timer != executionTime) {
						execute();
						incrementTime();
						if (debug)
							System.out.println("Time:" + timer);
					}

					if (debug)
						System.out.println("StartedProcessing:" + m.group(1) + ", Command " + cmdType);
					switch (cmdType) {
					case "PrintBuilding": {
						print(params);
						break;
					}
					case "Insert": {
						if (insertBuilding(params)) {
							printDuplicateError();
							return;
						}
						break;
					}
					}
				}
				// if no input found at current time, continue execution
				execute();
				incrementTime();
			}
			executeRemainingBuildings();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sub-routine to increment the global time counter
	 */
	private void incrementTime() {
		timer += 1;
		if (currBuilding != null)
			currBuilding.key = currBuilding.key + 1;
	}

	/**
	 * Sub-routine to insert a new building into the MinHeap and RedBlackTree
	 * 
	 * @param params
	 * @return
	 */
	private boolean insertBuilding(String[] params) {
		int buildNum = Integer.parseInt(params[0]);
		RedBlackTreeNode rbTreeNode = new RedBlackTreeNode(buildNum);
		MinHeapNode heapNode = new MinHeapNode(0);
		int totalTime = Integer.parseInt(params[1]);
		rbTreeNode.totalTime = totalTime;
		rbTreeNode.heapNode = heapNode;
		heapNode.rbNode = rbTreeNode;
		duplicateFlag = tree.insertNode(rbTreeNode);
		heap.insert(heapNode);
		if (debug)
			System.out.println(
					"Inserted buildingNum: " + buildNum + ", total time:" + totalTime + " duplicate" + duplicateFlag);
		return duplicateFlag;
	}

	/**
	 * Utility function to print into the output file
	 * 
	 * @param params
	 * @throws IOException
	 */
	private void print(String[] params) throws IOException {
		if (params.length == 1) {
			int buildNum = Integer.parseInt(params[0]);
			RedBlackTreeNode rbTreeNode = tree.search(buildNum);
			if (rbTreeNode == null)
				printZeroes();
			else
				printInFormat(rbTreeNode);
		} else {
			int buildNum1 = Integer.parseInt(params[0]);
			int buildNum2 = Integer.parseInt(params[1]);
			List<RedBlackTreeNode> list = tree.searchInRange(buildNum1, buildNum2);
			if (list.isEmpty() != true) {
				StringBuilder sb = new StringBuilder();
				for (RedBlackTreeNode node : list)
					sb.append("(" + node.key + "," + node.heapNode.key + "," + node.totalTime + "),");
				sb.deleteCharAt(sb.length() - 1);
				sb.append("\n");
				fileWriter.write(sb.toString());
			} else
				printZeroes();
		}
	}

	/**
	 * Sub-routine to construct remaining buildings in the tree
	 * 
	 * @throws IOException
	 */
	private void executeRemainingBuildings() throws IOException {
		while (currBuilding != null) {
			if (debug)
				System.out.println("Time:" + timer);
			execute();
			incrementTime();
		}
	}

	/**
	 * Function to construct the current building
	 * 
	 * @throws IOException
	 */
	private void execute() throws IOException {
		if (currBuilding == null) {
			if (heap.size == 0) {
				if (debug)
					System.out.println("No Building to Construct!!");
				return;
			} else {
				currBuilding = heap.extractMin();
				currBuildingCompletionTime = currBuilding.rbNode.totalTime + timer + -currBuilding.key;
				currWindowEndTime = timer + 5;
				if (debug)
					System.out.println("Construction Started:" + currBuilding.rbNode.key + " at time:" + (timer));
			}
		} else {
			if (currWindowEndTime >= currBuildingCompletionTime) {
				if (currBuildingCompletionTime == timer) {
					if (debug)
						System.out.println("Construction Completed: " + currBuilding.rbNode.key + " at time" + (timer));
					fileWriter.write("(" + currBuilding.rbNode.key + "," + (timer) + ")" + "\n");
					tree.delete(currBuilding.rbNode);
					currBuildingCompletionTime = 0;
					currWindowEndTime = 0;
					currBuilding = null;
					execute();
				}
			} else {
				if (currWindowEndTime == timer) {
					heap.insert(currBuilding);
					currWindowEndTime = 0;
					currBuildingCompletionTime = 0;
					currBuilding = null;
					execute();
				}
			}
		}
	}

	/**
	 * Function to print zeroes in the case when no building is currently in the
	 * queue
	 * 
	 * @throws IOException
	 */
	private void printZeroes() throws IOException {
		fileWriter.write( "(0,0,0)\n" );
	}

	/**
	 * Function to print error in the case when a duplicate building is inserted
	 * 
	 * @throws IOException
	 */
	private void printDuplicateError() throws IOException {
		fileWriter.write("Duplicate Input detected.. Exiting\n");
	}

	/**
	 * Function to print in file as a result of PrintBuilding command
	 * 
	 * @param node
	 * @throws IOException
	 */
	private void printInFormat(RedBlackTreeNode node) throws IOException {
		if (currBuilding.rbNode.key == node.key)
			fileWriter.write( "(" + node.key + "," + currBuilding.key + "," + node.totalTime + ")\n" );
		else
			fileWriter.write( "(" + node.key + "," + node.heapNode.key + "," + node.totalTime + ")\n" );
	}
}
