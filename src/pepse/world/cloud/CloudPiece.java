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
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Represents a single cloud piece in the game world.
 * The cloud piece can generate rain drops when the avatar jumps.
 */
public class CloudPiece extends GameObject {

    /** Base color of the cloud. */
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);
    /** Base color of the rain drop. */
    private static final Color BASE_DROP_COLOR = new Color(30, 30, 200);
    /** Dimensions of the cloud piece. */
    private static final Vector2 dimensions = new Vector2(Block.SIZE, Block.SIZE);
    /** Dimensions of a rain drop. */
    private static final Vector2 DROP_DIM = new Vector2(Block.SIZE / 4f, Block.SIZE / 4f);
    /** Random number generator for rain probability. */
    private static final Random random = new Random();
    /** Probability of generating a rain drop when the avatar jumps. */
    private static final float RAIN_DROP_PROBABILTY = 0.1f;
    /** Duration of the rain drop transition. */
    public static final float DROP_TRANSITION_TIME = 1.5f;
    /** Factor determining how far the rain drop falls. */
    public static final int BLOCK_LOCATION_DOWN_FACTOR = 4;
    /** Initial transparency of the rain drop. */
    public static final float DROP_INITIAL_OPAQUENESS = 1f;
    /** Final transparency of the rain drop. */
    public static final float DROP_END_OPAQUENESS = 0f;
    public static final int ZERO = 0;

    /** Checks if the avatar is jumping. */
    private final BooleanSupplier avatarJumpingChecker;
    /** Consumer to add objects to the game. */
    private Consumer<GameObject> addObject;
    /** Flag indicating whether rain is currently being generated. */
    private boolean dropingRain = false;

    /**
     * Constructs a CloudPiece object at the given location.
     *
     * @param location The position of the cloud piece.
     * @param addObject A function to add objects to the game.
     * @param avatarJumpingChecker A function that checks if the avatar is jumping.
     */
    public CloudPiece(Vector2 location, Consumer<GameObject> addObject, BooleanSupplier avatarJumpingChecker) {
        super(location, dimensions,
                new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR)));
        this.addObject = addObject;
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); //todo when infinite world
        this.avatarJumpingChecker = avatarJumpingChecker;
    }
    /**
     * Fades out the given cloud piece.
     *
     * @param cloudPiece The cloud piece to fade out.
     */
    public static void fadeCloudBlock(CloudPiece cloudPiece){
        cloudPiece.renderer().fadeOut(ZERO);
    }
    /**
     * Updates the cloud piece every frame. If the avatar is jumping,
     * the cloud may generate rain drops based on a probability.
     *
     * @param deltaTime Time passed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (avatarJumpingChecker.getAsBoolean() && !dropingRain){
            if (random.nextFloat() <= RAIN_DROP_PROBABILTY) {
                dropingRain = true;
                GameObject drop = new GameObject(this.getTopLeftCorner(), DROP_DIM,
                        new RectangleRenderable(BASE_DROP_COLOR));
                drop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES); // todo verify
                addObject.accept(drop);
                new Transition<Float>(
                        drop,
                        drop.renderer()::setOpaqueness,
                        DROP_INITIAL_OPAQUENESS,
                        DROP_END_OPAQUENESS,
                        Transition.CUBIC_INTERPOLATOR_FLOAT,
                        DROP_TRANSITION_TIME,
                        Transition.TransitionType.TRANSITION_ONCE,
                        null
                );

                new Transition<>(
                        drop,
                        (Vector2 newPosition) -> drop.setCenter(newPosition),
                        drop.getCenter(),
                        drop.getCenter().add(Vector2.DOWN.mult(Block.SIZE* BLOCK_LOCATION_DOWN_FACTOR)),
                        Transition.LINEAR_INTERPOLATOR_VECTOR, // Ensures constant rate
                        DROP_TRANSITION_TIME,
                        Transition.TransitionType.TRANSITION_ONCE,
                        null
                );
            }
        }
        if (!avatarJumpingChecker.getAsBoolean()) {
            dropingRain = false;
        }
    }
}
