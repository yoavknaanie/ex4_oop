package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;

public class Fruit extends GameObject{
    private static  final Vector2 dimensions = new Vector2(Block.SIZE, Block.SIZE);
    private static final Color BASE_FRUIT_COLOR = new Color (255, 255, 0);
    public static final String FRUIT_TAG = "fruit";

    public Fruit(Vector2 location) {
        super(location, dimensions,
                new OvalRenderable(ColorSupplier.approximateColor(BASE_FRUIT_COLOR, 50)));
        setTag(FRUIT_TAG);
    }


}
