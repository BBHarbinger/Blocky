/**********************************************************************
 *  HW4 - Blocky
 *  PROGRAM ANALYSIS
 *	
 *  For each method below:
 *    - List the data structure(s) you used. Provide a brief justification
 *    - Provide its big-O. Provide a brief justification 
 *  Only answer for 5 methods                                                 
 **********************************************************************/

//Author: Felix Yuzhou Sun


* random_init (1 pt):

Runtime analysis: random_init

Algorithm:

	We will keep choosing a random block in tree and smash it with smash ()
	until the block we choose has already reach the max depth.


Analysis:

	In the worst case, we smash every block to max depth before picking up
	a block with max depth.

	Suppose the max depth is D and number of nodes is N, we need to break 
	2^(D-1) + 2^(D-2) +...+ 2 + 1 = O(2^D) times, each BFS takes O(N).

	So the total time complexity is O(N2^D)


**********************************************************************
* getBlock (1 pt):

Runtime analysis: random_getBlock

Algorithm:

	Use BFS to traverse all nodes, keep track of the total count of the nodes as
	number and return the node with the given number.
	
	Return null if the node at given number is not found

Analysis:

	Suppose there are N nodes in the tree. The traversal of every node takes O(n).
	The runtime for getBlock() is O(n)



**********************************************************************
* swap (1 pt):

Runtime analysis: swap

Algorithm:

	First, swap the parent and Points of Block X and Y.
	
	Second, recursively update the Points of Block X and Y's decendents by the 
	calculation based on parent's point.	

Analysis:

	Let maximum depth be M. Let the depth for BlockX and BlockY be D.
	It takes constant time to swap BlockX and BlockY. Then we need to
	recursively update the Point for all their children and sub-children.

	For each children, it takes constant time to undate its Point.	
	Each internal node has 4 children. 
	There are 4^(M-D) children for each of Block X and Y in worst case.

	Therefore, the total time complexity is O(4^(M-D)).

**********************************************************************
* smash (1 pt):

Runtime analysis: smash

Algorithm:
	
	We create 4 nodes with random color and attach them to the given block.
	We set the Point of nodes based on the calulation of the parent's Points.

Analysis:
	
	It takes constant time to set up 4 children with random color and 
	attach them to the current block. 

	The runtime for smash() is O(1).



**********************************************************************
* rotate (1 pt):

Runtime analysis: rotate

Algorithm:
	
	We first build reference for the 4 subtrees of the current node and 
	their points, and reassign them back to the current node with the 
	switch of Points in a clockwise rotation manner.
	
	Then, we rucursively update the Points of the decendents based on
	calculation of their parent's Point.

Analysis:

	Step 1 takes constant time.
	
	In step 2, it takes constant time to update each child.

	Let maximum depth be M. Let the depth for current Block be D.
	Each internal node has 4 children. There are 4^(M-D) children in worst case.
	Therefore, the total time complexity is O(4^(M-D)).



**********************************************************************
* flatten (1 pt):

	Algorithm:

		Get the X of BotRight Point of the root. So we have the size, S.
		Create a S x S 2D-array.

		We use BFS to traverse the tree and update the 2D-array.
		For each tree, we get its TopLeft and BotRight Point and update
		the contains of the 2D-array within this area.

	Analysis:
		
		It takes constant time to create 2D-array/

		Let size be S and suppose there are N nodes in the tree.
		It takes O(N) time to traverse the tree.
		In worst case, it takes S^2 time to update the 2D-array (root).
		
		The total time complexity is O(NS^2)
		


**********************************************************************
* perimeter_score (0 pt):

Runtime analysis: perimeter_score

Algorithm:

    We call  flatten() and store the result in a 2D array

    We go through the array and count the number of border cells with the target color.

Analysis:

The number of cells in the array is the number of leaf nodes in the QuadTree; since it is a full 4-ary tree, we get the maximum number of leaf nodes when all the levels in the quadtree are full. Then, the number of leaf nodes is roughly equal to (3 * n)/4, with n representing the total number of nodes in the tree. Hence traversing the 2D array runs in O(n).

Since the method also call flatten(), the perimeter_score methos described above runs in Max{O(flatten) ,O(n)}



**********************************************************************