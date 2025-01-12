package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
/**
 * The Leaf class represents a leaf object in the game world, rendered as a rectangle with a color based on the base leaf color.
 * The object is assigned a tag ("leaf") for identification.
 */
public class Leaf extends GameObject {
    private static final Vector2 dimensions = new Vector2(Block.SIZE, Block.SIZE);
    private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    public static final String LEAF_TAG = "leaf";
    public static final int COLOR_DELTA = 50;

    /**
     * Constructs a new Leaf object.
     *
     * @param location The position in the game world where the leaf will be placed.
     */
    public Leaf(Vector2 location) {
        super(location, dimensions,
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR, COLOR_DELTA)));
        setTag(LEAF_TAG);
    }
}
