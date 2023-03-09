import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;


/**
 * 
 * Author: Felix Yuzhou Sun
 *
 */

public class GameTest {

    @Test
    public void testRandomInit() {
        //Each game should be unique
        IGame game1 = new Game(3, Color.RED);
        IGame game2 = new Game(3, Color.RED);
        //Avoid rare cases where the two games are the same
        while (game1.getRoot().equals(game2.getRoot())) {
            game2 = new Game(3, Color.RED);
        }
        assertNotEquals(game1.getRoot(), game2.getRoot());
    }

    @Test
    public void testGetBlock() {
        //Create a new Game
        IGame game = new Game(3, Color.RED);
        //Initialize a root
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        //Smash it to a depth of 3
        root.smash(3);
        //manually set the root
        game.setRoot(root);

        //Try to test the example in Step 1
        //Test 0, get -2. Should return null
        assertNull(game.getBlock(-2));

        //Test 1, get 0. Should return the root
        assertEquals(root, game.getBlock(0));

        //Test 2, get 1. Should return the TopLeftTree of the root
        assertEquals(root.getTopLeftTree(), game.getBlock(1));
        //get 4, should return the BotLeftTree of the root
        assertEquals(root.getBotLeftTree(), game.getBlock(4));

        //Test 3, get 5. Should return null
        assertNull(game.getBlock(5));

        //Test 4, get 12. Should return BottomLeftTree of the BottomLeftTree
        //Now we smash the TopLeftTree and BottomRightTree of the root
        root.getTopLeftTree().smash(3);
        root.getBotLeftTree().smash(3);
        assertEquals(root.getBotLeftTree().getBotLeftTree(), game.getBlock(12));
        assertEquals(root.getBotLeftTree().getTopRightTree(), game.getBlock(10));

        //Test 5.
        //Smash the TopLeftTree's TopRightTree
        root.getTopLeftTree().getTopRightTree().smash(3);
        //Smash the BotLeftTree's TopLeftTree
        root.getBotLeftTree().getTopLeftTree().smash(3);
        //Get 20, should return BottomLeftTree's TopLeftTree's BottomLeftTree
        assertEquals(root.getBotLeftTree().getTopLeftTree().getBotLeftTree(), game.getBlock(20));
        assertEquals(root.getBotLeftTree().getTopLeftTree().getBotRightTree(), game.getBlock(19));
        //We should get null if we get 21
        assertNull(game.getBlock(21));

    }

    @Test
    public void testCountBlocks() {
        //Create a new Game
        IGame game = new Game(3, Color.RED);
        //Initialize a root
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        //Smash it to a depth of 3
        root.smash(3);
        //manually set the root
        game.setRoot(root);
        //Smash the all the children of the root
        for (int i = 1; i <= 4; i++) {
            game.getBlock(i).smash(3);
        }
        //Count the number of blocks 4+4^2 = 20
        assertEquals(20, ((Game) game).countBlocks(game.getRoot()));
    }

    @Test
    public void swap() {
        //create a new game
        IGame game = new Game(3, Color.RED);
        //create a new root
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        //smash it to a depth of 3
        root.smash(3);
        //manually set the root
        game.setRoot(root);

        //Test 1
        //Swap leaf 1 and 4
        game.getBlock(1).setColor(Color.RED);
        game.getBlock(2).setColor(Color.BLACK);
        game.getBlock(3).setColor(Color.BLUE);
        game.getBlock(4).setColor(Color.BLACK);
        //keep a record of the original points
        Point p1old = game.getBlock(1).getTopLeft();
        //swap the block 1 and 4
        game.swap(1, 3);
        //check if the color of 1 and 4 are swapped
        assertEquals(Color.BLUE, game.getBlock(1).getColor());
        //check if the new point of 1 is the same as the old point of 1
        assertEquals(p1old, game.getBlock(1).getTopLeft());


        //Test 2 - Swap 1 and 4 with recursion
        //smash the block 1 and 4
        game.getBlock(1).smash(3);

        game.getBlock(4).smash(3);

        //manually set the color of 6 and 9 to red and blue, and others to black
        game.getBlock(1).setColor(Color.BLACK);
        game.getBlock(2).setColor(Color.BLACK);
        game.getBlock(3).setColor(Color.BLACK);
        game.getBlock(4).setColor(Color.BLACK);
        game.getBlock(5).setColor(Color.BLACK);
        game.getBlock(7).setColor(Color.BLACK);
        game.getBlock(8).setColor(Color.BLACK);
        game.getBlock(10).setColor(Color.BLACK);
        game.getBlock(11).setColor(Color.BLACK);
        game.getBlock(12).setColor(Color.BLACK);
        game.getBlock(6).setColor(Color.RED);
        game.getBlock(9).setColor(Color.BLUE);

        //check if the parent of 6 and 9 are 1 and 4
        assertEquals(((Block)game.getBlock(6)).getParent(), game.getBlock(1));
        assertEquals(((Block)game.getBlock(9)).getParent(), game.getBlock(4));

        //swap the block 1 and 4
        game.swap(1, 4);

        //Old 9 is the new 5 and the old 6 is the new 10
        assertEquals(Color.BLUE, game.getBlock(5).getColor());
        assertEquals(Color.RED, game.getBlock(10).getColor());


        //Test 3  - smaller blocks
        //Now we swap 5 and 9
        game.swap(5, 10);
        //5 should be red and 10 should be blue
        assertEquals(Color.RED, game.getBlock(5).getColor());
        assertEquals(Color.BLUE, game.getBlock(10).getColor());

    }

    @Test
    public void flatten() {
        //Case 1: Max depth 3
        IGame game = new Game(3, Color.RED);
        //create a new root
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        root.smash(3);
        //manually set the root
        game.setRoot(root);

        //Test 1 - depth 1
        IBlock[][] result1 = game.flatten();
        assertEquals(game.getBlock(1), result1[0][0]);
        assertEquals(game.getBlock(2), result1[0][5]);
        game.getBlock(4).smash(3);
        game.flatten();

        //Test2 - depth 2
        IBlock[][] result2 = game.flatten();
        assertEquals(game.getBlock(8), result2[7][0]);
        assertEquals(game.getBlock(6), result2[4][3]);

        //Test3 - depth 3
        game.getBlock(8).smash(3);
        IBlock[][] result3 = game.flatten();
        assertEquals(game.getBlock(12), result3[7][0]);
        assertEquals(game.getBlock(11), result3[7][1]);
        
        //Case 2: maxDepth 2
        IGame game2 = new Game(2, Color.RED);
        //create a new root
        Block root2 = new Block(new Point(0, 0), new Point(4, 4), 0, null);
        root2.smash(3);
        game2.setRoot(root2);
        game2.flatten();
        game2.getRoot().getBotLeftTree().smash(2);
        IBlock[][] result4 = game2.flatten();
        assertEquals(game2.getBlock(8), result4[3][0]);
    }

    @Test
    public void perimeterScore() {
        IGame game = new Game(3, Color.RED);
        //create a new root
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        root.smash(3);
        //manually set the top left tree and top right tree to be red
        root.getTopLeftTree().setColor(Color.RED);
        root.getTopRightTree().setColor(Color.RED);
        //manually set the bottom left tree and bottom right tree to be blue
        root.getBotLeftTree().setColor(Color.BLUE);
        root.getBotRightTree().setColor(Color.BLUE);
        //manually set the root
        game.setRoot(root);
        //Result should be 4 + 4 + 4 + 4 = 16
        assertEquals(16, game.perimeterScore());
    }

}