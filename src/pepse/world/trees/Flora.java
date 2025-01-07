package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Flora {
    private final Terrain terrain;
    private static final Random random = new Random();
    private List<Tree> trees = new ArrayList<Tree>();

    //    todo think about the return val:
    public List<Tree> createInRange(int minX, int maxX, Terrain terrain) {
//        List<Tree> trees = new ArrayList<Tree>();
        for (int x = minX+Block.SIZE/2; x <= maxX; x+= Block.SIZE) {
            // Create a tree at each X-coordinate within the range
            if (plantOrNotToPlant()){
                int groundHeight = ((int) terrain.getGroundHeightAtX0())+ 1;
                Tree tree = new Tree(new Vector2(x, groundHeight));
                trees.add(tree);
            }
        }
        return trees;
    }

    public List<Leaf> createLeaves(Tree tree){
        // height of leaves: from [ground] till [2*treeHeight]
        // X of trees: from [center of tree-(treeWidth*4)] till [center of tree+(treeWidth*4)]
        // create a matrix of height*width
        // itarate through the matrix, and in probability of 0.1 create a leaf in that position
        List<Leaf> leaves = new ArrayList<>();
        float treeHeight = tree.getTreeTrunkHeight();
        float treeWidth = Block.SIZE;

        // bounds for leaf creation
        float minY = tree.getTopLeftCorner().y() - (treeHeight); // top of tree
        float maxY = tree.getTopLeftCorner().y() + treeHeight - (2*Block.SIZE);
        float minX = tree.getTopLeftCorner().x() - 2 * treeWidth;
        float maxX = tree.getTopLeftCorner().x() + 2 * treeWidth;

        for (float y = minY; y <= maxY; y += Block.SIZE) {
            for (float x = minX; x <= maxX; x += Block.SIZE) {
                if (random.nextFloat() <= 0.35) {
                    Leaf leaf = new Leaf(new Vector2(x, y));
                    leaves.add(leaf);

                    new Transition<Float>(leaf,
                            (Float angle) -> leaf.renderer().setRenderableAngle(angle),
                            -15f,
                            15f,
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            5, // cycleLength
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                    new Transition<Float>(leaf,
                            (Float width) -> leaf.setDimensions(new Vector2(width, leaf.getDimensions().y())),
                            leaf.getDimensions().x(),
                            leaf.getDimensions().x() - 15,
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            5, // cycleLength
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null);
                }
            }
        }
        return leaves;
    }

    private static boolean plantOrNotToPlant(){
        float randomFloat = random.nextFloat(0,1);
        return randomFloat > 0.9;
    }

    public Flora(Terrain terrain){
        this.terrain = terrain;
    }
}
