package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Flora {
    private static final float LEAF_CREATION_PROBABILITY = 0.7f;
    private static final float FRUIT_CREATION_PROBABILITY = 0.2f; //todo pick right prob
    private static final float TREE_PLANT_PROBABILITY_THRESHOLD = 0.9f;
    private static final float TASK_DELAY_MULTIPLIER = 10f;
    private static final float TRANSITION_DURATION = 5f;
    private static final float MIN_ANGLE = -15f;
    private static final float MAX_ANGLE = 15f;
    private static final float SHRINK_AMOUNT = 15f;

    private final Terrain terrain;
    private static final Random random = new Random();
    private List<Tree> trees = new ArrayList<Tree>();

    public Flora(Terrain terrain){
        this.terrain = terrain;
    }

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

    public List<Fruit> createFruits(Tree tree){
        List<Fruit> fruits = new ArrayList<Fruit>();
        float treeHeight = tree.getTreeTrunkHeight();
        float treeWidth = Block.SIZE;
        // bounds for fruit creation todo i think fruits shouldnt be on trunk...
        float minY = tree.getTopLeftCorner().y() - (treeHeight); // top of tree
        float maxY = tree.getTopLeftCorner().y() + treeHeight - (2*Block.SIZE);
        float minX = tree.getTopLeftCorner().x() - 2 * treeWidth;
        float maxX = tree.getTopLeftCorner().x() + 2 * treeWidth;
        // Trunk bounds
        float trunkMinX = tree.getTopLeftCorner().x() - treeWidth / 2; // Centered trunk width
        float trunkMaxX = tree.getTopLeftCorner().x() + treeWidth / 2;

        // add fruits:
        for (float y = minY; y <= maxY; y += Block.SIZE) {
            for (float x = minX; x <= maxX; x += Block.SIZE) {
                if (x >= trunkMinX && x <= trunkMaxX) {
                    continue; // Skip positions on the trunk
                }
                if (random.nextFloat() <= FRUIT_CREATION_PROBABILITY) {
                    Fruit fruit = new Fruit(new Vector2(x, y));
                    fruits.add(fruit);
                }
            }
        }
        return fruits;
    }

    public List<Leaf> createLeaves(Tree tree){
        List<Leaf> leaves = new ArrayList<Leaf>();
        float treeHeight = tree.getTreeTrunkHeight();
        float treeWidth = Block.SIZE;
        // bounds for leaf creation
        float minY = tree.getTopLeftCorner().y() - (treeHeight); // top of tree
        float maxY = tree.getTopLeftCorner().y() + treeHeight - (2*Block.SIZE);
        float minX = tree.getTopLeftCorner().x() - 2 * treeWidth;
        float maxX = tree.getTopLeftCorner().x() + 2 * treeWidth;
        // add leaves:
        for (float y = minY; y <= maxY; y += Block.SIZE) {
            for (float x = minX; x <= maxX; x += Block.SIZE) {
                if (random.nextFloat() <= LEAF_CREATION_PROBABILITY) {
                    Leaf leaf = createLeafWithTasks(x, y);
                    leaves.add(leaf);
                }
            }
        }
        return leaves;
    }

    private Leaf createLeafWithTasks(float x, float y) {
        Leaf leaf = new Leaf(new Vector2(x, y));
        scheduleLeafAngleTask(leaf);
        scheduleLeafShrinkTask(leaf);
        return leaf;
    }

    private void scheduleLeafAngleTask(Leaf leaf) {
        new ScheduledTask(
                leaf,
                TASK_DELAY_MULTIPLIER * random.nextFloat(),
                false,
                () -> createAngleTransition(leaf)
        );
    }

    private void scheduleLeafShrinkTask(Leaf leaf) {
        new ScheduledTask(
                leaf,
                TASK_DELAY_MULTIPLIER * random.nextFloat(),
                false,
                () -> createShrinkTransition(leaf)
        );
    }

    private void createAngleTransition(Leaf leaf) {
        new Transition<>(
                leaf,
                (Float angle) -> leaf.renderer().setRenderableAngle(angle),
                MIN_ANGLE,
                MAX_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TRANSITION_DURATION,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    private void createShrinkTransition(Leaf leaf) {
        new Transition<>(
                leaf,
                (Float width) -> leaf.setDimensions(new Vector2(width, leaf.getDimensions().y())),
                leaf.getDimensions().x(),
                leaf.getDimensions().x() - SHRINK_AMOUNT,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                TRANSITION_DURATION,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    private static boolean plantOrNotToPlant(){
        float randomFloat = random.nextFloat(0,1);
        return randomFloat > TREE_PLANT_PROBABILITY_THRESHOLD;
    }
}
