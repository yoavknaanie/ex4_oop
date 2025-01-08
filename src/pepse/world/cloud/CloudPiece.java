package pepse.world.cloud;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.awt.*;
import java.util.Random;

public class CloudPiece extends GameObject {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
//    private static final Color BASE_DROP_COLOR = new Color(135, 206, 235);
    private static final Color BASE_DROP_COLOR = new Color(0, 0, 0);
    private static final Vector2 dimensions = new Vector2(Block.SIZE, Block.SIZE);
    private static final Vector2 DROP_DIM = new Vector2(Block.SIZE/4f, Block.SIZE/4f);
    private static final Random random = new Random();
    private static final float FRUIT_CREATION_PROBABILITY = 0.3f;
    private static final float TRANSITION_DURATION = 2f;

    public CloudPiece(Vector2 location) {
        super(location, dimensions,
                new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)));
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); //todo when infinite world
    }

    public static void fadeCloudBlock(CloudPiece cloudPiece){
        cloudPiece.renderer().fadeOut(0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //todo if (avatarJump) {
        if (random.nextFloat() <= FRUIT_CREATION_PROBABILITY) {
            GameObject drop = new GameObject(this.getTopLeftCorner(), DROP_DIM,
                    new RectangleRenderable(BASE_DROP_COLOR));
            new Transition<Float>(
                    drop,
                    drop.renderer()::setOpaqueness,
                    1f,
                    0f,
                    Transition.CUBIC_INTERPOLATOR_FLOAT,
                    TRANSITION_DURATION,
                    Transition.TransitionType.TRANSITION_ONCE,
                    null
            );
        }
    }
}
