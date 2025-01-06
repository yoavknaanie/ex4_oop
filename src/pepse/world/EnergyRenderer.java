package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.util.function.Supplier;

public class EnergyRenderer extends GameObject {
    private static final Vector2 DIMENSIONS = new Vector2(100, 30);
    private final Supplier<Integer> energySupplier;
    private final TextRenderable textRenderable;

    public EnergyRenderer(Vector2 position, Supplier<Integer> energySupplier) {
        super(position, DIMENSIONS, null);
        this.energySupplier = energySupplier;
        this.textRenderable = new TextRenderable(String.format("%d", energySupplier.get()));
        this.renderer().setRenderable(textRenderable);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        textRenderable.setString(String.format("%d", energySupplier.get()));
    }
}