package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;
/**
 * The Sun class represents the sun in the game world.
 * It simulates the movement of the sun in a circular trajectory, creating a day-night cycle.
 */
public class Sun {
    public static final String SUN_TAG = "sun";
    private static final float GROUND_HEIGHT_AT_X0 = 2 / 3f;
    public static final float DEGREES_IN_CYCLE = 360;
    private static final float SUN_SIZE = 200;
    private static final Vector2 SUN_DIMENSIONS = new Vector2(SUN_SIZE, SUN_SIZE);
    public static final float HALF = 0.5f;
    public static final float ZERO = 0f;
    /**
     * Creates and returns a GameObject representing the sun.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The duration of the full day-night cycle.
     * @return A GameObject representing the sun.
     */
    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength) {
        OvalRenderable ovalRenderable = new OvalRenderable(Color.yellow);
        float midXAxis = HALF * windowDimensions.x();
        float midSky = windowDimensions.y() * GROUND_HEIGHT_AT_X0 * HALF;
        Vector2 initialSunCenter = new Vector2(midXAxis, midSky);

        GameObject sun = new GameObject(
                Vector2.ZERO,
                SUN_DIMENSIONS,
                ovalRenderable);
        sun.setCenter(initialSunCenter);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        Vector2 cycleCenter = new Vector2(midXAxis, windowDimensions.y() * GROUND_HEIGHT_AT_X0);
        new Transition<Float>(sun,
                (Float angle) -> sun.setCenter(
                        initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter)),
                ZERO,
                DEGREES_IN_CYCLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
        return sun;
    }
}

