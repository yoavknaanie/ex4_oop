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
    public static final int FRUIT_ENERGY_BOOST = 10;
    public static final int ZERO = 0;
    public static final int COLOR_DELTA = 50;
    private boolean isGone = false;

    public Fruit(Vector2 location) {
        super(location, dimensions,
                new OvalRenderable(ColorSupplier.approximateColor(BASE_FRUIT_COLOR, COLOR_DELTA)));
        setTag(FRUIT_TAG);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (!isGone && other.getTag().equals(Avatar.AVATAR_TAG)){
            isGone = true;
            ((Avatar) other).increaceEnergy(FRUIT_ENERGY_BOOST);
            renderer().fadeOut(ZERO);
            new ScheduledTask(this, PepseGameManager.CYCLE_LENGTH,
                    false, () -> isGone = false);
            new ScheduledTask(this, PepseGameManager.CYCLE_LENGTH,
                    false, () -> renderer().fadeIn(ZERO));
        }
    }
}