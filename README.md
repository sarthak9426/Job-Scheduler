# Job-Scheduler
Job Scheduler implemented using a Red Black Tree and a MinHeap in Java

## Description:
A company is developing a new city. At any time, they select the building with lowest executed time and construct till 5 sec. After this construction window ends, this process is repeated again by selecting the building with minimum executed time. 

All buildings has these following fields:
• buildingNum: Unique integer identifier for each building.

• executed_time: Amount of time for which the building has already been constructed.

• total_time: Total time required to complete its construction.

## Requirements:
The needed operations are:

• Print (buildingNum) -This command should print the triplet (buildingNum, executed_time, total_time).

• Print (buildingNum1, buildingNum2) - This command should print all triplets (bn, executed_time, total_time) for which buildingNum1 <= bn <= buildingNum2.

• Insert (buildingNum, total_time) where buildingNum is different from existing building numbers and executed_time will be initialized to 0.

## Implemented Data Structures:

### Min-Heap
Min-heap has been built using an array as a base data structure and uses the repeated doubling strategy in case the array becomes full. Each entry in the array is of the form MinHeapNode which is defined in the class MinHeapNode. This min heap also supports insertion of a node with duplicate key. Tie is broken by whichever building has the lowest building number stored in the redblack tree. The purpose of this Min-Heap is the retrieval of a building such that its executed time is minimum. So whenever there is a requirement to schedule construction of the city, a extractMin() call to this heap is made and the building is selected. Heapify() function is used to maintain the min-heap property.

### Red-Black Tree
The Red-Black Tree is used to maintain a height balanced binary search tree. Each node is of the type RedBlackTreeNode which stores the building number, total time, the node pointers and a reference to the min-heap node. This tree is implemented exactly like taught in the class using the (p, py and ppy) terminology. It contains the standard insert, delete function as well a search and search in range function to support the problem statement. After each insertion and deletion, there is a need to check whether the tree is adhering to the properties of red-black-tree or not, so necessary colour changes or rotations need to be performed.

Both the data structures ensure that all the operations take O(log(n)) worst case time.


