package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Avatar;
import pepse.world.Block;

import java.awt.*;

public class Fruit extends GameObject{
    private static  final Vector2 dimensions = new Vector2(Block.SIZE, Block.SIZE);
    private static final Color BASE_FRUIT_COLOR = new Color (255, 255, 0);
    public static final String FRUIT_TAG = "fruit";
    public static final String GONE_TAG = "fruit already eaten";
    public static final int FRUIT_ENERGY_BOOST = 10;
    private boolean isGone = false;

    public Fruit(Vector2 location) {
        super(location, dimensions,
                new OvalRenderable(ColorSupplier.approximateColor(BASE_FRUIT_COLOR, 50)));
        setTag(FRUIT_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (!isGone){
    //            setTag(GONE_TAG);
            isGone = true;
            ((Avatar) other).increaceEnergy(FRUIT_ENERGY_BOOST);
            renderer().fadeOut(0);
            new ScheduledTask(this, PepseGameManager.CYCLE_LENGTH,
                    false, () -> isGone = false);
            new ScheduledTask(this, PepseGameManager.CYCLE_LENGTH,
                    false, () -> renderer().fadeIn(0));
        }
    }
}