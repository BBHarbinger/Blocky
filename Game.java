import java.awt.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;


/**
 * 
 * Author: Felix Yuzhou Sun
 *
 */
public class Game implements IGame {

    private int maxDepth;
    private Color target;
    private IBlock root;


    public Game(int maxDepth, Color target) {
        this.maxDepth = maxDepth;
        this.target = target;
        this.root = randomInit();
    }


    /**
     * @return the maximum dept of this blocky board.
     */
    @Override
    public int maxDepth() {
        return this.maxDepth;
    }

    /**
     * initializes a random board game. Details about how to approach
     * this are available in the assignment instructions; there is no
     * specific output that you need to generate, but calls to this
     * method should generally result in "interesting" game boards.
     *
     * @return the root of the tree of blocks
     */
    @Override
    public IBlock randomInit() {
        //create a new root block
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        //Smash the root
        root.smash(this.maxDepth());
        this.setRoot(root);
        //Create a new Random
        Random rand = new Random();
        //Randomly smash one of the blocks until the one we try to smash
        //reached the maximum depth
        while (true) {
            int randomBlock = rand.nextInt(this.countBlocks(root));
            if (this.getBlock(randomBlock).depth() == this.maxDepth) {
                break;
            }
            this.getBlock(randomBlock).smash(this.maxDepth());
        }
        //Return the root
        return root;
    }

    /**
     * Traverse the tree of blocks to find a sub block based on its id
     *
     * @param pos the id of the block
     * @return the block with id pos or null
     */
    @Override
    public IBlock getBlock(int pos) {

        //If pos < 0, then it is invalid, return null
        if (pos < 0) {
            return null;
        }

        //If the pos = 0 we return the root
        if (pos == 0) {
            return this.getRoot();
        }

        //We Use BFS to traverse the tree and count the number/id of the block
        //We use a queue to store the blocks
        Queue<IBlock> queue = new ArrayDeque<IBlock>();
        //We add the root to the queue
        queue.add(this.root);
        //We don't count the root. Initially the count is -1.
        int count = -1;

        //We loop until the queue is empty, everytime we remove a block from the
        //queue we add its children to the queue
        while (!queue.isEmpty()) {
            //We first increment the count
            count += 1;
            //We pop the first block from the queue
            IBlock block = queue.poll();

            //We check if the block is the one we are looking for
            if (count == pos) {
                return block;
            }
            //We add the children of the block to the queue in clockwise order
            List<IBlock> children = block.children();
            //if children is not empty we add the children to the queue
            if (children != null) {
                queue.addAll(children);
            }

        }
        //Else, the pos is greater than the number of blocks in the tree
        //We return null
        return null;
    }

    /**
     * This method use the BFS algorithm to traverse the tree and count the number
     *
     * @return the number of blocks in the tree
     */
    public int countBlocks(IBlock root) {
        //We Use BFS to traverse the tree and count the number/id of the block
        //We use a queue to store the blocks
        Queue<IBlock> queue = new ArrayDeque<IBlock>();
        //We add the root to the queue
        queue.add(this.getRoot());
        //We don't count the root. Initially the count is -1.
        int count = -1;

        //We loop until the queue is empty, everytime we remove a block from the
        //queue we add its children to the queue
        while (!queue.isEmpty()) {
            //We first increment the count
            count += 1;
            //We pop the first block from the queue
            IBlock block = queue.poll();

            //We add the children of the block to the queue in clockwise order
            List<IBlock> children = block.children();
            //if children is not empty we add the children to the queue
            if (children != null) {
                queue.addAll(children);
            }
        }
        return count;
    }

    /**
     * @return the root of the quad tree representing this
     * blockly board
     *
     * @implNote getRoot() == getBlock(0)
     */
    @Override
    public IBlock getRoot() {
        return this.root;
    }

    /**
     * The two blocks must be at the same level / have the same size.
     * We should be able to swap a block with no sub blocks with
     * one with sub blocks.
     *
     *
     * @param x the block to swap
     * @param y the other block to swap
     */
    @Override
    public void swap(int x, int y) {
        //We get the blocks with id x and y
        IBlock blockX = this.getBlock(x);
        IBlock blockY = this.getBlock(y);

        // In these conditions:
        // 1. If either blockX or blockY is null
        // 2. X and Y are the same block
        // 3. X and Y are not at the same level
        // we return and do nothing
        if (blockX == null || blockY == null || x == y || blockX.depth() != blockY.depth()) {
            return;
        }

        //first we swap the parent of the blocks
        IBlock parentX = ((Block) blockX).getParent();
        IBlock parentY = ((Block) blockY).getParent();
        ((Block)blockX).setParent(parentY);
        ((Block)blockY).setParent(parentX);
        //We get the index of the block in the parent's children list
        int moduleX = x % 4;
        int moduleY = y % 4;

        switch (moduleX) {
            case 1:
                ((Block) parentX).setTopLeftTree(blockY);
                break;
            case 2:
                ((Block) parentX).setTopRightTree(blockY);
                break;
            case 3: 
                ((Block) parentX).setBotRightTree(blockY);
                break;
            case 0: 
                ((Block) parentX).setBotLeftTree(blockY);
                break;
            default:
                break;
        }
        switch (moduleY) {
            case 1:
                ((Block) parentY).setTopLeftTree(blockX);
                break;
            case 2:
                ((Block) parentY).setTopRightTree(blockX);
                break;
            case 3:
                ((Block) parentY).setBotRightTree(blockX);
                break;
            case 0:
                ((Block) parentY).setBotLeftTree(blockX);
                break;
            default:
                break;
        }

        //We get each other's points
        Point pointXTopLeft = blockX.getTopLeft();
        Point pointXBottomRight = blockX.getBotRight();
        Point pointYTopLeft = blockY.getTopLeft();
        Point pointYBottomRight = blockY.getBotRight();

        //We swap the points
        ((Block)blockX).setTopLeft(pointYTopLeft);
        ((Block)blockX).setBotRight(pointYBottomRight);
        ((Block)blockY).setTopLeft(pointXTopLeft);
        ((Block)blockY).setBotRight(pointXBottomRight);

        //Reset the points of the children of blockX and blockY
        swapHelper(blockX, blockX.getTopLeft(), blockX.getBotRight());
        swapHelper(blockY, blockY.getTopLeft(), blockY.getBotRight());


    }
    /**
     * Recursively set the points of the children of the block
     */
    public void swapHelper(IBlock block, Point topLeft, Point botRight) {

        int midX = (block.getTopLeft().getX() + block.getBotRight().getX()) / 2;
        int midY = (block.getTopLeft().getY() + block.getBotRight().getY()) / 2;

        //If the children is not empty, we set the points of each of children
        if (!block.children().isEmpty()) {

            ((Block) block.getTopLeftTree()).setTopLeft(topLeft);
            ((Block) block.getTopLeftTree()).setBotRight(new Point(midX, midY));

            ((Block) block.getTopRightTree()).setTopLeft(new Point(midX, topLeft.getY()));
            ((Block) block.getTopRightTree()).setBotRight(new Point(botRight.getX(), midY));

            ((Block) block.getBotRightTree()).setTopLeft(new Point(midX, midY));
            ((Block) block.getBotRightTree()).setBotRight(botRight);

            ((Block) block.getBotLeftTree()).setTopLeft(new Point(topLeft.getX(), midY));
            ((Block) block.getBotLeftTree()).setBotRight(new Point(midX, botRight.getY()));

            //recursively set the points of the children of the children
            swapHelper(block.getTopLeftTree(), block.getTopLeftTree().getTopLeft(),
                    block.getTopLeftTree().getBotRight());
            swapHelper(block.getTopRightTree(), block.getTopRightTree().getTopLeft(),
                    block.getTopRightTree().getBotRight());
            swapHelper(block.getBotRightTree(), block.getBotRightTree().getTopLeft(),
                    block.getBotRightTree().getBotRight());
            swapHelper(block.getBotLeftTree(), block.getBotLeftTree().getTopLeft(),
                    block.getBotLeftTree().getBotRight());
        }
    }

    /**
     * Turns (flattens) the quadtree into a 2D-array of blocks.
     * Each cell in the array represents a unit cell.
     * This method should not mutate the tree.
     * @return a 2D array of the tree
     */
    @Override
    public IBlock[][] flatten() {
        //If the root is null, return null and do nothing
        if (root == null) {
            return null;
        }
        
        //Get the size of the root square
        int size = this.getRoot().getBotRight().getX();
        //Get the depth of the root square
        int depth = this.maxDepth;
        int sizeArray = (int) Math.pow(2, depth);
        
        IBlock[][] twoDArray = new IBlock[sizeArray][sizeArray];
        int[][] tracker = new int[sizeArray][sizeArray];
        
        //In case the size is greater than array size, we calculate the ratio
        int ratio = size / sizeArray;

        //Use BFS to put the objects in twoDarray one by one
        //We Use BFS to traverse the tree and count the number/id of the block
        //We use a queue to store the blocks
        Queue<IBlock> queue = new ArrayDeque<IBlock>();
        //We add the root to the queue
        queue.add(this.root);
        //We don't count the root. Initially the count is -1.
        int count = -1;

        //We loop until the queue is empty, everytime we remove a block from the
        //queue we add its children to the queue
        while (!queue.isEmpty()) {
            //We first increment the count
            count += 1;
            //We pop the first block from the queue
            IBlock block = queue.poll();
            //We check if the block is the one we are looking for
            Point blockTopLeft = block.getTopLeft();
            Point blockBottomRight = block.getBotRight();
            //put the objects into the
            for (int x = blockTopLeft.getX() / ratio; x < blockBottomRight.getX() / ratio; x++) {
                for (int y = blockTopLeft.getY() / ratio; y < blockBottomRight.getY() 
                        / ratio; y++) {
                    twoDArray[y][x] = block;
                    tracker[y][x] = count;
                }
            }
            if (blockTopLeft.getX() / ratio == blockBottomRight.getX() / ratio
                    && blockTopLeft.getY() / ratio == blockBottomRight.getY() / ratio) {
                tracker[blockBottomRight.getY()][blockBottomRight.getX()] = count;
                twoDArray[blockBottomRight.getY()][blockBottomRight.getX()] = block;

            }

            //We add the children of the block to the queue in clockwise order
            List<IBlock> children = block.children();
            //if children is not empty we add the children to the queue
            if (children != null) {
                for (IBlock child : children) {
                    queue.add(child);
                }
            }

        }
        StringBuilder sb = new StringBuilder();
        for (int[] s1 : tracker) {
            sb.append(Arrays.toString(s1)).append('\n');
        }
        return twoDArray;
    }

    /**
     * computes the scores based on perimeter blocks of the same color
     * as the target color.
     * The quadtree must be flattened first
     *
     * @return the score of the user (corner blocs count twice)
     */
    @Override
    public int perimeterScore() {

        if (this.flatten() == null) {
            return 0;
        }

        //Get size
        int size = (int) Math.pow(2, this.maxDepth);
        IBlock[][] currentGame = this.flatten();
        int score = 0;
        
        IBlock[] topRow = currentGame[0];
        IBlock[] botRow = currentGame[size - 1];

        //Calculate the score on top
        for (IBlock b : topRow) {
            if (b.getColor().equals(target)) {
                score += 1;
            }
        }
        //The score on bottom
        for (IBlock b : botRow) {
            if (b.getColor().equals(target)) {
                score += 1;
            }
        }
        //The score at left and right
        for (int y = 0; y < size; y++) {
            if (currentGame[y][0].getColor().equals(target)) {
                score += 1;
            }
            if (currentGame[y][size - 1].getColor().equals(target)) {
                score += 1;
            }

        }
        return score;
    }

    /**
     * This method will be useful to test your code
     * @param root the root of this blocky board
     */
    @Override
    public void setRoot(IBlock root) {
        this.root = root;
    }
}
