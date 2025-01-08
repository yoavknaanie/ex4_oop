package pepse.world.cloud;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.awt.*;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class CloudPiece extends GameObject {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    private static final Color BASE_DROP_COLOR = new Color(30, 30, 200);
    private static final Vector2 dimensions = new Vector2(Block.SIZE, Block.SIZE);
    private static final Vector2 DROP_DIM = new Vector2(Block.SIZE/4f, Block.SIZE/4f);
    private static final Random random = new Random();
    private static final float RAIN_DROP_PROBABILTY = 0.1f;
    public static final float DROP_TRANSITION_TIME = 1.5f;
    private final BooleanSupplier avatarJumpingChecker;
    private Consumer<GameObject> addObject;
    private boolean dropingRain = false;

    public CloudPiece(Vector2 location, Consumer<GameObject> addObject, BooleanSupplier avatarJumpingChecker) {
        super(location, dimensions,
                new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)));
        this.addObject = addObject;
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); //todo when infinite world
        this.avatarJumpingChecker = avatarJumpingChecker;
    }

    public static void fadeCloudBlock(CloudPiece cloudPiece){
        cloudPiece.renderer().fadeOut(0);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (avatarJumpingChecker.getAsBoolean() && !dropingRain){
            if (random.nextFloat() <= RAIN_DROP_PROBABILTY) {
                dropingRain = true;
                GameObject drop = new GameObject(this.getTopLeftCorner(), DROP_DIM,
                        new RectangleRenderable(BASE_DROP_COLOR));
                addObject.accept(drop);
                new Transition<Float>(
                        drop,
                        drop.renderer()::setOpaqueness,
                        1f,
                        0f,
                        Transition.CUBIC_INTERPOLATOR_FLOAT,
                        DROP_TRANSITION_TIME,
                        Transition.TransitionType.TRANSITION_ONCE,
                        null
                );

                new Transition<>(
                        drop,
                        (Vector2 newPosition) -> drop.setCenter(newPosition),
                        drop.getCenter(),
                        drop.getCenter().add(Vector2.DOWN.mult(Block.SIZE*4)),
                        Transition.LINEAR_INTERPOLATOR_VECTOR, // Ensures constant rate
                        DROP_TRANSITION_TIME,
                        Transition.TransitionType.TRANSITION_ONCE,
                        null
                );
//                new ScheduledTask(
//                        this,
//                        DROP_TRANSITION_TIME,
//                        false,
//                        () -> dropingRain = false);
            }
        }
        if (!avatarJumpingChecker.getAsBoolean()) {
            dropingRain = false;
        }
    }
}
