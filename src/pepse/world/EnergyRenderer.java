package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import java.util.function.Supplier;
/**
 * The EnergyRenderer class is responsible for displaying the current energy level of the avatar or other game objects.
 * It updates the displayed energy percentage as the energy level changes.
 */
public class EnergyRenderer extends GameObject {
    private static final Vector2 DIMENSIONS = new Vector2(100, 30);
    private final Supplier<Integer> energySupplier;
    private final TextRenderable textRenderable;

    /**
     * Constructs a new EnergyRenderer object that displays the energy level.
     *
     * @param position The position of the energy display in the game world.
     * @param energySupplier The supplier that provides the current energy level.
     */
    public EnergyRenderer(Vector2 position, Supplier<Integer> energySupplier) {
        super(position, DIMENSIONS, null);
        this.energySupplier = energySupplier;
        this.textRenderable = new TextRenderable(String.format("%d", energySupplier.get()));
        this.renderer().setRenderable(textRenderable);
//        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Updates the energy display each frame based on the current energy level.
     * The energy percentage is fetched from the energy supplier and displayed as text.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        textRenderable.setString(String.format("%d%%", energySupplier.get()));
    }
}