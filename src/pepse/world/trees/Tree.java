package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

//todo check with friend what suppused to be done with that
public class Tree extends GameObject {
    //todo make the color a varient of these:
    private static final Color BASE_TREE_TRUNK_COLOR = new Color(100, 50, 20);
    private int treeTrunkHeight = 0;
    public static final String TREE_TAG = "tree";

    public Tree(Vector2 groundHeight) {
        super(groundHeight, new Vector2(0, 0), new RectangleRenderable(ColorSupplier.approximateColor(BASE_TREE_TRUNK_COLOR,
                25)));
        setTag(TREE_TAG);
        generateRandomHeight();
        this.setTopLeftCorner(groundHeight.subtract(new Vector2(0,treeTrunkHeight/2)));
        this.setDimensions(new Vector2(Block.SIZE, treeTrunkHeight));
//        this.setTopLeftCorner(groundHeight.subtract(new Vector2(0,treeTrunkHeight/2)));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    private void generateRandomHeight() {
        Random random = new Random();
        treeTrunkHeight = 100 + (int)(random.nextFloat() * 50); // Random height in range [100, 150]
    }

    public int getTreeTrunkHeight() {
        return treeTrunkHeight;
    }
}
