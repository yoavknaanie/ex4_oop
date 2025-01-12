package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 * The Tree class represents a tree object in the game world, with a trunk that has a randomized height.
 * It is rendered as a rectangle with a color based on a base color, and it is assigned a tag ("tree") for identification.
 */
public class Tree extends GameObject {
    private static final Color BASE_TREE_TRUNK_COLOR = new Color(100, 50, 20);
    public static final int ZERO = 0;
    public static final int COLOR_DELTA = 25;
    public static final float HALF = 0.5f;
    public static final int SEED = 42;
    public static final int TREE_BASE_HEIGHT = 100;
    public static final int TREE_MAX_HEIGHT_ADD = 50;
    private int treeTrunkHeight = 0;
    public static final String TREE_TAG = "tree";

    /**
     * Constructs a new Tree object.
     *
     * @param groundHeight The position in the game world where the tree's trunk will be placed.
     *                     This value is used to set the tree's position and calculate its height.
     */
    public Tree(Vector2 groundHeight) {
        super(groundHeight, new Vector2(ZERO, ZERO),
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_TREE_TRUNK_COLOR,
                        COLOR_DELTA)));
        setTag(TREE_TAG);
        generateRandomHeight((int) groundHeight.x());
        this.setTopLeftCorner(groundHeight.subtract(new Vector2(ZERO,treeTrunkHeight * HALF)));
        this.setDimensions(new Vector2(Block.SIZE, treeTrunkHeight));
//        this.setTopLeftCorner(groundHeight.subtract(new Vector2(0,treeTrunkHeight/2)));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    private void generateRandomHeight(int x) {
//        Random random = new Random();
        Random random = new Random(Objects.hash(x, SEED));
        treeTrunkHeight = TREE_BASE_HEIGHT + (int)(random.nextFloat() * TREE_MAX_HEIGHT_ADD); // Random height in range [100, 150]
    }
    /**
     * Returns the height of the tree trunk.
     *
     * @return The height of the tree trunk.
     */
    public int getTreeTrunkHeight() {
        return treeTrunkHeight;
    }
}
