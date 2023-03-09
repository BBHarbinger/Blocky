import java.awt.*;

public class TestStep1 {
    public static void main(String[] args) {
        //create a new game
        IGame game = new Game(3, Color.RED);
        //create a new root
        Block root = new Block(new Point(0, 0), new Point(8, 8), 0, null);
        //manually add four children to the root and set the color
        root.setTopLeftTree(new Block(new Point(0, 0), new Point(4, 4), 1, root));
        root.setTopRightTree(new Block(new Point(4, 0), new Point(8, 4), 1, root));
        root.setBotLeftTree(new Block(new Point(0, 4), new Point(4, 8), 1, root));
        root.setBotRightTree(new Block(new Point(4, 4), new Point(8, 8), 1, root));
        root.getTopLeftTree().setColor(Color.BLUE);
        root.getTopRightTree().setColor(Color.RED);
        root.getBotLeftTree().setColor(Color.YELLOW);
        root.getBotRightTree().setColor(Color.GRAY);
        //smash top left child and bottom right child
        root.getTopLeftTree().smash(3);
        root.getBotLeftTree().smash(3);

        root.getTopLeftTree().getTopLeftTree().setColor(Color.BLACK);
        root.getTopLeftTree().getTopRightTree().smash(3);
        root.getTopLeftTree().getBotLeftTree().setColor(Color.YELLOW);
        root.getTopLeftTree().getBotRightTree().setColor(Color.RED);
        root.getBotLeftTree().getTopLeftTree().setColor(Color.MAGENTA);
        root.getBotLeftTree().getTopRightTree().smash(3);
        root.getBotLeftTree().getBotLeftTree().setColor(Color.YELLOW);
        root.getBotLeftTree().getBotRightTree().setColor(Color.RED);

        root.rotate();
        game.setRoot(root);
        GameFrame gamef = new GameFrame();
        //(4)Add the root of the quadtree to the GUI
        gamef.addQuad(game.getRoot());
        gamef.display();

    }

}
