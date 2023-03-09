import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Block implements IBlock {


    /**
     * Author: Felix Yuzhou Sun
     * 
     * ===Representation Invariants ===
     *
     * - children.size() == 0 or children.size() == 4
     *
     * - If this Block has children,
     *      - their max_depth is the same as that of this Block,
     *      - their size is half that of this Block,
     *      - their level is one greater than that of this Block,
     *      - their position is determined by the position and
     *        size of this Block, as defined in the Assignment handout, and
     *      - this Block's color is null
     *
     * - If this Block has no children,
     *      - its color is not null
     *      - level <= max_depth
     */

    //  The top left and bottom right points delimiting this block
    private Point topLeft;
    private Point botRight;


    /**
     *  If this block is not subdivided, <color> stores its color.
     *   Otherwise, <color> is <null> and this  block's sublocks
     *   store their individual colors.
     */
    private Color color;

    // The level of this block within the overall block structure.
    //    * The outermost block corresponding to the root of the tree is at level/depth zero.
    //    * If a block is at level i, its children are at level i+1.
    private int depth;

    private IBlock topLeftTree;
    private IBlock topRightTree;
    private IBlock botLeftTree;
    private IBlock botRightTree;

    private IBlock parent;

    /**
     * No-argument constructor. This should:
     * - Initialize the top left and bottom right to two dummy points at (0, 0)
     * - Set the depth to be 0
     * - Set all parent and child pointers to null
     *
     * Even if you don't use this constructor anywhere, it may be useful for testing.
     */
    public Block() {
        this.topLeft = new Point(0, 0);
        this.botRight = new Point(0, 0);
        this.depth = 0;
        this.parent = null;
        this.topLeftTree = null;
        this.topRightTree = null;
        this.botLeftTree = null;
        this.botRightTree = null;
        this.color = null;
    }

    // ----------------------------------------------------------
    /**
     * Create a new Quad object.
     *
     * @param topL   top left point / bound of this block
     * @param botR   bottom right point / bound of this block
     * @param depth  of this block
     * @param parent of this block
     */
    public Block(Point topL, Point botR, int depth, Block parent) {
        this.topLeft = topL;
        this.botRight = botR;
        this.depth = depth;
        this.parent = parent;
        this.topLeftTree = null;
        this.topRightTree = null;
        this.botLeftTree = null;
        this.botRightTree = null;
        this.color = null;
    }

    @Override
    public int depth() {
        return this.depth;
    }

    /**
     * smash this block into 4 sub block of random color. the depth of the new
     * blocks should be less than maximum depth
     *
     * @param maxDepth the max depth of this board/quadtree
     */
    @Override
    public void smash(int maxDepth) {
        //Smashing the top-level block is not allowed when the game already has
        //smashed blocks, where depth == 0
        // If the block has already reached the maximum depth, do nothing
        if ((this.depth == 0 && this.topLeftTree != null) || this.depth == maxDepth) {
            return;
        }
        
        
        Random rand = new Random();
        // If the block has no sub-blocks, generate new sub-blocks and assign them random colors
        if (topLeftTree == null && topRightTree == null
                && botLeftTree == null && botRightTree == null) {
            
            //Set its color to null
            this.color = null;
            //Set the color of each sub-block to a random color
            int colorTL = rand.nextInt(IBlock.COLORS.length);
            int colorTR = rand.nextInt(IBlock.COLORS.length);
            int colorBR = rand.nextInt(IBlock.COLORS.length);
            int colorBL = rand.nextInt(IBlock.COLORS.length);

            //Calculate the midpoints of the block
            int midX = (this.topLeft.getX() + this.botRight.getX()) / 2;
            int midY = (this.topLeft.getY() + this.botRight.getY()) / 2;

            //Create the sub-blocks
            topLeftTree = new Block(this.topLeft, new Point(midX, midY), depth + 1, this);
            topRightTree = new Block(new Point(midX, this.topLeft.getY()),
                    new Point(this.botRight.getX(), midY), depth + 1, this);
            botRightTree = new Block(new Point(midX, midY), this.botRight, depth + 1, this);
            botLeftTree = new Block(new Point(this.topLeft.getX(), midY),
                    new Point(midX, this.botRight.getY()), depth + 1, this);

            //Assign the sub-blocks their random colors
            topLeftTree.setColor(IBlock.COLORS[colorTL]);
            topRightTree.setColor(IBlock.COLORS[colorTR]);
            botRightTree.setColor(IBlock.COLORS[colorBR]);
            botLeftTree.setColor(IBlock.COLORS[colorBL]);

        }
        //else, do nothing
    }


    /**
     * used by {@link IGame#randomInit()} random_init
     * to keep track of sub blocks.
     *
     * The children are returned in this order:
     * upper-left child (NE),
     * upper-right child (NW),
     * lower-right child (SW),
     * lower-left child (SE).
     *
     * @return the list of all the children of this block (clockwise order,
     *         starting with top left block)
     */
    @Override
    public List<IBlock> children() {
        List<IBlock> children = new ArrayList<IBlock>();
        //if there is no children, return an empty list
        if (this.topLeftTree == null) {
            return children;
        }

        children.add(this.topLeftTree);
        children.add(this.topRightTree);
        children.add(this.botRightTree);
        children.add(this.botLeftTree);
        return children;
    }

    /**
     * rotate this block clockwise.
     *
     *  To rotate, first move the children's pointers
     *  then recursively update the top left and
     *  bottom right points of each child.
     *
     *  You may want to write a helper method that
     *  takes in a Block and its new topLeft and botRight and
     *  sets these values for the current Block before calculating
     *  the values for its children and recursively setting them.
     */
    @Override
    public void rotate() {
        //If there are no children, return
        if (this.topLeftTree == null) {
            return;
        }
        //get the top left and bottom right points of the children
        Point topLeftTreeTopLeft = this.topLeftTree.getTopLeft();
        Point topLeftTreeBotRight = this.topLeftTree.getBotRight();
        Point topRightTreeTopLeft = this.topRightTree.getTopLeft();
        Point topRightTreeBotRight = this.topRightTree.getBotRight();
        Point botRightTreeTopLeft = this.botRightTree.getTopLeft();
        Point botRightTreeBotRight = this.botRightTree.getBotRight();
        Point botLeftTreeTopLeft = this.botLeftTree.getTopLeft();
        Point botLeftTreeBotRight = this.botLeftTree.getBotRight();

        //Rotate the children
        IBlock newTopLeft = this.botLeftTree;
        IBlock newTopRight = this.topLeftTree;
        IBlock newBotRight = this.topRightTree;
        IBlock newBotLeft = this.botRightTree;

        this.topLeftTree = newTopLeft;
        this.topRightTree = newTopRight;
        this.botRightTree = newBotRight;
        this.botLeftTree = newBotLeft;

        //set the top left and bottom right points of the children
        ((Block) this.topLeftTree).setTopLeft(topLeftTreeTopLeft);
        ((Block) this.topLeftTree).setBotRight(topLeftTreeBotRight);
        ((Block) this.topRightTree).setTopLeft(topRightTreeTopLeft);
        ((Block) this.topRightTree).setBotRight(topRightTreeBotRight);
        ((Block) this.botRightTree).setTopLeft(botRightTreeTopLeft);
        ((Block) this.botRightTree).setBotRight(botRightTreeBotRight);
        ((Block) this.botLeftTree).setTopLeft(botLeftTreeTopLeft);
        ((Block) this.botLeftTree).setBotRight(botLeftTreeBotRight);


        //Recursively set the points of the sub-children
        this.rotateHelper(this.topLeftTree, topLeftTreeTopLeft, topLeftTreeBotRight);
        this.rotateHelper(this.topRightTree, topRightTreeTopLeft, topRightTreeBotRight);
        this.rotateHelper(this.botRightTree, botRightTreeTopLeft, botRightTreeBotRight);
        this.rotateHelper(this.botLeftTree, botLeftTreeTopLeft, botLeftTreeBotRight);

    }

    /**
     * Recursively set the points of the children of the block
     */
    public void rotateHelper(IBlock block, Point topLeft, Point botRight) {

        if (block == null) {
            return;
        }

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
            rotateHelper(block.getTopLeftTree(), block.getTopLeftTree().getTopLeft(),
                    block.getTopLeftTree().getBotRight());
            rotateHelper(block.getTopRightTree(), block.getTopRightTree().getTopLeft(),
                    block.getTopRightTree().getBotRight());
            rotateHelper(block.getBotRightTree(), block.getBotRightTree().getTopLeft(),
                    block.getBotRightTree().getBotRight());
            rotateHelper(block.getBotLeftTree(), block.getBotLeftTree().getTopLeft(),
                    block.getBotLeftTree().getBotRight());
        }
    }

    /*
     * ========================
     *  Block getters
     *  You should implement these yourself.
     *  The implementations should be very simple.
     * ========================
     */

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Point getTopLeft() {
        return  topLeft;
    }

    @Override
    public Point getBotRight() {
        return botRight;
    }

    @Override
    public boolean isLeaf() {
        //If it has no children, then it is a leaf, return true
        //else, return false
        return this.topLeftTree == null;
    }

    @Override
    public IBlock getTopLeftTree() {
        return this.topLeftTree;
    }

    @Override
    public IBlock getTopRightTree() {
        return this.topRightTree;
    }

    @Override
    public IBlock getBotLeftTree() {
        return this.botLeftTree;
    }

    @Override
    public IBlock getBotRightTree() {
        return this.botRightTree;
    }


    /*
     * ========================
     *  Provided setters
     *  Don't delete these!
     *  Necessary for testing.
     * ========================
     */

    @Override
    public void setColor(Color c) {
        this.color = c;
    }

    public void setTopLeftTree(IBlock topLeftTree) {
        this.topLeftTree = topLeftTree;
    }

    public void setTopRightTree(IBlock topRightTree) {
        this.topRightTree = topRightTree;
    }

    public void setBotLeftTree(IBlock botLeftTree) {
        this.botLeftTree = botLeftTree;
    }

    public void setBotRightTree(IBlock botRightTree) {
        this.botRightTree = botRightTree;
    }

    public void setParent(IBlock parent) {
        this.parent = parent;
    }

    public void setTopLeft(Point topLeft) {
        this.topLeft = topLeft;
    }

    public void setBotRight(Point botRight) {
        this.botRight = botRight;
    }
    /**
     * @return the parent of this block
     */
    public IBlock getParent() {
        return this.parent;
    };
}
