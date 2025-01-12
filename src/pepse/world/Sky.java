package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
/**
 * The Sky class is responsible for creating a game object that represents the sky in the game world.
 * It is rendered with a basic color and spans the entire game window.
 */
public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");
    public static final String skyTag = "sky";

    /**
     * Creates a new sky GameObject that fills the entire game window.
     *
     * @param windowDimensions The dimensions of the window (width and height) to determine
     *                        the size of the sky.
     * @return A new GameObject representing the sky.
     */
    public static GameObject create(Vector2 windowDimensions) {
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag(skyTag);
        return sky;
    }
}
