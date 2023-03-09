import java.awt.*;

import static org.junit.Assert.assertEquals;

public class TestSwap {
    public static void main(String[] args) {
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


        //Test 2 depth 2
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

        //root.rotate();
        //Test 1
        game.swap(1, 4);
        ////If we swap the block 1 and 4, 5 and 9 should be swapped to each other's location
        //We set 5 and 9 to red and blue
        //game.getBlock(5).setColor(Color.RED);
        //game.getBlock(9).setColor(Color.BLUE);
        //We also record their original points
        //Point p5old = game.getBlock(5).getTopLeft();
        //Point p9old = game.getBlock(9).getTopLeft();
        GameFrame gamef = new GameFrame();
        //(4)Add the root of the quadtree to the GUI
        gamef.addQuad(game.getRoot());
        gamef.display();

    }

}

