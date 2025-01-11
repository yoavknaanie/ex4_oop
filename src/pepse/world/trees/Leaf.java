package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class Leaf extends GameObject {
    private static  final Vector2 dimensions = new Vector2(Block.SIZE, Block.SIZE);
    private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    public static final String LEAF_TAG = "leaf";


    public Leaf(Vector2 location) {
        super(location, dimensions,
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR, 50)));
        setTag(LEAF_TAG);
    }
}
