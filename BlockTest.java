import org.junit.Test;
import java.awt.*;
import static org.junit.Assert.*;

/**
 * 
 * Author: Felix Yuzhou Sun
 *
 */

public class BlockTest {

    @Test
    public void smash() {
        //Test 1 smash the root two times
        //make a new root
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        //make a copy of the root call rootBefore
        Block rootBefore = root;
        //smash the root
        root.smash(1);
        //check if the root is not the same as the rootBefore
        //Since we can't smash root again, they should be the same
        assertEquals(root, rootBefore);


        //Test 2, smash one of the children
        //smash the TopLeftTree of the root
        //Set the color of the TopLeftTree before smashing to a color that does not
        //exist in the interface
        root.getTopLeftTree().setColor(Color.MAGENTA);
        //sine the maxDepth is 1, the smash method will not divide the TopLeftTree
        root.getTopLeftTree().smash(1);

        //There should be no children of the TopLeftTree
        //We should do nothing to the TopLeftTree
        assertEquals(0, root.getTopLeftTree().children().size());
        assertEquals(Color.MAGENTA, root.getTopLeftTree().getColor());

        //Now, set the maxDepth to 2, and smash the TopLeftTree again
        root.getTopLeftTree().smash(2);
        //There should be 4 children of the TopLeftTree
        assertEquals(4, root.getTopLeftTree().children().size());

        //Test 3, smash the TopLeftTree(with children) again
        //Make a copy of the TopLeftTree
        IBlock topLeftTreeBefore = root.getTopLeftTree();
        //topLeftTreeBefore should have 4 children
        assertEquals(4, topLeftTreeBefore.children().size());
        //smash the TopLeftTree again, we should do nothing
        root.getTopLeftTree().smash(3);
        //topLeftTreeBefore and the TopLeftTree should be the same
        assertEquals(topLeftTreeBefore, root.getTopLeftTree());

    }

    @Test
    public void testChildren() {
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        root.smash(3);
        assertEquals(4, root.children().size());
        assertEquals(1, root.getBotLeftTree().depth());
    }

    @Test
    public void testRotate() {
        //Set up an empty root.
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        //Set up four children with different colors.
        Block tl = new Block(new Point(0, 0), new Point(4, 4), 0, root);
        tl.setColor(Color.BLUE);
        Block tr = new Block(new Point(4, 0), new Point(8, 4), 0, root);
        tr.setColor(Color.RED);
        Block bl = new Block(new Point(0, 4), new Point(4, 8), 0, root);
        bl.setColor(Color.YELLOW);
        Block br = new Block(new Point(4, 4), new Point(8, 8), 0, root);
        br.setColor(Color.GRAY);

        //We also create four sub-children for br with different colors.
        Block brtl = new Block(new Point(4, 4), new Point(6, 6), 0, br);
        brtl.setColor(Color.GREEN);
        Block brtr = new Block(new Point(6, 4), new Point(8, 6), 0, br);
        brtr.setColor(Color.WHITE);
        Block brbl = new Block(new Point(4, 6), new Point(6, 8), 0, br);
        brbl.setColor(Color.ORANGE);
        Block brbr = new Block(new Point(6, 6), new Point(8, 8), 0, br);
        brbr.setColor(Color.PINK);

        //Set the children of br.
        br.setTopLeftTree(brtl);
        br.setTopRightTree(brtr);
        br.setBotLeftTree(brbl);
        br.setBotRightTree(brbr);

        //Set the children of the root.
        root.setTopLeftTree(tl);
        root.setTopRightTree(tr);
        root.setBotLeftTree(bl);
        root.setBotRightTree(br);

        //Now, rotate the root clockwise.
        root.rotate();
        //Check that the children have been rotated.
        //New top left should be the old bottom left.
        assertEquals(Color.YELLOW, root.getTopLeftTree().getColor());
        //New top right should be the old top left.
        assertEquals(Color.BLUE, root.getTopRightTree().getColor());
        //New bottom left should be the old bottom right.
        assertEquals(Color.GRAY, root.getBotLeftTree().getColor());
        //New bottom right should be the old top right.
        assertEquals(Color.RED, root.getBotRightTree().getColor());

    }

    @Test
    public void testGetColor() {
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        root.setColor(Color.BLUE);
        assertEquals(Color.BLUE, root.getColor());
    }

    @Test
    public void testIsLeaf() {
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        //At this point, root has no children. The root should be a leaf.
        assertTrue(root.isLeaf());
        //After smashing the root, the root should no longer be a leaf.
        root.smash(3);
        assertFalse(root.isLeaf());
    }

    @Test
    public void testDepth() {
        Block root = new Block();
        //The root should have a depth of 0.
        assertEquals(0, root.depth());
        //Smash the root and check the depth of the children.
        root.smash(3);
        assertEquals(1, root.getTopLeftTree().depth());
    }

}