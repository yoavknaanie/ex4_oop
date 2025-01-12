package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import java.awt.*;


/**
 * The Night class represents the transition of nighttime in the game world.
 * It gradually changes the screen's opacity to simulate the effect of nightfall and sunrise.
 */
public class Night {

    /** Tag used to identify the night object. */
    public static final String NIGHT_TAG = "night_tag";

    /** Maximum opacity at midnight. */
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /** Minimum opacity (daytime). */
    private static final float ZERO = 0f;

    /** Factor to determine transition duration for night/day cycle. */
    private static final float HALF = 0.5f;

    /**
     * Creates and returns a GameObject representing the night overlay.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength The duration of the full night-day cycle.
     * @return A GameObject representing the night effect.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<Float>(night,
                night.renderer()::setOpaqueness,
                ZERO,
                MIDNIGHT_OPACITY,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                HALF * cycleLength,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return night;
    }
}
