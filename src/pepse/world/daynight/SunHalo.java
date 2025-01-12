package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import java.awt.*;

public class SunHalo {
    public static final float SUN_HALO_RATIO = 1.5f;
    private static final String HALO_TAG = "sun halo tag";
    public static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);

    public static GameObject create(GameObject sun){
//        Color sunHaloColor = SUN_HALO_COLOR;
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
