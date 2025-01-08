package pepse.world.cloud;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class Cloud{
    private static final Vector2 topLeftCorner = new Vector2(-6 * Block.SIZE ,4 * Block.SIZE);
    private List<CloudPiece> cloudBlocks = new ArrayList<CloudPiece>();
    private static final float TRANSITION_DURATION = 10f;

    public Cloud(Vector2 windowDimensions, Consumer<GameObject> addObject,
                 BooleanSupplier avatarJumpingChecker) {
        List<List<Integer>> cloudMatrix = List.of(
                List.of(0, 1, 1, 0, 0, 0),
                List.of(1, 1, 1, 0, 1, 0),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(1, 1, 1, 1, 1, 1),
                List.of(0, 1, 1, 1, 0, 0),
                List.of(0, 0, 0, 0, 0, 0)
        );
        // iterate over the cloudMatrix to create cloud blocks
        for (int row = 0; row < cloudMatrix.size(); row++) {
            for (int col = 0; col < cloudMatrix.get(row).size(); col++) {
                if (cloudMatrix.get(row).get(col) == 1) {
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
                cloudPiecePosition.add(new Vector2(windowDimensions.x() + 6 * Block.SIZE, 0)),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                TRANSITION_DURATION,
                Transition.TransitionType.TRANSITION_ONCE,
                () -> CloudPiece.fadeCloudBlock(cloudPiece)
                );
        return cloudPiece;
    }

    public List<CloudPiece> getBlocks() {
        return cloudBlocks;
    }
}