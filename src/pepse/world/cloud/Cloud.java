package pepse.world.cloud;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * The Cloud class represents a cloud formation in the game world. It consists of multiple CloudPiece objects
 * arranged in a predefined pattern. The cloud moves horizontally across the screen and fades out when it completes
 * its transition.
 */
public class Cloud {

    public static final int IS_DROP = 1;
    /** Factor determining the vertical position of cloud pieces relative to the block size. */
    public static final int CLOUD_PIECE_POSITION_VERTICAL_FACTOR = 6;
    public static final List<List<Integer>> CLOUD_MATRIX = List.of(
            List.of(0, 1, 1, 0, 0, 0),
            List.of(1, 1, 1, 0, 1, 0),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0)
    );
    public static final int ZERO = 0;

    /** The initial top-left corner position of the cloud formation. */
    private static final Vector2 topLeftCorner = new Vector2(-6 * Block.SIZE, 4 * Block.SIZE);
    /** List holding all the cloud pieces forming the cloud. */
    private List<CloudPiece> cloudBlocks = new ArrayList<>();
    /** Duration of the cloud transition animation. */
    private static final float TRANSITION_DURATION = 50f;

    /**
     * Constructs a Cloud object and generates its cloud pieces based on a predefined cloud matrix.
     * @param windowDimensions The dimensions of the game window.
     * @param addObject A consumer function to add objects to the game.
     * @param avatarJumpingChecker A boolean supplier to check if the avatar is jumping.
     */
    public Cloud(Vector2 windowDimensions, Consumer<GameObject> addObject,
                 BooleanSupplier avatarJumpingChecker) {
        // iterate over the cloudMatrix to create cloud blocks
        for (int row = ZERO; row < CLOUD_MATRIX.size(); row++) {
            for (int col = ZERO; col < CLOUD_MATRIX.get(row).size(); col++) {
                if (CLOUD_MATRIX.get(row).get(col) == IS_DROP) {
                    // calculate the position of the cloud block
                    Vector2 cloudPiecePosition = new Vector2(
                            topLeftCorner.x() + col * Block.SIZE,
                            topLeftCorner.y() + row * Block.SIZE
                    );
                    // create a new cloud block
                    CloudPiece cloudPiece = createPieceWithTask(cloudPiecePosition, windowDimensions,
                            addObject, avatarJumpingChecker);
                    cloudBlocks.add(cloudPiece);
                }
            }
        }
    }

    private CloudPiece createPieceWithTask(Vector2 cloudPiecePosition, Vector2 windowDimensions,
                                           Consumer<GameObject> addObject, BooleanSupplier avatarJumpingChecker) {
        CloudPiece cloudPiece = new CloudPiece(cloudPiecePosition, addObject, avatarJumpingChecker);
        new Transition<Vector2>(
                cloudPiece,
                (Vector2 position) -> cloudPiece.setCenter(position.add(Vector2.RIGHT)),
                cloudPiecePosition,
                cloudPiecePosition.add(new Vector2(windowDimensions.x() +
                        CLOUD_PIECE_POSITION_VERTICAL_FACTOR * Block.SIZE, ZERO)),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                TRANSITION_DURATION,
                Transition.TransitionType.TRANSITION_ONCE,
                () -> CloudPiece.fadeCloudBlock(cloudPiece)
                );
        return cloudPiece;
    }

    /**
     * Returns the list of cloud pieces forming the cloud.
     *
     * @return A list of CloudPiece objects.
     */
    public List<CloudPiece> getBlocks() {
        return cloudBlocks;
    }
}