package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.util.Vector2;
import danogl.gui.rendering.Renderable;

/**
 * The Block class represents a stationary block in the game world.
 * It is immovable and has a fixed size.
 */
public class Block extends GameObject {

    /** The size (width and height) of the block. */
    public static final int SIZE = 30;

    /**
     * Constructs a new Block object at a given position with a specified renderable.
     *
     * @param topLeftCorner The position of the top-left corner of the block in the game world.
     * @param renderable The renderable to display for the block (image, shape, etc.).
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);  // Set block size and position
        physics().preventIntersectionsFromDirection(Vector2.ZERO);  // Prevent intersection in all directions
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);  // Make the block immovable
    }
}
