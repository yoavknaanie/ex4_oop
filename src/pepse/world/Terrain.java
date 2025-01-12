package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Terrain class generates a procedurally generated terrain for the game world, with a ground that varies in height
 * based on noise. The terrain is built by creating blocks in a specified range.
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123,74);
    public static final String GROUND_TAG = "ground";
    public static final float WINDOW_PROPORTIONS = ((float) 2 / 3);
    public static final int NOISE_FACTOR = 10 * Block.SIZE;
    private final Vector2 windowDimensions;
    public final float groundHeightAtX0; //todo should be static?
    private static final int TERRAIN_DEPTH = 20;
    private final NoiseGenerator noiseGenerator;
    /**
     * Constructs a new Terrain object with the specified window dimensions and seed for terrain generation.
     *
     * @param windowDimensions The dimensions of the game window (width and height).
     * @param seed The seed value used to generate the noise for the terrain.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        groundHeightAtX0 =  WINDOW_PROPORTIONS * windowDimensions.y();
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed, (int)groundHeightAtX0);
    }

    /**
     * Returns the height of the ground at a specific x-coordinate, based on noise generation.
     *
     * @param x The x-coordinate for which to determine the ground height.
     * @return The ground height at the given x-coordinate.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, NOISE_FACTOR);
        return (float) Math.floor((groundHeightAtX0 + noise)/Block.SIZE) * Block.SIZE;
//        new NoiseGenerator(seed, (int)groundHeightAtX0);
//        return groundHeightAtX0;
    }

    /**
     * Creates a list of blocks representing the terrain in a given horizontal range.
     *
     * @param minX The starting x-coordinate for the terrain creation range.
     * @param maxX The ending x-coordinate for the terrain creation range.
     * @return A list of Block objects representing the terrain in the specified range.
     */
    public List<Block> createInRange(int minX, int maxX) {
        int leftBlockLocation = (int) Math.floor((double) minX /Block.SIZE) * Block.SIZE;
        int rightBlockLocation = (int) Math.floor((double) maxX /Block.SIZE) * Block.SIZE;
//        int numberOfBricksInRow = (rightBlockLocation - leftBlockLocation)/Block.SIZE;
        List<Block> blocks = new ArrayList<Block>();

        for(int colX = leftBlockLocation; colX != rightBlockLocation; colX += Block.SIZE) {
//            int y = (int) Math.floor((double) groundHeightAt(colX) / Block.SIZE) * Block.SIZE;
            int y = (int) groundHeightAt(colX);
            for (int rowBlocks = 0; rowBlocks < TERRAIN_DEPTH; rowBlocks++){
                addBlockToBlock(colX, y + rowBlocks * Block.SIZE, blocks);
            }
        }
        return blocks;
    }


    private static void addBlockToBlock(int colX, int y, List<Block> blocks) {
        RectangleRenderable groundRender =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        Vector2 newBlockLocation = new Vector2(colX, y);
        Block block = new Block(newBlockLocation, groundRender);
        block.setTag(GROUND_TAG);
        blocks.add(block);
    }

    /**
     * Returns the ground height at x=0.
     *
     * @return The ground height at x=0.
     */
    public float getGroundHeightAtX0(){
        return groundHeightAtX0;
    }
}
