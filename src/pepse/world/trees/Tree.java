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

//todo check with friend what suppused to be done with that
public class Tree extends GameObject {
    //todo make the color a varient of these:
    private static final Color BASE_TREE_TRUNK_COLOR = new Color(100, 50, 20);
    public static final int ZERO = 0;
    public static final int COLOR_DELTA = 25;
    public static final float HALF = 0.5f;
    public static final int SEED = 42;
    public static final int TREE_BASE_HEIGHT = 100;
    public static final int TREE_MAX_HEIGHT_ADD = 50;
    private int treeTrunkHeight = 0;
    public static final String TREE_TAG = "tree";

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

    public int getTreeTrunkHeight() {
        return treeTrunkHeight;
    }
}
