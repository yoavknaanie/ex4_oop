package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.util.Vector2;
import danogl.gui.rendering.Renderable;


public class Block extends GameObject {
    public static final int SIZE = 30;

    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}