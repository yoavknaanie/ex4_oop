package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import java.awt.*;

/**
 * The SunHalo class represents a glowing effect around the sun.
 * It follows the sun's position to create a realistic halo effect.
 */
public class SunHalo {

    /** The ratio by which the sun halo's size is larger than the sun itself. */
    public static final float SUN_HALO_RATIO = 1.5f;

    /** Tag used to identify the sun halo object. */
    private static final String HALO_TAG = "sun halo tag";

    /** The color of the sun halo with transparency for a glowing effect. */
    public static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);

    /**
     * Creates and returns a GameObject representing the sun halo.
     * The halo is slightly larger than the sun and follows its movement.
     *
     * @param sun The sun GameObject around which the halo is created.
     * @return A GameObject representing the sun halo.
     */
    public static GameObject create(GameObject sun){
        OvalRenderable haloRenderable = new OvalRenderable(SUN_HALO_COLOR);
        Vector2 haloDimensions = sun.getDimensions().mult(SUN_HALO_RATIO);

        GameObject sunHalo = new GameObject(Vector2.ZERO, haloDimensions, haloRenderable);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setCenter(sun.getCenter());
        sun.setTag(HALO_TAG);

        sunHalo.addComponent(
                (deltaTime -> sunHalo.setCenter(sun.getCenter()))
        );
        return sunHalo;
    }
}
